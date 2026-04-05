package com.habitly.model;

import java.io.Serializable;

/**
 * Representa una unidad inmobiliaria dentro del inventario.
 * Gestiona la lógica de precios, estado de ocupación y mantenimiento.
 * * @author DevNaranjo
 * @version 1.0.34
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

    /**
     * Constructor principal para el modelo de vivienda.
     * Inicializa el acumulador de pagos a cero.
     */
    public Vivienda(String direccion, double precioBase, double superficie, int habitaciones, int baños,
                    boolean tieneGaraje, boolean tienePiscina, boolean estaAmueblado, String conservacion) {
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

    // --- SETTERS ---
    public void setEstado(EstadoVivienda estado) { this.estado = estado; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public void setTienePiscina(boolean tienePiscina) { this.tienePiscina = tienePiscina; }
    public void setEstaAmueblado(boolean estaAmueblado) { this.estaAmueblado = estaAmueblado; }

    // --- MÉTODOS DE LÓGICA DE NEGOCIO ---

    /**
     * Registra un abono para la mensualidad actual.
     * Si el pago se completa, la vivienda cambia automáticamente a ALQUILADA.
     * @param cuota Cantidad a abonar.
     */
    public void registrarPago(double cuota) {
        if (this.estado == EstadoVivienda.VENDIDA) {
            System.out.println("[!] Error: Vivienda vendida. No se admiten pagos.");
            return;
        }

        if (cuota > 0) {
            this.totalPagadoMes += cuota;

            if (isPagadoCompleto() && this.estado == EstadoVivienda.DISPONIBLE) {
                this.estado = EstadoVivienda.ALQUILADA;
                System.out.println("[SISTEMA] Pago completado. Estado actualizado a ALQUILADA.");
            }
        }
    }

    /**
     * Calcula el precio con impuestos. Debe ser implementado por subclases (Piso/Casa).
     * @return Precio total con tasas.
     */
    public abstract double getPrecioFinalConImpuestos();

    /**
     * Calcula la deuda restante basándose en el precio con impuestos.
     * @return Diferencia entre el precio total y lo ya pagado.
     */
    public double getPendienteDePago() {
        double pendiente = getPrecioFinalConImpuestos() - totalPagadoMes;
        return (pendiente < 0) ? 0 : pendiente; // Evita devolver valores negativos
    }

    /**
     * Verifica si se ha cubierto el total del precio con impuestos.
     */
    public boolean isPagadoCompleto() {
        return totalPagadoMes >= getPrecioFinalConImpuestos();
    }

    /**
     * Actualiza el precio base aplicando el IPC.
     * @param porcentaje Porcentaje de subida (ej. 3.0 para 3%).
     */
    public void aplicarSubidaAnual(double porcentaje) {
        if (porcentaje != 0) {
            this.precioBase += (this.precioBase * porcentaje / 100);
        }
    }

    /**
     * Calcula la ratio de rentabilidad por superficie.
     * @return Valor del metro cuadrado.
     */
    public double getPrecioMetroCuadrado() {
        return (superficie > 0) ? (precioBase / superficie) : 0.0;
    }
}