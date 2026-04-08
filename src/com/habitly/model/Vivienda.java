package com.habitly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una unidad inmobiliaria dentro del inventario.
 * Gestiona la lógica de precios, estado de ocupación, mantenimiento
 * y cumplimiento de la Ley de Vivienda (IRAV).
 * * @author DevNaranjo
 * @version 1.0.5
 * @since 1.0.0
 */
public abstract class Vivienda implements Serializable {

    private static final long serialVersionUID = 1L;

    private String direccion;
    private double precioBase;
    protected double totalPagadoMes;
    private EstadoVivienda estado = EstadoVivienda.DISPONIBLE;
    private double superficie;
    private int habitaciones;
    private int baños;
    private boolean tieneGaraje;
    private String conservacion;
    private boolean tienePiscina;
    private boolean estaAmueblado;

    // Atributos v1.0.4
    private String idPropietario;
    private Usuario inquilino;
    private ArrayList<Gasto> historialGastos;

    // Atributos v1.0.50 - Etapa 5 (Compliance)
    private double limiteMaximoIrav; // Límite legal según el índice de referencia
    private ContratoAlquiler contratoActivo;

    public Vivienda(String idPropietario, String direccion, double precioBase, double superficie, int habitaciones, int baños,
                    boolean tieneGaraje, boolean tienePiscina, boolean estaAmueblado, String conservacion) {
        this.idPropietario = idPropietario;
        this.direccion = direccion;
        this.precioBase = precioBase;
        this.superficie = superficie;
        this.habitaciones = habitaciones;
        this.baños = baños;
        this.tieneGaraje = tieneGaraje;
        this.tienePiscina = tienePiscina;
        this.estaAmueblado = estaAmueblado;
        this.conservacion = conservacion;
        this.totalPagadoMes = 0.0;
        this.historialGastos = new ArrayList<>();
        this.inquilino = null;
        this.limiteMaximoIrav = 0.0; // Se debe setear tras el análisis legal
    }

    // --- GETTERS ---
    public String getDireccion() { return direccion; }
    public double getPrecioBase() { return precioBase; }
    public EstadoVivienda getEstado() { return estado; }
    public double getSuperficie() { return superficie; }
    public int getHabitaciones() { return habitaciones; }
    public int getBaños() { return baños; }
    public boolean isTieneGaraje() { return tieneGaraje; }
    public boolean isTienePiscina() { return tienePiscina; }
    public String getConservacion() { return conservacion; }
    public double getTotalPagadoMes() { return totalPagadoMes; }
    public boolean isEstaAmueblado() { return estaAmueblado; }
    public String getIdPropietario() { return idPropietario; }
    public Usuario getInquilino() { return inquilino; }
    public ArrayList<Gasto> getHistorialGastos() { return historialGastos; }

    /** @return El límite de renta mensual permitido por el índice IRAV. */
    public double getLimiteMaximoIrav() { return limiteMaximoIrav; }

    /** @return El contrato de alquiler actualmente vinculado a la vivienda. */
    public ContratoAlquiler getContratoActivo() { return contratoActivo; }

    /**
     * Calcula el precio por metro cuadrado
     * @return double con el ratio euros/m2.
     */
    public double getPrecioMetroCuadrado() {
        return (superficie > 0) ? precioBase / superficie : 0;
    }

    // --- SETTERS ---
    public void setEstado(EstadoVivienda estado) { this.estado = estado; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public void setTienePiscina(boolean tienePiscina) { this.tienePiscina = tienePiscina; }
    public void setEstaAmueblado(boolean estaAmueblado) { this.estaAmueblado = estaAmueblado; }
    public void setInquilino(Usuario inquilino) { this.inquilino = inquilino; }

    /** Define el límite legal de renta tras consultar el sistema IRAV oficial. */
    public void setLimiteMaximoIrav(double limiteMaximoIrav) { this.limiteMaximoIrav = limiteMaximoIrav; }

    /** Vincula un contrato legal a la unidad habitacional. */
    public void setContratoActivo(ContratoAlquiler contratoActivo) { this.contratoActivo = contratoActivo; }

    // --- LÓGICA DE NEGOCIO ---

    public void registrarFactura(Gasto g) {
        if (g != null) this.historialGastos.add(g);
    }

    public void registrarPago(double cuota) {
        if (cuota > 0) {
            this.totalPagadoMes += cuota;
            // Si el pago es completo, la vivienda pasa a estar alquilada
            if (isPagadoComplete() && this.estado == EstadoVivienda.DISPONIBLE) {
                this.estado = EstadoVivienda.ALQUILADA;
            }
        }
    }

    public void aplicarSubidaAnual(double porcentaje) {
        if (porcentaje != 0) {
            this.precioBase += (this.precioBase * porcentaje / 100);
        }
    }

    public double calcularBalanceTotalPendiente() {
        double totalGastos = 0;
        for (Gasto g : historialGastos) {
            if (!g.isPagado()) totalGastos += g.getMonto();
        }
        return getPrecioFinalConImpuestos() + totalGastos;
    }

    public List<Gasto> getListaGastosPendientes() {
        List<Gasto> pendientes = new ArrayList<>();
        for (Gasto g : historialGastos) {
            if (!g.isPagado()) pendientes.add(g);
        }
        return pendientes;
    }

    public abstract double getPrecioFinalConImpuestos();

    public double getPendienteDePago() {
        double pendiente = getPrecioFinalConImpuestos() - totalPagadoMes;
        return (pendiente < 0) ? 0 : pendiente;
    }

    public boolean isPagadoComplete() {
        return totalPagadoMes >= getPrecioFinalConImpuestos();
    }
}