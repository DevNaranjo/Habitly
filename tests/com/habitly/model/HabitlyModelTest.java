package com.habitly.model;

import java.util.ArrayList;

/**
 * Test Integral de Modelos Habitly v1.0.4
 * Verifica la jerarquía de Vivienda, Piso y Casa con sus nuevos constructores.
 */
public class HabitlyModelTest {
    public static void main(String[] args) {
        System.out.println("=== UNIT TEST: MODELOS INMOBILIARIOS v1.0.4 ===");

        // 1. Configuración de datos de prueba
        String dniProp = "12345678A";

        // 2. Probar Constructor de PISO (12 parámetros)
        // Orden: idProp, dir, precio, m2, hab, baños, garaje, piscina, amueblado, cons, planta, puerta
        Piso miPiso = new Piso(dniProp, "Calle Mayor 5", 800.0, 75.0, 3, 1,
                false, false, true, "Bueno", 2, "B");

        // 3. Probar Constructor de CASA (11 parámetros)
        // Orden: idProp, dir, precio, m2, hab, baños, garaje, piscina, amueblado, cons, parcela
        Casa miCasa = new Casa(dniProp, "Urb. El Pinar 12", 1200.0, 150.0, 4, 2,
                true, true, false, "Excelente", 500.0);

        System.out.println("\n--- VERIFICACIONES DE INTEGRIDAD ---");

        // PRUEBA A: Atributos Heredados (Vivienda)
        if (miPiso.getIdPropietario().equals(dniProp) && miCasa.getPrecioBase() == 1200.0) {
            System.out.println("✅ [OK] Herencia de Vivienda: IDs y Precios base correctos.");
        } else {
            System.out.println("❌ [ERROR] Fallo en la herencia de atributos base.");
        }

        // PRUEBA B: Cálculos Económicos (IGIC 7%)
        // 800 * 1.07 = 856.0
        double precioEsperadoPiso = 856.0;
        if (Math.abs(miPiso.getPrecioFinalConImpuestos() - precioEsperadoPiso) < 0.01) {
            System.out.println("✅ [OK] Cálculo IGIC (7%) en Piso correcto: " + miPiso.getPrecioFinalConImpuestos() + "€");
        } else {
            System.out.println("❌ [ERROR] Fallo en cálculo de impuestos. Obtenido: " + miPiso.getPrecioFinalConImpuestos());
        }

        // PRUEBA C: Atributos Específicos de Subclase
        boolean checkPiso = (miPiso.getPlanta() == 2 && miPiso.getPuerta().equals("B"));
        boolean checkCasa = (miCasa.getMetrosParcela() == 500.0);

        if (checkPiso && checkCasa) {
            System.out.println("✅ [OK] Atributos específicos (Planta/Puerta/Parcela) verificados.");
        } else {
            System.out.println("❌ [ERROR] Los datos específicos de Piso o Casa no coinciden.");
        }

        // PRUEBA D: Gestión de Gastos Inicial
        if (miPiso.getHistorialGastos() != null && miPiso.getHistorialGastos().isEmpty()) {
            System.out.println("✅ [OK] Lista de gastos inicializada correctamente.");
        } else {
            System.out.println("❌ [ERROR] La lista de gastos no se inicializó en el constructor.");
        }

        System.out.println("\n--- RESUMEN DE OBJETOS CREADOS ---");
        System.out.println("Piso: " + miPiso.getDireccion() + " | " + miPiso.getPrecioFinalConImpuestos() + "€ (IGIC inc.)");
        System.out.println("Casa: " + miCasa.getDireccion() + " | Parcela: " + miCasa.getMetrosParcela() + "m2");
        System.out.println("=============================================");
    }
}