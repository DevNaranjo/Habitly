package com.habitly.model;

import com.habitly.model.*;

/**
 * Prueba la lógica interna de los modelos que usa Habitly (v1.0.33).
 */
public class HabitlyModelTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTS DE MODELO ===");
        testCalculoPrecioM2();
        testDeudaPendiente();
        System.out.println("=== TESTS DE MODELO FINALIZADOS ===\n");
    }

    private static void testCalculoPrecioM2() {
        // Probamos un Piso: 1000€ / 50m2 = 20€/m2
        Piso p = new Piso("Calle Falsa 123", 1000, 50, 3, 2, true, false, true, "Nuevo", 1, "A");
        if (p.getPrecioMetroCuadrado() == 20.0) {
            System.out.println("[OK] Cálculo EUR/M2 correcto.");
        } else {
            System.out.println("[ERROR] El cálculo EUR/M2 falló: " + p.getPrecioMetroCuadrado());
        }
    }

    private static void testDeudaPendiente() {
        // Casa de 1000€ -> Con IGIC son 1070€
        Casa c = new Casa("Test", 1000, 100, 3, 2, true, true, false, "Nuevo", 500);
        c.registrarPago(300);

        // 1070 - 300 = 770
        if (c.getPendienteDePago() == 770.0) {
            System.out.println("[OK] Registro de pago y deuda con IGIC correcto.");
        } else {
            System.out.println("[ERROR] La deuda no cuadra. Esperado: 770.0, Obtenido: " + c.getPendienteDePago());
        }
    }
}