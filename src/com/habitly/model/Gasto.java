package com.habitly.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un gasto o factura individual asociada a una propiedad.
 * Esta clase gestiona la integridad del monto y el estado de pago.
 * * @author DevNaranjo
 * @version 1.0.6
 * @since 1.0.4
 */
public class Gasto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idGasto;
    private String concepto;
    private double monto;
    private LocalDate fechaRegistro;
    private boolean pagado;

    /**
     * Constructor para crear un nuevo gasto pendiente.
     * @param idGasto Identificador único de la factura.
     * @param concepto Descripción del gasto (ej. Luz, Agua).
     * @param monto Valor económico del gasto (debe ser positivo).
     */
    public Gasto(String idGasto, String concepto, double monto) {
        this.idGasto = idGasto;
        this.concepto = concepto;
        this.monto = validarMonto(monto);
        this.fechaRegistro = LocalDate.now();
        this.pagado = false;
    }

    /**
     * Valida que el monto ingresado no sea negativo.
     * @param montoAComprobar El valor a validar.
     * @return El monto validado o 0.0 si es incorrecto.
     */
    private double validarMonto(double montoAComprobar) {
        return (montoAComprobar < 0) ? 0.0 : montoAComprobar;
    }

    /**
     * Cambia el estado del gasto a pagado.
     */
    public void marcarComoPagado() {
        this.pagado = true;
    }

    // --- Getters ---
    public String getIdGasto() { return idGasto; }

    public String getConcepto() { return concepto; }

    public double getMonto() { return monto; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }

    public boolean isPagado() { return pagado; }

    // --- Setters ---
    public void setIdGasto(String idGasto) { this.idGasto = idGasto; }

    public void setConcepto(String concepto) { this.concepto = concepto; }

    public void setMonto(double monto) { this.monto = validarMonto(monto); }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | %s | %.2fE | Estado: %s",
                fechaRegistro, idGasto, concepto, monto, pagado ? "PAGADO" : "PENDIENTE");
    }
}