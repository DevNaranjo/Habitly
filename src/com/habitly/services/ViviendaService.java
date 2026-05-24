package com.habitly.services;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.List;

/**
 * Servicio de aplicación para la gestión de viviendas.
 * Separa la lógica de negocio de la interfaz de usuario por consola.
 */
public class ViviendaService {
    private final GestorInventario gestor;

    public ViviendaService(GestorInventario gestor) {
        this.gestor = gestor;
    }

    /**
     * Registra un nuevo Piso en el inventario.
     */
    public boolean registrarPiso(String dniProp, String direccion, double precio, double m2, int hab, int banos,
                                 boolean garaje, boolean piscina, boolean amueblado, String cons, int planta, String puerta, boolean esTuristico) {
        Piso p = new Piso(dniProp, direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, planta, puerta);
        p.setEsAlquilerTuristico(esTuristico);
        gestor.añadirVivienda(p);
        gestor.guardarDatos();
        return true;
    }

    /**
     * Registra una nueva Casa en el inventario.
     */
    public boolean registrarCasa(String dniProp, String direccion, double precio, double m2, int hab, int banos,
                                 boolean garaje, boolean piscina, boolean amueblado, String cons, double parcela, boolean esTuristico) {
        Casa c = new Casa(dniProp, direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, parcela);
        c.setEsAlquilerTuristico(esTuristico);
        gestor.añadirVivienda(c);
        gestor.guardarDatos();
        return true;
    }

    /**
     * Registra un gasto/suministro en la vivienda y guarda el estado.
     */
    public boolean registrarGastoSuministro(Vivienda v, String concepto, double monto) {
        if (v == null) return false;
        String idGasto = "G-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        v.registrarFactura(new Gasto(idGasto, concepto, monto));
        gestor.guardarDatos();
        return true;
    }

    /**
     * Registra un pago de renta para la vivienda.
     */
    public boolean registrarPagoRenta(Vivienda v, double cuota) {
        if (v == null) return false;
        v.registrarPago(cuota);
        gestor.guardarDatos();
        return true;
    }

    /**
     * Aplica el porcentaje de IPC general a todas las viviendas.
     */
    public void aplicarIPCGeneral(double porcentaje) {
        for (Vivienda v : gestor.getInventario()) {
            if (v != null) {
                v.aplicarSubidaAnual(porcentaje);
            }
        }
        gestor.guardarDatos();
    }

    /**
     * Elimina una vivienda del inventario global.
     */
    public boolean eliminarVivienda(Vivienda v) {
        if (v == null) return false;
        if (v.getEstado() != EstadoVivienda.DISPONIBLE) {
            return false;
        }
        int indexGlobal = gestor.getInventario().indexOf(v);
        if (indexGlobal != -1) {
            boolean success = gestor.eliminarVivienda(indexGlobal);
            if (success) {
                gestor.guardarDatos();
                return true;
            }
        }
        return false;
    }

    /**
     * Establece el límite IRAV de una vivienda.
     */
    public void asignarLimiteIrav(Vivienda v, double limite) {
        if (v != null) {
            v.setLimiteMaximoIrav(limite);
            gestor.guardarDatos();
        }
    }

    /**
     * Liquida un gasto (suministro) marcándolo como pagado y guardando los datos.
     */
    public boolean liquidarGasto(Vivienda v, Gasto g) {
        if (v == null || g == null) return false;
        g.marcarComoPagado();
        gestor.guardarDatos();
        return true;
    }
}
