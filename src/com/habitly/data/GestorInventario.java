package com.habitly.data;

import com.habitly.model.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import com.habitly.config.AppConfig;

/**
 * Motor central de lógica de negocio y persistencia.
 * Coordina las colecciones de datos en memoria con el almacenamiento seguro
 * en disco mediante cifrado AES.
 * v1.0.5: Añade soporte para cumplimiento legal (Etapa 5) y mantiene retrocompatibilidad.
 * @author DevNaranjo
 * @version 1.0.7-F
 * @since 1.0.0
 */
public class GestorInventario {

    private ArrayList<Vivienda> inventario;
    private HashMap<String, Usuario> usuarios;
    private Usuario usuarioIdentificado;

    // La configuración de rutas se delega a AppConfig

    public GestorInventario() {
        this.inventario = new ArrayList<>();
        this.usuarios = new HashMap<>();
        this.usuarioIdentificado = null;
    }

    // --- MÉTODOS DE PERSISTENCIA ---

    /**
     * Serializa el inventario y los usuarios, aplica cifrado AES-128
     * y guarda el resultado en el repositorio local.
     */
    public boolean guardarDatos() {
        try {
            AppConfig.asegurarDirectorio();
            CajaFuerte maleta = new CajaFuerte(this.inventario, this.usuarios);
            CryptoManager.guardarObjetoCifrado(maleta, AppConfig.getFullFilePath());
            return true;
        } catch (Exception e) {
            System.err.println("Error crítico al cifrar los datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera y descifra el archivo de sistema desde /data.
     */
    public void cargarDatos() {
        AppConfig.asegurarDirectorio();
        File archivo = new File(AppConfig.getFullFilePath());
        if (archivo.exists() && archivo.length() > 0) {
            try {
                Object cargado = CryptoManager.leerObjetoCifrado(AppConfig.getFullFilePath());
                if (cargado instanceof CajaFuerte maleta) {
                    if (maleta.getListaViviendas() != null) this.inventario = maleta.getListaViviendas();
                    if (maleta.getMapaUsuarios() != null) this.usuarios = maleta.getMapaUsuarios();
                }
            } catch (Exception e) {
                System.err.println("[!] Error al descifrar el archivo de datos.");
            }
        }
    }

    /**
     * Borrado de fábrica: Limpia la memoria RAM y elimina el archivo físico.
     * @return true si se eliminó correctamente.
     */
    public boolean borrarDatosAplicacion() {
        this.inventario.clear();
        this.usuarios.clear();
        this.usuarioIdentificado = null;
        File archivo = new File(AppConfig.getFullFilePath());
        if (archivo.exists()) {
            return archivo.delete();
        }
        return true;
    }

    // --- MÉTODOS DE COMPATIBILIDAD ---

    /** @return true si el inventario de viviendas está vacío. */
    public boolean estaVacio() {
        return inventario.isEmpty();
    }

    /** @return Cantidad de viviendas registradas. */
    public int tamañoInventario() {
        return inventario.size();
    }

    /**
     * Obtiene una vivienda según su índice en el ArrayList.
     * @param index Posición de la vivienda.
     * @return Objeto Vivienda o null si el índice es inválido.
     */
    public Vivienda obtenerVivienda(int index) {
        if (index >= 0 && index < inventario.size()) {
            return inventario.get(index);
        }
        return null;
    }

    /**
     * Elimina un usuario del sistema.
     * @param dni Identificador del usuario.
     * @return true si se eliminó con éxito.
     */
    public boolean eliminarUsuario(String dni) {
        if (!usuarios.containsKey(dni)) return false;
        
        Usuario u = usuarios.get(dni);
        if (u instanceof Propietario) {
            if (!getViviendasPorDueño(dni).isEmpty()) {
                System.out.println("[!] ERROR DE INTEGRIDAD: No se puede eliminar a un Propietario que tiene viviendas registradas.");
                return false;
            }
        } else if (u instanceof Inquilino) {
            if (getViviendaDeInquilino(dni) != null) {
                System.out.println("[!] ERROR DE INTEGRIDAD: No se puede eliminar a un Inquilino con un contrato de alquiler activo.");
                return false;
            }
        }
        
        usuarios.remove(dni);
        return true;
    }

    // --- MÉTODOS DE LÓGICA DE NEGOCIO (ETAPA 5) ---

    /**
     * Formaliza legalmente un contrato vinculándolo a una vivienda.
     * Valida disponibilidad, duración legal, límites IRAV y límites de fianza/garantías adicionales.
     */
    public String formalizarContrato(ContratoAlquiler contrato, Vivienda vivienda) {
        if (vivienda.getEstado() != EstadoVivienda.DISPONIBLE) {
            return "ERROR: La vivienda no está disponible para alquiler.";
        }
        
        Usuario futuroInq = obtenerUsuario(contrato.getDniInquilino());
        if (futuroInq == null || !(futuroInq instanceof Inquilino)) {
            return "ERROR DE INTEGRIDAD: El usuario especificado no existe o no tiene el rol de Inquilino.";
        }
        if (contrato.getRentaMensual() > vivienda.getLimiteMaximoIrav()) {
            return "ERROR LEGAL: La renta propuesta supera el límite máximo del índice IRAV ("
                    + vivienda.getLimiteMaximoIrav() + "€).";
        }
        if (!contrato.cumpleDuracionMinimaLegal()) {
            int minimoRequerido = (contrato.getTipoArrendador() == TipoArrendador.FISICO) ? 60 : 84;
            return "ERROR LEGAL: Duración insuficiente. La ley exige un mínimo de " + minimoRequerido + " meses.";
        }
        if (!contrato.validarFianzaLegal()) {
            return "ERROR LEGAL: El importe de la fianza debe ser exactamente 1 mes de renta y las garantías adicionales no pueden superar las 2 mensualidades.";
        }

        vivienda.setContratoActivo(contrato);
        vivienda.setInquilino(obtenerUsuario(contrato.getDniInquilino()));
        vivienda.setEstado(EstadoVivienda.ALQUILADA);
        guardarDatos();
        return "ÉXITO: Contrato formalizado.";
    }

    /**
     * Recorre todos los contratos activos y actualiza su renta mensual según el porcentaje IRAV.
     * @param porcentajeIrav Porcentaje del índice de actualización de renta.
     * @return El número de contratos actualizados.
     */
    public int aplicarActualizacionAnualIRAV(double porcentajeIrav) {
        return aplicarActualizacionAnualIRAV(porcentajeIrav, false);
    }

    /**
     * Recorre todos los contratos activos y actualiza su renta mensual según el porcentaje IRAV,
     * permitiendo forzar la actualización para pruebas o simulaciones.
     * @param porcentajeIrav Porcentaje del índice de actualización de renta.
     * @param forzar true si se desea forzar la actualización sin importar la fecha de aniversario.
     * @return El número de contratos actualizados.
     */
    public int aplicarActualizacionAnualIRAV(double porcentajeIrav, boolean forzar) {
        int actualizados = 0;
        for (Vivienda v : inventario) {
            if (v.getContratoActivo() != null && v.getContratoActivo().estaVigente()) {
                if (v.getContratoActivo().aplicarActualizacionIRAV(porcentajeIrav, forzar)) {
                    actualizados++;
                }
            }
        }
        if (actualizados > 0) {
            guardarDatos();
        }
        return actualizados;
    }

    /**
     * Recorre los contratos activos de un propietario y actualiza su renta según el porcentaje IRAV.
     * @param dniPropietario DNI del propietario.
     * @param porcentajeIrav Porcentaje del índice de actualización de renta.
     * @param forzar true si se desea forzar la actualización.
     * @return El número de contratos actualizados.
     */
    public int aplicarActualizacionAnualIRAV(String dniPropietario, double porcentajeIrav, boolean forzar) {
        int actualizados = 0;
        for (Vivienda v : getViviendasPorDueño(dniPropietario)) {
            if (v.getContratoActivo() != null && v.getContratoActivo().estaVigente()) {
                if (v.getContratoActivo().aplicarActualizacionIRAV(porcentajeIrav, forzar)) {
                    actualizados++;
                }
            }
        }
        if (actualizados > 0) {
            guardarDatos();
        }
        return actualizados;
    }

    /**
     * Obtiene una lista de contratos cuya fecha de vencimiento está próxima a vencer.
     * @param diasMargen Días de margen para alertar.
     * @return Lista de contratos próximos a vencer.
     */
    public List<ContratoAlquiler> obtenerContratosProximosAVencer(int diasMargen) {
        List<ContratoAlquiler> proximos = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(diasMargen);
        for (Vivienda v : inventario) {
            if (v.getContratoActivo() != null && v.getContratoActivo().estaVigente()) {
                LocalDate vto = v.getContratoActivo().getFechaVencimiento();
                if (vto != null && (vto.isEqual(hoy) || (vto.isAfter(hoy) && vto.isBefore(limite.plusDays(1))))) {
                    proximos.add(v.getContratoActivo());
                }
            }
        }
        return proximos;
    }

    /** @return Lista de contratos con fianzas pendientes de depósito legal (ICAVI). */
    public List<ContratoAlquiler> obtenerContratosPendientesFianza() {
        List<ContratoAlquiler> pendientes = new ArrayList<>();
        for (Vivienda v : inventario) {
            if (v.getContratoActivo() != null && !v.getContratoActivo().isFianzaDepositada()) {
                pendientes.add(v.getContratoActivo());
            }
        }
        return pendientes;
    }

    // --- MÉTODOS DE FILTRADO Y CONSULTA ---

    /** @return Lista de viviendas que pertenecen a un propietario concreto. */
    public List<Vivienda> getViviendasPorDueño(String idDueño) {
        List<Vivienda> filtradas = new ArrayList<>();
        for (Vivienda v : inventario) {
            if (v.getIdPropietario() != null && v.getIdPropietario().equals(idDueño)) {
                filtradas.add(v);
            }
        }
        return filtradas;
    }

    /** @return Vivienda donde reside el inquilino especificado. */
    public Vivienda getViviendaDeInquilino(String idInquilino) {
        for (Vivienda v : inventario) {
            if (v.getInquilino() != null && v.getInquilino().getDni().equals(idInquilino)) {
                return v;
            }
        }
        return null;
    }

    // --- MÉTODOS DE LÓGICA DE USUARIOS ---

    public boolean añadirUsuario(Usuario nuevoUsuario) {
        if (nuevoUsuario == null || usuarios.containsKey(nuevoUsuario.getDni())) return false;
        usuarios.put(nuevoUsuario.getDni(), nuevoUsuario);
        return true;
    }

    public Usuario obtenerUsuario(String dni) {
        return usuarios.get(dni);
    }

    public void setUsuarioIdentificado(Usuario u) { this.usuarioIdentificado = u; }

    public Usuario getUsuarioIdentificado() { return usuarioIdentificado; }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return Collections.unmodifiableList(new ArrayList<>(usuarios.values()));
    }

    public void añadirVivienda(Vivienda v) {
        if (v != null) inventario.add(v);
    }

    public List<Vivienda> getInventario() {
        return Collections.unmodifiableList(inventario);
    }

    /**
     * Calcula el beneficio neto de un propietario: ingresos cobrados - gastos registrados.
     * @param dniPropietario DNI del propietario.
     * @return double con el beneficio neto.
     */
    public double getBeneficioTotal(String dniPropietario) {
        double ingresos = 0;
        double gastos = 0;
        for (Vivienda v : getViviendasPorDueño(dniPropietario)) {
            ingresos += v.getTotalPagadoMes();
            for (Gasto g : v.getHistorialGastos()) {
                gastos += g.getMonto();
            }
        }
        return ingresos - gastos;
    }

    /**
     * Elimina una vivienda del inventario según su índice.
     * La vivienda solo se puede eliminar si está en estado DISPONIBLE.
     * @param index Posición de la vivienda en el inventario.
     * @return true si se eliminó con éxito, false en caso contrario.
     */
    public boolean eliminarVivienda(int index) {
        if (index >= 0 && index < inventario.size()) {
            Vivienda v = inventario.get(index);
            if (v.getEstado() == EstadoVivienda.DISPONIBLE) {
                inventario.remove(index);
                guardarDatos();
                return true;
            }
        }
        return false;
    }
}