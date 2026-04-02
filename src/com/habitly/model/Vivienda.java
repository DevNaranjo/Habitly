package com.habitly.model;

import java.io.Serializable;

/**
 * Clase abstracta que representa la base de cualquier vivienda en Habitly.
 * Implementa Serializable para permitir la persistencia de datos.
 */
public abstract class Vivienda implements Serializable {
    private String direccion;
    private double precioBase;
    protected double totalPagadoMes;
    private EstadoVivienda estado;

    /**
     * Constructor para inicializar una vivienda con dirección y precio.
     * Por defecto, la vivienda nace en estado DISPONIBLE.
     */
    public Vivienda(String direccion, double precioBase) {
        this.direccion = direccion;
        this.precioBase = precioBase;
        this.totalPagadoMes = 0;
        this.estado = EstadoVivienda.DISPONIBLE;
    }

    // --- GETTERS Y SETTERS ---
    public String getDireccion() { return direccion; }

    public double getPrecioBase() { return precioBase; }

    public EstadoVivienda getEstado() { return estado; }

    public void setEstado(EstadoVivienda estado) { this.estado = estado; }

    /**
     * Registra un ingreso de dinero para la mensualidad actual.
     * Incrementa el acumulador de pagos realizados por el inquilino.
     * @param cuota La cantidad de dinero (en euros) que se desea abonar.
     */
    public void registrarPago(double cuota) {
        // Comprobar que la vivienda no esté vendida
        if (this.estado == EstadoVivienda.VENDIDA) {
            System.out.println("La vivienda está vendida. No se pueden registrar más pagos.");
            return;
        }
        //Si no está vendida realiza el pago
        this.totalPagadoMes += cuota;

        // Si se completa el pago, pasa a ser alquilada (temporal)
        if (isPagadoCompleto() && this.estado == EstadoVivienda.DISPONIBLE) {
            this.estado = EstadoVivienda.ALQUILADA;
            System.out.println("SISTEMA: La vivienda ha pasado automáticamente a estado ALQUILADA.");
        }
    }

    /**
     * Calcula el precio final aplicando los impuestos correspondientes (IGIC).
     * @return El precio total con impuestos incluidos.
     */
    public abstract double getPrecioFinalConImpuestos();

    /**
     * Calcula la cantidad que aún queda por pagar para cubrir la mensualidad.
     * @return Cantidad pendiente en euros.
     */
    public double getPendienteDePago() {
        return getPrecioFinalConImpuestos() - totalPagadoMes;
    }

    /**
     * Verifica si el total pagado cubre el precio con impuestos.
     * @return true si ya no queda deuda.
     */
    public boolean isPagadoCompleto() {
        return totalPagadoMes >= getPrecioFinalConImpuestos();
    }

    /**
     * Aplica un incremento porcentual al precio base (IPC).
     * @param porcentaje Valor del incremento (ej: 2.5 para un 2.5%).
     */
    public void aplicarSubidaAnual(double porcentaje) {
        this.precioBase += (this.precioBase * porcentaje / 100);
    }
}