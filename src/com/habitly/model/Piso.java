package com.habitly.model;

import java.io.Serializable;

/**
 * Especialización de vivienda para inmuebles en régimen de propiedad horizontal.
 * Añade atributos de ubicación vertical (planta y puerta).
 * * @author DevNaranjo
 * @version 1.0.6
 * @since 1.0.0
 */
public class Piso extends Vivienda implements Serializable {

    private static final long serialVersionUID = 1L;

    private int planta;
    private String puerta;

    /**
     * Constructor para la clase Piso.
     * * @param idPropietario DNI del dueño que registra el piso (v1.0.4).
     * @param direccion Ubicación física del edificio.
     * @param precioBase Importe del alquiler sin impuestos.
     * @param superficie Metros cuadrados útiles.
     * @param habitaciones Número de dormitorios.
     * @param baños Número de cuartos de baño.
     * @param tieneGaraje Indica si incluye plaza de garaje.
     * @param tienePiscina Indica si dispone de piscina comunitaria.
     * @param estaAmueblado Indica si el piso incluye mobiliario.
     * @param conservacion Descripción del estado físico.
     * @param planta Nivel en el que se encuentra el piso.
     * @param puerta Identificador específico (ej: "1ºA").
     */
    public Piso(String idPropietario, String direccion, double precioBase, double superficie, int habitaciones,
                int baños, boolean tieneGaraje, boolean tienePiscina, boolean estaAmueblado,
                String conservacion, int planta, String puerta) {

        // Pasamos el idPropietario a la superclase Vivienda
        super(idPropietario, direccion, precioBase, superficie, habitaciones, baños, tieneGaraje,
                tienePiscina, estaAmueblado, conservacion);

        this.planta = planta;
        this.puerta = puerta;
    }

    // --- LÓGICA DE NEGOCIO ---

    /**
     * Calcula el precio final de la mensualidad del piso.
     * Según la legislación de Canarias para 2026, el arrendamiento de vivienda habitual 
     * está exento de IGIC, por lo que se devuelve el precio base sin impuestos adicionales.
     * @return double con el precio final (Base, exento de IGIC).
     */
    @Override
    public double getPrecioFinalConImpuestos() {
        return Math.round(getPrecioBase() * 100.0) / 100.0;
    }

    // --- GETTERS ---
    public String getPuerta() { return puerta; }
    public int getPlanta() { return planta; }

    // --- SETTERS ---
    public void setPuerta(String nuevaPuerta) { this.puerta = nuevaPuerta; }
    public void setPlanta(int nuevaPlanta) { this.planta = nuevaPlanta; }
}