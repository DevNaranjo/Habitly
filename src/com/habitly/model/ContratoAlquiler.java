package com.habitly.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un contrato de arrendamiento bajo la Ley 12/2023.
 * Gestiona duraciones legales, rentas y estados de fianza (ICAVI).
 * * @author DevNaranjo
 * @version 1.0.6
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

    // --- Nuevos campos legales (LAU + Ley 12/2023) ---
    private int duracionMinLegal;       // Calculado según tipoArrendador (60 o 84 meses)
    private LocalDate fechaVencimiento; // fechaFirma + duracionMeses
    private boolean enProrrogaTacita;   // ¿Está en los 3 años de prórroga tácita?
    private int aniosProrrogaConsumidos; // 0-3

    // --- Fianza detallada ---
    private double importeFianza;        // 1 mes de renta (Art. 36 LAU)
    private double garantiasAdicionales; // Máx 2 meses de renta
    private LocalDate fechaDepositoIcavi; // Fecha de depósito en ICAVI (null si pendiente)
    private LocalDate plazoLimiteDeposito; // fechaFirma + 1 mes

    // --- IRAV ---
    private double ultimoIravAplicado;   // Último % IRAV usado para actualización

    // --- Desistimiento ---
    private boolean desistimientoPermitido; // true si han pasado >=6 meses desde firma

    // --- Estado ---
    private EstadoContrato estado;

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

        // Defaults para cumplimiento legal
        this.importeFianza = renta; // 1 mes por defecto
        this.garantiasAdicionales = 0.0;
        this.duracionMinLegal = (tipo == TipoArrendador.FISICO) ? 60 : 84;
        this.fechaVencimiento = this.fechaFirma.plusMonths(meses);
        this.plazoLimiteDeposito = this.fechaFirma.plusMonths(1);
        this.enProrrogaTacita = false;
        this.aniosProrrogaConsumidos = 0;
        this.fechaDepositoIcavi = null;
        this.ultimoIravAplicado = 0.0;
        this.desistimientoPermitido = false;
        this.estado = EstadoContrato.VIGENTE;
    }

    public ContratoAlquiler(String direccionVivienda, String dniInquilino, TipoArrendador tipo,
                            double renta, int meses, char serie, double fianza, double garantias) {
        this(direccionVivienda, dniInquilino, tipo, renta, meses, serie);
        this.importeFianza = fianza;
        this.garantiasAdicionales = garantias;
        this.fechaVencimiento = this.fechaFirma.plusMonths(meses);
        this.plazoLimiteDeposito = this.fechaFirma.plusMonths(1);
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
        this.fechaDepositoIcavi = LocalDate.now();
    }

    public LocalDate calcularFechaVencimiento() {
        return this.fechaFirma.plusMonths(this.duracionMeses);
    }

    public boolean estaVigente() {
        return this.estado == EstadoContrato.VIGENTE || this.estado == EstadoContrato.EN_PRORROGA_TACITA;
    }

    public boolean estaEnPeriodoDesistimiento() {
        LocalDate hoy = LocalDate.now();
        return this.desistimientoPermitido || hoy.isAfter(this.fechaFirma.plusMonths(6)) || hoy.isEqual(this.fechaFirma.plusMonths(6));
    }

    public int calcularPlazoPreaviso(boolean esArrendador) {
        return esArrendador ? 4 : 2;
    }

    public boolean aplicarActualizacionIRAV(double porcentajeIrav) {
        return aplicarActualizacionIRAV(porcentajeIrav, false);
    }

    public boolean aplicarActualizacionIRAV(double porcentajeIrav, boolean forzar) {
        LocalDate hoy = LocalDate.now();
        boolean esAniversario = forzar || (hoy.getMonth() == fechaFirma.getMonth() && 
                                           hoy.getDayOfMonth() == fechaFirma.getDayOfMonth() && 
                                           hoy.isAfter(fechaFirma));
        if (esAniversario) {
            this.rentaMensual = Math.round((this.rentaMensual * (1 + porcentajeIrav / 100.0)) * 100.0) / 100.0;
            this.ultimoIravAplicado = porcentajeIrav;
            return true;
        }
        return false;
    }

    public boolean validarFianzaLegal() {
        boolean fianzaCorrecta = Math.abs(this.importeFianza - this.rentaMensual) < 0.01;
        boolean garantiasCorrectas = this.garantiasAdicionales <= (2 * this.rentaMensual) + 0.01;
        return fianzaCorrecta && garantiasCorrectas;
    }

    public boolean estaPendienteDepositoIcavi() {
        return this.fechaDepositoIcavi == null && LocalDate.now().isAfter(this.plazoLimiteDeposito);
    }

    public boolean activarProrrogaTacita() {
        if (estado == EstadoContrato.VIGENTE || estado == EstadoContrato.EN_PRORROGA_TACITA) {
            if (aniosProrrogaConsumidos < 3) {
                this.estado = EstadoContrato.EN_PRORROGA_TACITA;
                this.enProrrogaTacita = true;
                this.aniosProrrogaConsumidos++;
                this.fechaVencimiento = this.fechaVencimiento.plusYears(1);
                return true;
            } else {
                this.estado = EstadoContrato.FINALIZADO;
                this.enProrrogaTacita = false;
            }
        }
        return false;
    }

    // --- GETTERS Y SETTERS ---

    public String getIdContrato() { return idContrato; }
    public String getDireccionVivienda() { return direccionVivienda; }
    public String getDniInquilino() { return dniInquilino; }
    public TipoArrendador getTipoArrendador() { return tipoArrendador; }
    public double getRentaMensual() { return rentaMensual; }
    public void setRentaMensual(double rentaMensual) { this.rentaMensual = rentaMensual; }
    public int getDuracionMeses() { return duracionMeses; }
    public LocalDate getFechaFirma() { return fechaFirma; }
    public void setFechaFirma(LocalDate fechaFirma) { 
        this.fechaFirma = fechaFirma; 
        this.fechaVencimiento = fechaFirma.plusMonths(duracionMeses);
        this.plazoLimiteDeposito = fechaFirma.plusMonths(1);
    }
    public boolean isFianzaDepositada() { return fianzaDepositada; }
    public boolean isTieneInventario() { return tieneInventario; }
    public void setTieneInventario(boolean tieneInventario) { this.tieneInventario = tieneInventario; }

    public int getDuracionMinLegal() { return duracionMinLegal; }
    public void setDuracionMinLegal(int duracionMinLegal) { this.duracionMinLegal = duracionMinLegal; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public boolean isEnProrrogaTacita() { return enProrrogaTacita; }
    public void setEnProrrogaTacita(boolean enProrrogaTacita) { this.enProrrogaTacita = enProrrogaTacita; }

    public int getAniosProrrogaConsumidos() { return aniosProrrogaConsumidos; }
    public void setAniosProrrogaConsumidos(int aniosProrrogaConsumidos) { this.aniosProrrogaConsumidos = aniosProrrogaConsumidos; }

    public double getImporteFianza() { return importeFianza; }
    public void setImporteFianza(double importeFianza) { this.importeFianza = importeFianza; }

    public double getGarantiasAdicionales() { return garantiasAdicionales; }
    public void setGarantiasAdicionales(double garantiasAdicionales) { this.garantiasAdicionales = garantiasAdicionales; }

    public LocalDate getFechaDepositoIcavi() { return fechaDepositoIcavi; }
    public void setFechaDepositoIcavi(LocalDate fechaDepositoIcavi) { 
        this.fechaDepositoIcavi = fechaDepositoIcavi; 
        if (fechaDepositoIcavi != null) {
            this.fianzaDepositada = true;
        }
    }

    public LocalDate getPlazoLimiteDeposito() { return plazoLimiteDeposito; }
    public void setPlazoLimiteDeposito(LocalDate plazoLimiteDeposito) { this.plazoLimiteDeposito = plazoLimiteDeposito; }

    public double getUltimoIravAplicado() { return ultimoIravAplicado; }
    public void setUltimoIravAplicado(double ultimoIravAplicado) { this.ultimoIravAplicado = ultimoIravAplicado; }

    public boolean isDesistimientoPermitido() { return estaEnPeriodoDesistimiento(); }
    public void setDesistimientoPermitido(boolean desistimientoPermitido) { this.desistimientoPermitido = desistimientoPermitido; }

    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("Contrato %s | Inquilino: %s | Renta: %.2fE | Estado: %s | Fianza: %s",
                idContrato, dniInquilino, rentaMensual, estado, fianzaDepositada ? "OK" : "PENDIENTE");
    }
}