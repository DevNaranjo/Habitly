package com.habitly.data;

import com.habitly.model.EstadoVivienda;
import com.habitly.model.Usuario;
import com.habitly.model.Vivienda;
import com.habitly.model.ContratoAlquiler;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Motor central de lógica de negocio y persistencia.
 * Coordina las colecciones de datos en memoria con el almacenamiento seguro
 * en disco mediante cifrado AES.
 * v1.0.5: Añade soporte para cumplimiento legal (Etapa 5) y mantiene retrocompatibilidad.
 * @author DevNaranjo
 * @version 1.0.5
 * @since 1.0.0
 */
public class GestorInventario {

    private ArrayList<Vivienda> inventario;
    private HashMap<String, Usuario> usuarios;
    private Usuario usuarioIdentificado;

    private final String CARPETA_DATA = "data";
    private final String RUTA_SISTEMA = CARPETA_DATA + File.separator + "sistema.dat";

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
    public void guardarDatos() {
        try {
            File directorio = new File(CARPETA_DATA);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            CajaFuerte maleta = new CajaFuerte(this.inventario, this.usuarios);
            CryptoManager.guardarObjetoCifrado(maleta, RUTA_SISTEMA);
        } catch (Exception e) {
            System.err.println("Error crítico al cifrar los datos: " + e.getMessage());
        }
    }

    /**
     * Recupera y descifra el archivo de sistema desde /data.
     */
    public void cargarDatos() {
        File archivo = new File(RUTA_SISTEMA);
        if (archivo.exists() && archivo.length() > 0) {
            try {
                Object cargado = CryptoManager.leerObjetoCifrado(RUTA_SISTEMA);
                if (cargado instanceof CajaFuerte maleta) {
                    if (maleta.listaViviendas != null) this.inventario = maleta.listaViviendas;
                    if (maleta.mapaUsuarios != null) this.usuarios = maleta.mapaUsuarios;
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
        File archivo = new File(RUTA_SISTEMA);
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
        if (usuarios.containsKey(dni)) {
            usuarios.remove(dni);
            return true;
        }
        return false;
    }

    // --- MÉTODOS DE LÓGICA DE NEGOCIO (ETAPA 5) ---

    /**
     * Formaliza legalmente un contrato vinculándolo a una vivienda.
     * Valida disponibilidad y cumplimiento del índice IRAV.
     */
    public String formalizarContrato(ContratoAlquiler contrato, Vivienda vivienda) {
        if (vivienda.getEstado() != EstadoVivienda.DISPONIBLE) {
            return "ERROR: La vivienda no está disponible para alquiler.";
        }
        if (contrato.getRentaMensual() > vivienda.getLimiteMaximoIrav()) {
            return "ERROR LEGAL: La renta propuesta supera el límite máximo del índice IRAV ("
                    + vivienda.getLimiteMaximoIrav() + "€).";
        }

        vivienda.setContratoActivo(contrato);
        vivienda.setInquilino(buscarPorDni(contrato.getDniInquilino()));
        vivienda.setEstado(EstadoVivienda.ALQUILADA);
        guardarDatos();
        return "ÉXITO: Contrato formalizado.";
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
        return new ArrayList<>(usuarios.values());
    }

    public void añadirVivienda(Vivienda v) {
        if (v != null) inventario.add(v);
    }

    public ArrayList<Vivienda> getInventario() {
        return inventario;
    }

    public Usuario buscarPorDni(String dni) {
        return usuarios.get(dni);
    }
}