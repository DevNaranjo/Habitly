package com.habitly.model;

/**
 * Representa una vivienda de tipo Piso dentro de un régimen de propiedad horizontal.
 * Hereda de la clase Vivienda los atributos de ubicación y precio.
 *
 * @author Iriome
 * @version 1.0 (2026)
 */
public class Piso extends Vivienda
{
    // Atributos específicos del tipo Piso
    private int planta;
    private String puerta;

    /**
     * Constructor para la clase Piso.
     * Utiliza el mecanismo de herencia para inicializar los datos base.
     *
     * @param direccion Ubicación física del edificio.
     * @param precioBase Importe del alquiler sin impuestos.
     * @param planta Nivel en la que se encuentra el piso.
     * @param puerta Identificador específico (ej: "A", "Izquierda", "1").
     */
    public Piso(String direccion, double precioBase, int planta, String puerta)
    {
        // Invocación al constructor de la superclase (Vivienda)
        super(direccion, precioBase);
        this.planta = planta;
        this.puerta = puerta;
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
        this.planta = nuevaPlanta;
    }
}
