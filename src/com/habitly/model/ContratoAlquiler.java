package com.habitly.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un contrato de arrendamiento bajo la Ley 12/2023.
 * Gestiona duraciones legales, rentas y estados de fianza (ICAVI).
 * * @author DevNaranjo
 * @version 1.0.5
 */
public class ContratoAlquiler implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idContrato;
    private String direccionVivienda;
    private String dniInquilino;
    private TipoArrendador tipoArrendador;
    private double rentaMensual;
    private int duracionMeses;
    private LocalDate fechaFirma;
    private boolean fianzaDepositada; // Control para ICAVI
    private boolean tieneInventario;

    public ContratoAlquiler(String direccionVivienda, String dniInquilino, TipoArrendador tipo,
                            double renta, int meses, char serie) {
        this.idContrato = "CON-" + serie + "-" + System.currentTimeMillis() % 10000;
        this.direccionVivienda = direccionVivienda;
        this.dniInquilino = dniInquilino;
        this.tipoArrendador = tipo;
        this.rentaMensual = renta;
        this.duracionMeses = meses;
        this.fechaFirma = LocalDate.now();
        this.fianzaDepositada = false;
        this.tieneInventario = true; // Por defecto se asume inventario digital
    }

    // --- LÓGICA DE CUMPLIMIENTO LEGAL ---

    /**
     * Valida si el contrato cumple con la prórroga obligatoria de la LAU.
     * Personas físicas: 5 años (60 meses).
     * Personas jurídicas: 7 años (84 meses).
     * @return true si la duración pactada es legal.
     */
    public boolean cumpleDuracionMinimaLegal() {
        int minimoRequerido = (tipoArrendador == TipoArrendador.FISICO) ? 60 : 84;
        return this.duracionMeses >= minimoRequerido;
    }

    /**
     * Registra el depósito de la fianza en el organismo oficial (ICAVI).
     */
    public void registrarDepositoFianza() {
        this.fianzaDepositada = true;
    }

    // --- GETTERS Y SETTERS ---

    public String getIdContrato() { return idContrato; }
    public String getDniInquilino() { return dniInquilino; }
    public double getRentaMensual() { return rentaMensual; }
    public boolean isFianzaDepositada() { return fianzaDepositada; }
    public boolean isTieneInventario() { return tieneInventario; }
    public void setTieneInventario(boolean tieneInventario) { this.tieneInventario = tieneInventario; }

    @Override
    public String toString() {
        return String.format("Contrato %s | Inquilino: %s | Renta: %.2f€ | Fianza: %s",
                idContrato, dniInquilino, rentaMensual, fianzaDepositada ? "OK" : "PENDIENTE");
    }
}