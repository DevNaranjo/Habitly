package com.habitly.model;

import java.io.Serializable;

/**
 * Representa una vivienda de tipo Piso dentro de un régimen de propiedad horizontal.
 * Hereda de la clase Vivienda los atributos de ubicación, precio y características físicas.
 *
 * @author Iriome
 * @version 1.0.2
 * @since 03-04-26
 */
public class Piso extends Vivienda implements Serializable
{
    // Atributos específicos del tipo Piso
    private int planta;
    private String puerta;

    /**
     * Constructor para la clase Piso.
     * Utiliza el mecanismo de herencia para inicializar los datos base y técnicos.
     *
     * @param direccion Ubicación física del edificio.
     * @param precioBase Importe del alquiler sin impuestos.
     * @param superficie Metros cuadrados útiles.
     * @param habitaciones Número de dormitorios.
     * @param baños Número de cuartos de baño.
     * @param tieneGaraje Indica si incluye plaza de garaje.
     * @param conservacion Descripción del estado físico (ej: "Reformado").
     * @param planta Nivel en la que se encuentra el piso.
     * @param puerta Identificador específico (ej: "A", "Izquierda", "1").
     */
    public Piso(String direccion, double precioBase, double superficie, int habitaciones,
                int baños, boolean tieneGaraje, String conservacion, int planta, String puerta)
    {
        // Invocación al constructor actualizado de la superclase (Vivienda)
        super(direccion, precioBase, superficie, habitaciones, baños, tieneGaraje, conservacion);
        this.planta = planta;
        this.puerta = puerta;
    }

    // --- Lógica de Negocio (obligatorio) ---

    /**
     * Calcula el precio final de la mensualidad del piso aplicando el IGIC.
     * @return double con el precio final (Base + 7% IGIC).
     */
    @Override
    public double getPrecioFinalConImpuestos() {
        // Aplicamos el 7% de IGIC sobre el precio base.
        return getPrecioBase() * 1.07;
    }

    /**
     * Obtiene el identificador de la puerta del piso.
     *
     * @return String con la puerta.
     */
    public String getPuerta()
    {
        return puerta;
    }

    /**
     * Obtiene el número de planta.
     *
     * @return int con el nivel del piso.
     */
    public int getPlanta()
    {
        return planta;
    }

    /**
     * Actualiza la identificación de la puerta.
     *
     * @param nuevaPuerta Nueva cadena para identificar la puerta.
     */
    public void setPuerta(String nuevaPuerta)
    {
        this.puerta = nuevaPuerta;
    }

    /**
     * Actualiza el número de planta del inmueble.
     *
     * @param nuevaPlanta Nuevo valor entero para la planta.
     */
    public void setPlanta(int nuevaPlanta)
    {
        // El error estaba aquí: debe ser this.planta
        this.planta = nuevaPlanta;
    }
}