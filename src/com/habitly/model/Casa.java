package com.habitly.model;

import java.io.Serializable;

/**
 * Representa una vivienda de tipo Casa o Unifamiliar.
 * Extiende la funcionalidad de Vivienda añadiendo información sobre el terreno y características físicas.
 * @author DevNaranjo
 * @version 1.0.21
 * @since 03-04-26
 */
public class Casa extends Vivienda implements Serializable
{
    // Atributo específico: superficie de terreno privado
    private double metrosParcela;

    /**
     * Constructor para la clase Casa.
     * Delega la configuración de datos base y técnicos a la superclase.
     * @param direccion Ubicación de la casa.
     * @param precioBase Mensualidad neta acordada.
     * @param superficie Metros cuadrados construidos.
     * @param habitaciones Número de dormitorios.
     * @param baños Número de cuartos de baño.
     * @param tieneGaraje Indica si incluye zona de aparcamiento.
     * @param tienePiscina Indica si dispone de piscina privada.
     * @param estaAmueblado Indica si la casa se entrega con mobiliario.
     * @param conservacion Descripción del estado físico (ej: "A reformar").
     * @param metrosParcela Superficie total del terreno en m².
     */
    public Casa(String direccion, double precioBase, double superficie, int habitaciones,
                int baños, boolean tieneGaraje, boolean tienePiscina, boolean estaAmueblado,
                String conservacion, double metrosParcela)
    {
        // Invocación al constructor actualizado de Vivienda (v1.0.21) con los 9 parámetros
        super(direccion, precioBase, superficie, habitaciones, baños, tieneGaraje, tienePiscina, estaAmueblado, conservacion);
        this.metrosParcela = metrosParcela;
    }

    // --- Lógica de Negocio ---

    /**
     * Calcula el precio final de la mensualidad de la casa aplicando el IGIC.
     * @return double con el precio final (Base + 7% IGIC).
     */
    @Override
    public double getPrecioFinalConImpuestos() {
        // Aplicamos el 7% de IGIC sobre el precio base.
        return getPrecioBase() * 1.07;
    }

    // --- Getters ---

    /**
     * Obtiene la superficie de la parcela.
     * @return double con los metros cuadrados del terreno.
     */
    public double getMetrosParcela()
    {
        return metrosParcela;
    }

    // --- Setters ---

    /**
     * Permite modificar la extensión de la parcela registrada.
     * @param nuevosMetrosParcela Valor decimal con la nueva superficie.
     */
    public void setMetrosParcela(double nuevosMetrosParcela)
    {
        this.metrosParcela = nuevosMetrosParcela;
    }
}