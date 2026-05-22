package com.habitly.model;

/**
 * Representa los posibles estados legales de un contrato de alquiler
 * según la Ley de Arrendamientos Urbanos (LAU).
 * 
 * @author DevNaranjo
 * @version 1.0.6
 * @since 1.0.6
 */
public enum EstadoContrato {
    VIGENTE,              // Dentro del período obligatorio (5/7 años)
    EN_PRORROGA_TACITA,   // En los 3 años de prórroga automática (Art. 10 LAU)
    FINALIZADO,           // Contrato terminado normalmente
    DESISTIDO,            // Inquilino desistió tras al menos 6 meses (Art. 11 LAU)
    RESUELTO              // Rescisión por incumplimiento
}
