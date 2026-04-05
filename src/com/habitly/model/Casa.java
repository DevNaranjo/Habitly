package com.habitly.model;

import java.io.Serializable;

/**
 * Especialización de vivienda para inmuebles independientes.
 * Gestiona atributos de superficie de terreno y áreas exteriores privadas.
 * * @author DevNaranjo
 * @version 1.0.34
 * @since 1.0.0
 */

public class Casa extends Vivienda implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributo específico: superficie de terreno privado (parcela)
    private double metrosParcela;

    /**
     * Constructor para la clase Casa.
     * Delega la configuración de datos base y técnicos a la superclase.
     * * @param direccion Ubicación física de la propiedad.
     * @param precioBase Importe del alquiler sin impuestos.
     * @param superficie Metros cuadrados construidos.
     * @param habitaciones Número de dormitorios.
     * @param baños Número de cuartos de baño.
     * @param tieneGaraje Indica si incluye zona de aparcamiento.
     * @param tienePiscina Indica si dispone de piscina privada.
     * @param estaAmueblado Indica si la casa incluye mobiliario.
     * @param conservacion Descripción del estado físico.
     * @param metrosParcela Superficie total del terreno/parcela en m².
     */
    public Casa(String direccion, double precioBase, double superficie, int habitaciones,
                int baños, boolean tieneGaraje, boolean tienePiscina, boolean estaAmueblado,
                String conservacion, double metrosParcela) {

        super(direccion, precioBase, superficie, habitaciones, baños, tieneGaraje,
                tienePiscina, estaAmueblado, conservacion);

        this.metrosParcela = metrosParcela;
    }

    // --- LÓGICA DE NEGOCIO ---

    /**
     * Calcula el precio final de la mensualidad de la casa aplicando el IGIC (7%).
     * Se aplica un redondeo a dos decimales para garantizar la precisión en los tests.
     * @return double con el precio final (Base + 7% IGIC).
     */
    @Override
    public double getPrecioFinalConImpuestos() {
        return Math.round((getPrecioBase() * 1.07) * 100.0) / 100.0;
    }

    // --- GETTERS ---

    /**
     * Obtiene la superficie de la parcela privada.
     * @return double con los metros cuadrados del terreno.
     */
    public double getMetrosParcela() {
        return metrosParcela;
    }

    // --- SETTERS ---

    /**
     * Actualiza la extensión de la parcela registrada.
     * @param nuevosMetrosParcela Nuevo valor para la superficie del terreno.
     */
    public void setMetrosParcela(double nuevosMetrosParcela) {
        this.metrosParcela = nuevosMetrosParcela;
    }
}