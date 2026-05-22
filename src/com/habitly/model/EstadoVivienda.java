package com.habitly.model;

/**
    * Definición de los estados legales y operativos de un inmueble.
    * Controla la disponibilidad de las viviendas para procesos de reserva y alquiler.
    * @author DevNaranjo
    * @version 1.0.6
    * @since 1.0.0
 */
public enum EstadoVivienda {
    DISPONIBLE,
    RESERVADA,
    ALQUILADA,
    VENDIDA
}