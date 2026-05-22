package com.habitly.test;

import com.habitly.model.ContratoAlquiler;
import com.habitly.model.TipoArrendador;

/**
 * Test unitario para validar la lógica interna de la clase ContratoAlquiler.
 * Verifica las reglas de la LAU y el control de fianzas.
 * @author DevNaranjo
 * @version 1.0.6
 */
public class ContratoAlquilerTest {

    public static void main(String[] args) {
        System.out.println("=== UNIT TEST: CONTRATO ALQUILER v1.0.6 ===");

        // 1. Probar contrato para Persona Física (Mínimo 5 años / 60 meses)
        // Parámetros: Dirección, DNI Inquilino, Tipo, Renta, Meses, Serie
        ContratoAlquiler contratoFisico = new ContratoAlquiler(
                "Calle Triana 10", "12345678X", TipoArrendador.FISICO, 800.0, 60, 'A'
        );

        // 2. Probar contrato para Persona Jurídica (Mínimo 7 años / 84 meses)
        ContratoAlquiler contratoEmpresa = new ContratoAlquiler(
                "Av. Mesa y López", "B99887766", TipoArrendador.JURIDICO, 1200.0, 60, 'B'
        );

        // --- PRUEBA 1: Validación de Duración Legal ---
        if (contratoFisico.cumpleDuracionMinimaLegal()) {
            System.out.println("✅ OK: Duración 5 años para Persona Física aceptada.");
        } else {
            System.out.println("❌ ERROR: Falló validación 5 años Física.");
        }

        if (!contratoEmpresa.cumpleDuracionMinimaLegal()) {
            System.out.println("✅ OK: Bloqueada duración 5 años para Persona Jurídica (Debe ser 7).");
        } else {
            System.out.println("❌ ERROR: Se permitió contrato corto a una empresa.");
        }

        // --- PRUEBA 2: Gestión de Fianza ---
        if (!contratoFisico.isFianzaDepositada()) {
            System.out.println("✅ OK: Estado inicial de fianza es PENDIENTE.");
        }

        contratoFisico.registrarDepositoFianza();
        if (contratoFisico.isFianzaDepositada()) {
            System.out.println("✅ OK: Registro de fianza en ICAVI verificado.");
        } else {
            System.out.println("❌ ERROR: No se pudo marcar la fianza como depositada.");
        }

        // --- PRUEBA 3: Identificadores ---
        if (contratoFisico.getIdContrato().startsWith("CON-A-")) {
            System.out.println("✅ OK: Formato de ID de contrato correcto.");
        }

        System.out.println("\nResumen Contrato: " + contratoFisico.toString());
        System.out.println("===========================================");
    }
}