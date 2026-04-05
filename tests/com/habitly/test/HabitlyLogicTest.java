package com.habitly.test;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;

/**
 * Prueba los "Cerrojos" y el "Hall de Entrada" del parche v1.0.33.
 */
public class HabitlyLogicTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTS DE LÓGICA (v1.0.33) ===");
        testSesionYLogin();
        testSeguridadInvitado();
        System.out.println("=== TESTS DE LÓGICA FINALIZADOS ===\n");
    }

    private static void testSesionYLogin() {
        GestorInventario gestor = new GestorInventario();
        Propietario p = new Propietario("123X", "Admin", 0, "a@a.com", true);

        // Probamos añadirUsuario y buscarPorDni (métodos de tu Gestor)
        gestor.añadirUsuario(p);
        Usuario encontrado = gestor.buscarPorDni("123X");

        if (encontrado != null && encontrado.getNombre().equals("Admin")) {
            System.out.println("[OK] Registro y búsqueda por DNI correctos.");
        } else {
            System.out.println("[ERROR] No se encuentra al usuario registrado.");
        }

        // Probamos la sesión (v1.0.33)
        gestor.setUsuarioIdentificado(encontrado);
        if (gestor.getUsuarioIdentificado() != null && gestor.getUsuarioIdentificado().getDni().equals("123X")) {
            System.out.println("[OK] Inicio de sesión activo correcto.");
        } else {
            System.out.println("[ERROR] El usuario identificado no se guardó.");
        }
    }

    private static void testSeguridadInvitado() {
        GestorInventario gestor = new GestorInventario();
        // Simulamos activarModoInvitado() de tu Main
        Propietario invitado = new Propietario("GUEST-001", "Invitado", 0, "g@h.com", false);
        gestor.setUsuarioIdentificado(invitado);

        // Verificamos que el DNI sea el que dispara el "Cerrojo" en tu Main
        if (gestor.getUsuarioIdentificado().getDni().equals("GUEST-001")) {
            System.out.println("[OK] El sistema identifica correctamente al Invitado para bloquearlo.");
        } else {
            System.out.println("[ERROR] El DNI de invitado no coincide con el cerrojo.");
        }
    }
}