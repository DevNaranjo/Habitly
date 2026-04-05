package com.habitly.ui;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de usuario de Habitly.
 * Coordina la interacción entre el usuario y el sistema de persistencia cifrado.
 * @author DevNaranjo
 * @version 1.0.33 (Parche de Seguridad y Acceso)
 * @since 03-04-26
 */
public class Habitly {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    public static void main(String[] args) {
        //Inicialización
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("Aviso: Error de compatibilidad al cargar datos previos.");
        }

        //FASE DE IDENTIFICACIÓN (Hall de Entrada)
        //No permite avanzar al menú principal hasta que haya un usuario en sesión.
        while (gestor.getUsuarioIdentificado() == null) {
            mostrarAsistenteInicial();
        }

        //Menú Principal
        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una operación (1-7): ");

            switch (opcion) {
                case 1 -> registrarUsuarioMenu();
                case 2 -> registrarVivienda();
                case 3 -> listarInventario();
                case 4 -> gestionarPago();
                case 5 -> aplicarIPCGeneral();
                case 6 -> {
                    System.out.println("Guardando cambios y cerrando sesión...");
                    gestor.guardarDatos();
                    System.out.println("¡Hasta pronto, " + gestor.getUsuarioIdentificado().getNombre() + "!");
                }
                case 7 -> {
                    borrarDatosSistema();
                    // Al borrar datos, el bucle termina para forzar un reinicio limpio.
                }
                default -> System.out.println("Error: Opción no válida.");
            }
        } while (opcion != 6 && opcion != 7);

        sc.close();
    }

    private static void mostrarAsistenteInicial() {
        System.out.println("\n========================================");
        System.out.println("       BIENVENIDO A HABITLY v1.0.33");
        System.out.println("========================================");
        System.out.println("1. Registrarme como PROPIETARIO");
        System.out.println("2. Registrarme como INQUILINO");
        System.out.println("3. Acceder como INVITADO (Solo lectura)");

        boolean hayUsuarios = !gestor.obtenerTodosLosUsuarios().isEmpty();
        if (hayUsuarios) {
            System.out.println("4. Acceder con mi DNI (Login)");
        }
        System.out.println("----------------------------------------");

        int opcion = leerEntero("¿Cómo quieres empezar hoy?: ");

        switch (opcion) {
            case 1 -> registrarPropietario();
            case 2 -> registrarInquilino();
            case 3 -> activarModoInvitado();
            case 4 -> {
                if (hayUsuarios) accederSistema();
                else System.out.println("[!] Opción no disponible.");
            }
            default -> System.out.println("[!] Elija una opción para configurar su sesión.");
        }
    }

    private static void accederSistema() {
        System.out.print("Introduzca su DNI para acceder: ");
        String dni = sc.nextLine();

        Usuario u = gestor.obtenerUsuario(dni);
        if (u != null) {
            gestor.setUsuarioIdentificado(u);
            System.out.println("\nAcceso concedido. Bienvenido, " + u.getNombre());
        } else {
            System.out.println("Error: DNI no encontrado en el sistema.");
        }
    }

    private static void activarModoInvitado() {
        System.out.println("\nMODO INVITADO ACTIVADO (Privilegios limitados)");
        Propietario invitado = new Propietario("GUEST-001", "Invitado Temporal", 0, "guest@habitly.com", false);
        gestor.setUsuarioIdentificado(invitado);
    }

    private static void mostrarMenu() {
        String nombreUser = gestor.getUsuarioIdentificado().getNombre().toUpperCase();
        System.out.println("\n========================================");
        System.out.println("  HABITLY - SESIÓN: " + nombreUser);
        System.out.println("========================================");
        System.out.println("1. Gestión de usuarios y perfiles");
        System.out.println("2. Registrar nueva vivienda");
        System.out.println("3. Consultar inventario detallado");
        System.out.println("4. Registrar cobro de mensualidad");
        System.out.println("5. Actualización de precios (IPC)");
        System.out.println("6. Salir y Guardar");
        System.out.println("7. Borrar datos (Reset de fábrica)");
        System.out.println("----------------------------------------");
    }

    private static void registrarUsuarioMenu() {
        // CERROJO DE SEGURIDAD PARA INVITADOS
        if (gestor.getUsuarioIdentificado().getDni().equals("GUEST-001")) {
            System.out.println("\nACCESO DENEGADO: Los invitados no pueden gestionar perfiles.");
            return;
        }

        int opcionUsuario = 0;
        do {
            mostrarMenuUsuario();
            opcionUsuario = leerEntero("Seleccione una operación (1-6): ");

            switch (opcionUsuario) {
                case 1 -> registrarPropietario();
                case 2 -> registrarInquilino();
                case 3 -> listarUsuarios();
                case 4 -> buscarUsuario();
                case 5 -> eliminarUsuario();
                case 6 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Error: Opción no válida.");
            }
        } while (opcionUsuario != 6);
    }

    private static void mostrarMenuUsuario() {
        System.out.println("\n========================================");
        System.out.println("    HABITLY - REGISTRO USUARIO");
        System.out.println("========================================");
        System.out.println("1. Registrar propietario");
        System.out.println("2. Registrar inquilino");
        System.out.println("3. Listar usuarios");
        System.out.println("4. Buscar Usuario (DNI)");
        System.out.println("5. Eliminar Usuario (DNI)");
        System.out.println("6. Volver");
        System.out.println("----------------------------------------");
    }

    public static void registrarPropietario() {
        System.out.println("\n--- NUEVO REGISTRO DE PROPIETARIO ---");
        System.out.print("DNI/CIF: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) != null) {
            System.out.println("Error: El DNI ya existe.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        int tlf = leerEntero("Teléfono: ");
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        boolean esEmpresa = leerBoolean("¿Es empresa? (S/N): ");

        Propietario p = new Propietario(dni, nombre, tlf, email, esEmpresa);
        if (gestor.añadirUsuario(p)) {
            gestor.setUsuarioIdentificado(p); // Login automático
            gestor.guardarDatos();
            System.out.println("Usuario registrado e identificado.");
        }
    }

    public static void registrarInquilino() {
        System.out.println("\n--- NUEVO REGISTRO DE INQUILINO ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) != null) {
            System.out.println("Error: El DNI ya existe.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        int tlf = leerEntero("Teléfono: ");
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        int solvencia = leerEntero("Solvencia (0-100): ");

        Inquilino i = new Inquilino(dni, nombre, tlf, email, solvencia);
        if (gestor.añadirUsuario(i)) {
            gestor.setUsuarioIdentificado(i); // Login automático
            gestor.guardarDatos();
            System.out.println("Usuario registrado e identificado.");
        }
    }

    private static void registrarVivienda() {
        System.out.println("\n--- NUEVO REGISTRO DE VIVIENDA ---");
        System.out.print("DNI del Propietario: ");
        String dniProp = sc.nextLine();

        if (gestor.obtenerUsuario(dniProp) == null) {
            System.out.println("Error: El DNI no existe.");
            return;
        }

        System.out.print("Dirección: ");
        String direccion = sc.nextLine();
        double precio = leerDouble("Precio base (EUR): ");
        double m2 = leerDouble("Superficie (m2): ");
        int hab = leerEntero("Habitaciones: ");
        int banos = leerEntero("Baños: ");
        boolean garaje = leerBoolean("¿Garaje? (S/N): ");
        boolean piscina = leerBoolean("¿Piscina? (S/N): ");
        boolean amueblado = leerBoolean("¿Amueblado? (S/N): ");
        System.out.print("Conservación: ");
        String cons = sc.nextLine();

        int tipo = leerEntero("¿Piso (1) o Casa (2)?: ");
        if (tipo == 1) {
            int planta = leerEntero("Planta: ");
            System.out.print("Puerta: ");
            String puerta = sc.nextLine();
            gestor.añadirVivienda(new Piso(direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, planta, puerta));
        } else {
            double parcela = leerDouble("Metros parcela: ");
            gestor.añadirVivienda(new Casa(direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, parcela));
        }
        gestor.guardarDatos();
        System.out.println("Vivienda registrada con éxito.");
    }

    private static void listarInventario() {
        if (gestor.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-3s | %-12s | %-15s | %-6s | %-4s | %-6s | %-8s%n", "ID", "ESTADO", "DIRECCIÓN", "TIPO", "M2", "EUR/M2", "EXTRAS");
        System.out.println("-".repeat(90));

        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);
            String tipo = (v instanceof Piso) ? "Piso" : "Casa";
            String extras = (v.isTieneGaraje() ? "G" : "-") + (v.isTienePiscina() ? "P" : "-") + (v.isEstaAmueblado() ? "A" : "-");
            System.out.printf("%-3d | %-12s | %-15.15s | %-6s | %-4.0f | %-6.2f | [%-3s]%n", (i + 1), v.getEstado(), v.getDireccion(), tipo, v.getSuperficie(), v.getPrecioMetroCuadrado(), extras);
        }
    }

    private static void gestionarPago() {
        listarInventario();
        if (gestor.estaVacio()) return;
        int idx = leerEntero("Número de vivienda: ") - 1;
        if (idx >= 0 && idx < gestor.tamañoInventario()) {
            double cuota = leerDouble("Cantidad a abonar: ");
            gestor.obtenerVivienda(idx).registrarPago(cuota);
            gestor.guardarDatos();
            System.out.println("Pago registrado.");
        }
    }

    private static void aplicarIPCGeneral() {
        double porc = leerDouble("Porcentaje incremento: ");
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            gestor.obtenerVivienda(i).aplicarSubidaAnual(porc);
        }
        gestor.guardarDatos();
        System.out.println("IPC aplicado.");
    }

    private static void listarUsuarios() {
        List<Usuario> lista = gestor.obtenerTodosLosUsuarios();
        System.out.println("\n" + "-".repeat(60));
        for (Usuario u : lista) {
            String perfil = (u instanceof Propietario) ? "PROP" : "INQ";
            System.out.printf("%-12s | %-20s | %-5s%n", u.getDni(), u.getNombre(), perfil);
        }
    }

    private static void buscarUsuario() {
        System.out.print("DNI a buscar: ");
        String dni = sc.nextLine();
        Usuario u = gestor.obtenerUsuario(dni);
        if (u != null) System.out.println("Encontrado: " + u.getNombre() + " (" + u.getEmail() + ")");
        else System.out.println("No encontrado.");
    }

    private static void eliminarUsuario() {
        System.out.print("DNI a eliminar: ");
        String dni = sc.nextLine();
        if (gestor.eliminarUsuario(dni)) {
            gestor.guardarDatos();
            System.out.println("Usuario eliminado.");
        }
    }

    private static void borrarDatosSistema() {
        System.out.print("\n⚠️ ¿Confirmar borrado total? Esta acción es irreversible (SI): ");
        String confirmacion = sc.nextLine().toUpperCase();

        if (confirmacion.equals("SI")) {
            boolean exito = gestor.borrarDatosAplicacion();

            if (exito) {
                System.out.println("El sistema se ha reseteado correctamente.");
                System.out.println("Cerrando aplicación para aplicar cambios...");
                // La condición del while hará que se salga solo
            } else {
                System.out.println("ERROR: No se pudo eliminar el archivo 'sistema.dat'.");
                System.out.println("Compruebe que no esté abierto en otro programa e inténtelo de nuevo.");
            }
        } else {
            System.out.println("Operación cancelada. Sus datos están a salvo.");
        }
    }
    private static boolean leerBoolean(String m) {
        while (true) {
            System.out.print(m);
            String in = sc.nextLine().trim().toUpperCase();
            if (in.equals("S")) return true;
            if (in.equals("N")) return false;
        }
    }

    private static int leerEntero(String m) {
        while (true) {
            try {
                System.out.print(m);
                int v = sc.nextInt(); sc.nextLine();
                return v;
            } catch (Exception e) { sc.nextLine(); }
        }
    }

    private static double leerDouble(String m) {
        while (true) {
            try {
                System.out.print(m);
                double v = sc.nextDouble(); sc.nextLine();
                return v;
            } catch (Exception e) { sc.nextLine(); }
        }
    }
}