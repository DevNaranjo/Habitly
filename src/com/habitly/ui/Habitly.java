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
 * @version 1.0.32 (Etapa 2)
 * @since 03-04-26
 */
public class Habitly {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    public static void main(String[] args) {
        // Inicialización del sistema: Carga de datos cifrados con AES
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("Aviso: Error de compatibilidad al cargar datos previos.");
            System.out.println("Se recomienda realizar un borrado de fábrica (Opción 7).");
        }

        // V1.0.32: Setup Wizard forzado
        while (gestor.obtenerTodosLosUsuarios().isEmpty()) {
            mostrarAsistenteInicial();
        }

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
                    System.out.println("Guardando cambios en el repositorio local cifrado...");
                    gestor.guardarDatos();
                    System.out.println("Sesión finalizada. ¡Hasta pronto!");
                }
                case 7 -> borrarDatosSistema();
                default -> System.out.println("Error: Opción no válida.");
            }
        } while (opcion != 6 && opcion != 7); //Al borrar los datos del sistema también se sale.

        sc.close();
    }

    private static void mostrarAsistenteInicial() {
        System.out.println("\nBIENVENIDO A TU ESPACIO EN HABITLY");
        System.out.println("Para comenzar a gestionar tus viviendas, primero necesitamos saber quién eres.");
        System.out.println("Esto permitirá que todas las operaciones queden vinculadas a tu perfil.");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("1. Registrarme como PROPIETARIO (Voy a alquilar mis inmuebles)");
        System.out.println("2. Registrarme como INQUILINO (Busco gestionar mis alquileres)");
        System.out.println("3. Acceder como INVITADO (Solo quiero probar la aplicación)");
        System.out.println("----------------------------------------------------------------------");

        int opcion = leerEntero("¿Cómo quieres empezar hoy?: ");

        switch (opcion) {
            case 1 -> registrarPropietario();
            case 2 -> registrarInquilino();
            case 3 -> activarModoInvitado();
            default -> System.out.println("[!] Por favor, elige una opción para configurar tu cuenta.");
        }
    }

    private static void activarModoInvitado() {
        System.out.println("\n[!] MODO INVITADO ACTIVADO (Sesión temporal)");
        System.out.println("Puedes explorar todas las funciones, pero los datos se borrarán al salir.");

        Propietario invitado = new Propietario("GUEST-001", "Invitado Temporal", 0, "guest@habitly.com", false);

        if (gestor.añadirUsuario(invitado)) {
            System.out.println("Acceso temporal concedido. Identificador: GUEST-001");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("    HABITLY - GESTIÓN INMOBILIARIA v1.0.21");
        System.out.println("========================================");
        System.out.println("1. Gestionar usuarios (Propietarios/Inquilinos)");
        System.out.println("2. Registrar nueva vivienda");
        System.out.println("3. Consultar inventario detallado");
        System.out.println("4. Registrar cobro de mensualidad");
        System.out.println("5. Actualización de precios (IPC)");
        System.out.println("6. Salir del programa");
        System.out.println("7. Borrar datos (Reseteado de fábrica)");
        System.out.println("----------------------------------------");
    }

    // --- MÉTODOS ACTUALIZADOS V1.0.21 (ETAPA 2 FINAL) ---

    private static void registrarVivienda() {
        System.out.println("\n--- NUEVO REGISTRO DE VIVIENDA ---");

        System.out.print("DNI del Propietario responsable: ");
        String dniProp = sc.nextLine();

        if (gestor.obtenerUsuario(dniProp) == null) {
            System.out.println("Error: El DNI no existe. Registre al propietario primero.");
            return;
        }

        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        double precioBase = leerDouble("Precio base mensual (EUR): ");
        double superficie = leerDouble("Superficie útil (m2): ");
        int habitaciones = leerEntero("Número de habitaciones: ");
        int baños = leerEntero("Número de baños: ");
        boolean tieneGaraje = leerBoolean("¿Tiene garaje? (S/N): ");
        boolean tienePiscina = leerBoolean("¿Tiene piscina? (S/N): ");
        boolean estaAmueblado = leerBoolean("¿Está amueblado? (S/N): "); // Requisito Etapa 2

        System.out.print("Estado de conservación (Nuevo/Reformado/A reformar): ");
        String conservacion = sc.nextLine();

        int tipo;
        do {
            tipo = leerEntero("¿Es un Piso (1) o una Casa (2)? ");
        } while (tipo != 1 && tipo != 2);

        if (tipo == 1) {
            int planta = leerEntero("Planta: ");
            System.out.print("Puerta: ");
            String puerta = sc.nextLine();

            gestor.añadirVivienda(new Piso(direccion, precioBase, superficie, habitaciones,
                    baños, tieneGaraje, tienePiscina, estaAmueblado, conservacion, planta, puerta));
        } else {
            double metrosParcela = leerDouble("Metros de parcela/terreno: ");

            gestor.añadirVivienda(new Casa(direccion, precioBase, superficie, habitaciones,
                    baños, tieneGaraje, tienePiscina, estaAmueblado, conservacion, metrosParcela));
        }

        gestor.guardarDatos();
        System.out.println("Registro completado con éxito.");
    }

    private static void listarInventario() {
        if (gestor.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n==========================================================================================");
        System.out.println("                                INVENTARIO DE INMUEBLES v1.0.21");
        System.out.println("==========================================================================================");
        System.out.printf("%-3s | %-12s | %-15s | %-6s | %-4s | %-6s | %-8s%n",
                "ID", "ESTADO", "DIRECCIÓN", "TIPO", "M2", "EUR/M2", "EXTRAS");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);

            String tipo = (v instanceof Piso) ? "Piso" : "Casa";
            // G = Garaje, P = Piscina, A = Amueblado
            String extras = (v.isTieneGaraje() ? "G" : "-") +
                    (v.isTienePiscina() ? "P" : "-") +
                    (v.isEstaAmueblado() ? "A" : "-");

            System.out.printf("%-3d | %-12s | %-15.15s | %-6s | %-4.0f | %-6.2f | [%-3s]%n",
                    (i + 1), v.getEstado(), v.getDireccion(), tipo,
                    v.getSuperficie(), v.getPrecioMetroCuadrado(), extras);

            System.out.printf("    > Detalle: %d hab, %d baños | %s | Pendiente: %.2f EUR%n",
                    v.getHabitaciones(), v.getBaños(), v.getConservacion(), v.getPendienteDePago());
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    private static void gestionarPago() {
        if (gestor.estaVacio()) {
            System.out.println("No hay viviendas registradas.");
            return;
        }

        listarInventario();
        int indice = leerEntero("Seleccione el número de vivienda: ") - 1;

        if (indice >= 0 && indice < gestor.tamañoInventario()) {
            Vivienda elegida = gestor.obtenerVivienda(indice);
            double cuota = leerDouble("Cantidad a abonar (EUR): ");
            elegida.registrarPago(cuota);
            gestor.guardarDatos();
            System.out.println("Pago procesado.");
        } else {
            System.out.println("Error: Índice no válido.");
        }
    }

    private static void aplicarIPCGeneral() {
        if (gestor.estaVacio()) {
            System.out.println("No hay viviendas para actualizar.");
            return;
        }

        double porcentaje = leerDouble("Porcentaje de incremento (IPC): ");
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            gestor.obtenerVivienda(i).aplicarSubidaAnual(porcentaje);
        }
        gestor.guardarDatos();
        System.out.println("Precios actualizados.");
    }

    private static void borrarDatosSistema() {
        System.out.print("\n¿Confirmar borrado total? (Escriba 'SI'): ");
        String confirmacion = sc.nextLine().toUpperCase();

        if (confirmacion.equals("SI")) {
            if (gestor.borrarDatosAplicacion()) {
                System.out.println("Datos eliminados correctamente.");
            } else {
                System.out.println("Error al intentar borrar los archivos.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void registrarUsuarioMenu() {
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
        System.out.println("    HABITLY - REGISTRO USUARIO v1.0.3");
        System.out.println("========================================");
        System.out.println("1. Registrar propietario");
        System.out.println("2. Registrar inquilino");
        System.out.println("3. Listar usuarios");
        System.out.println("4. Buscar Usuario (DNI)");
        System.out.println("5. Eliminar Usuario (DNI)");
        System.out.println("6. Volver al Menú Principal");
        System.out.println("----------------------------------------");
    }

    public static void registrarPropietario() {
        System.out.println("\n--- NUEVO REGISTRO DE PROPIETARIO ---");
        System.out.print("DNI/CIF: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) != null) {
            System.out.println("Error: Ya existe un usuario registrado con este DNI.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        int telefono = leerEntero("Teléfono de contacto: ");
        System.out.print("E-mail: ");
        String email = sc.nextLine();

        System.out.println("\n--- TIPO DE TITULARIDAD ---");
        boolean esEmpresa = leerBoolean("¿El propietario es una empresa/sociedad? (S/N): ");

        Propietario p = new Propietario(dni, nombre, telefono, email, esEmpresa);
        if (gestor.añadirUsuario(p)) {
            gestor.guardarDatos();
            System.out.println("Propietario registrado con éxito.");
        }
    }

    public static void registrarInquilino() {
        System.out.println("\n--- NUEVO REGISTRO DE INQUILINO ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) != null) {
            System.out.println("Error: Ya existe un usuario registrado con ese DNI.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        int telefono = leerEntero("Teléfono de contacto: ");
        System.out.print("E-mail: ");
        String email = sc.nextLine();

        System.out.println("\n--- EVALUACIÓN DE RIESGO FINANCIERO ---");
        int solvencia;
        while (true) {
            solvencia = leerEntero("Introduzca el índice calculado (0-100): ");
            if (solvencia >= 0 && solvencia <= 100) {
                break;
            }
            System.out.println("Error: El índice debe estar entre 0 y 100.");
        }

        Inquilino i = new Inquilino(dni, nombre, telefono, email, solvencia);
        if (gestor.añadirUsuario(i)) {
            gestor.guardarDatos();
            System.out.println("Inquilino registrado con éxito.");
        }
    }

    private static void listarUsuarios() {
        List<Usuario> lista = gestor.obtenerTodosLosUsuarios();

        if (lista.isEmpty()) {
            System.out.println("\n[!] No hay registros para mostrar.");
            return;
        }

        Iterator<Usuario> it = lista.iterator();

        System.out.println("\n====================================================================");
        System.out.println("                   LISTADO COMPLETO DE USUARIOS");
        System.out.println("====================================================================");
        System.out.printf("%-12s | %-20s | %-12s | %-15s%n", "IDENTIFICADOR", "NOMBRE", "PERFIL", "DETALLES");
        System.out.println("--------------------------------------------------------------------");

        while (it.hasNext()) {
            Usuario u = it.next();
            String perfil = (u instanceof Propietario) ? "PROPIETARIO" : "INQUILINO";
            String detalles = "";

            if (u instanceof Inquilino inq) {
                detalles = "Solvencia: " + inq.getSolvencia() + "%";
            } else if (u instanceof Propietario prop) {
                detalles = prop.isEsEmpresa() ? "Empresa/CIF" : "Particular";
            }

            System.out.printf("%-12s | %-20s | %-12s | %-15s%n", u.getDni(), u.getNombre(), perfil, detalles);
        }
        System.out.println("====================================================================");
    }

    public static void buscarUsuario() {
        System.out.println("\n--- BUSCADOR DE USUARIO ---");
        System.out.print("Introduce el DNI: ");
        String dni = sc.nextLine();

        Usuario u = gestor.obtenerUsuario(dni);
        if (u != null) {
            String tipo = (u instanceof Propietario) ? "PROPIETARIO" : "INQUILINO";
            System.out.println("----------------------------------------");
            System.out.println("Tipo: " + tipo + " | Nombre: " + u.getNombre());
            System.out.println("DNI: " + u.getDni() + " | Email: " + u.getEmail());
            System.out.println("----------------------------------------");
        } else {
            System.out.println("Error: No se encontró ningún usuario.");
        }
    }

    public static void eliminarUsuario() {
        System.out.println("\n--- ELIMINADOR DE USUARIO ---");
        System.out.print("DNI a eliminar: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) == null) {
            System.out.println("Error: El usuario no existe.");
            return;
        }

        System.out.print("¿Confirmar borrado? (SI): ");
        if (sc.nextLine().toUpperCase().equals("SI")) {
            if (gestor.eliminarUsuario(dni)) {
                gestor.guardarDatos();
                System.out.println("Usuario eliminado.");
            }
        }
    }

    private static boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) return false;
            System.out.println("Error: Responda S o N.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Introduzca un número entero.");
                sc.nextLine();
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = sc.nextDouble();
                sc.nextLine();
                if (valor <= 0) continue;
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Formato numérico no válido.");
                sc.nextLine();
            }
        }
    }
}