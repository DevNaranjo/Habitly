package com.habitly.services;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;

/**
 * Servicio de aplicación para la gestión de contratos de alquiler y cumplimiento legal.
 * Separa la lógica de negocio de la interfaz de usuario por consola.
 */
public class ContratoService {
    private final GestorInventario gestor;

    public ContratoService(GestorInventario gestor) {
        this.gestor = gestor;
    }

    /**
     * Formaliza un nuevo contrato de alquiler, realizando las validaciones del dominio y legales correspondientes.
     * @return Mensaje con el resultado de la operación (Éxito o Error).
     */
    public String formalizarContrato(Vivienda vivienda, String dniInq, TipoArrendador tipo, double renta,
                                     int duracion, char serie, double fianza, double garantias) {
        Usuario inq = gestor.obtenerUsuario(dniInq);
        if (inq == null || !(inq instanceof Inquilino)) {
            return "ERROR DE INTEGRIDAD: El DNI no corresponde a ningún inquilino registrado.";
        }

        ContratoAlquiler contrato = new ContratoAlquiler(vivienda.getDireccion(), dniInq, tipo, renta, duracion, serie, fianza, garantias);

        if (!contrato.cumpleDuracionMinimaLegal()) {
            return "ERROR LEGAL: Duración insuficiente. La ley exige un mínimo de " +
                ((tipo == TipoArrendador.FISICO) ? "60" : "84") + " meses.";
        }

        if (!contrato.validarFianzaLegal()) {
            return "ERROR LEGAL: El importe de la fianza debe ser exactamente 1 mes de renta (" + renta + "€) y las garantías adicionales no pueden superar las 2 mensualidades (" + (2 * renta) + "€).";
        }

        String res = gestor.formalizarContrato(contrato, vivienda);
        if (res.startsWith("ÉXITO")) {
            gestor.guardarDatos();
        }
        return res;
    }

    /**
     * Registra el depósito de la fianza en el ICAVI.
     */
    public boolean registrarDepositoFianza(Vivienda v) {
        if (v == null || v.getContratoActivo() == null) return false;
        v.getContratoActivo().registrarDepositoFianza();
        gestor.guardarDatos();
        return true;
    }

    /**
     * Aplica la actualización anual del precio del alquiler basada en el IRAV a los contratos del propietario.
     */
    public int aplicarActualizacionIrav(String dniProp, double porcentaje, boolean forzar) {
        int actualizados = gestor.aplicarActualizacionAnualIRAV(dniProp, porcentaje, forzar);
        if (actualizados > 0) {
            gestor.guardarDatos();
        }
        return actualizados;
    }
}
