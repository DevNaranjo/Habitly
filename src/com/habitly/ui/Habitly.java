package com.habitly.ui;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de usuario (UI) de Habitly.
 * Coordina la interacción entre el usuario y el sistema de persistencia,
 * gestionando el flujo de navegación, la validación de entradas y la
 * seguridad basada en roles (Propietario, Inquilino e Invitado).
 * * @author DevNaranjo
 * @version 1.0.4 (wip: Economy & persistence)
 * @since 1.0.0
 */
public class Habitly {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    /**
     * Punto de entrada principal. Carga datos y gestiona el bucle de la aplicación.
     */
    public static void main(String[] args) {
        // Carga inicial de archivos binarios
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("[!] Aviso: Error de compatibilidad al cargar datos previos.");
        }

        // Obliga a identificarse antes de entrar al menú principal
        while (gestor.getUsuarioIdentificado() == null) {
            mostrarAsistenteInicial();
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una operación (1-8): ");

            switch (opcion) {
                case 1 -> registrarUsuarioMenu();
                case 2 -> registrarVivienda();
                case 3 -> listarInventario();
                case 4 -> {
                    // Lógica polimórfica según tipo de usuario
                    if (gestor.getUsuarioIdentificado() instanceof Inquilino) menuEstadoDeCuenta();
                    else gestionarPago();
                }
                case 5 -> {
                    if (gestor.getUsuarioIdentificado() instanceof Propietario) registrarGastoSuministro();
                    else System.out.println("[!] Acceso denegado: Solo los propietarios registran facturas.");
                }
                case 6 -> aplicarIPCGeneral();
                case 7 -> cerrarSesionSegura();
                case 8 -> borrarDatosSistema();
                default -> System.out.println("Error: Opción no válida.");
            }
        } while (opcion != 7 && opcion != 8);

        sc.close();
    }

    /**
     * Gestiona el cierre de sesión y la persistencia final de datos.
     */
    private static void cerrarSesionSegura() {
        System.out.println("\nGuardando cambios y cerrando sesión...");
        gestor.guardarDatos();
        System.out.println("¡Hasta pronto, " + gestor.getUsuarioIdentificado().getNombre() + "!");
        gestor.setUsuarioIdentificado(null);
    }

    /**
     * Muestra el asistente de acceso para usuarios no identificados.
     */
    private static void mostrarAsistenteInicial() {
        System.out.println("\n========================================");
        System.out.println("       BIENVENIDO A HABITLY v1.0.4");
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

    /**
     * Valida el acceso de un usuario mediante su DNI.
     */
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

    /**
     * Crea una sesión de invitado con privilegios restringidos.
     */
    private static void activarModoInvitado() {
        System.out.println("\nMODO INVITADO ACTIVADO (Privilegios limitados)");
        Propietario invitado = new Propietario("GUEST-001", "Invitado Temporal", 0, "guest@habitly.com", false);
        gestor.setUsuarioIdentificado(invitado);
    }

    /**
     * Muestra el menú principal de operaciones.
     */
    private static void mostrarMenu() {
        Usuario u = gestor.getUsuarioIdentificado();
        String nombreUser = u.getNombre().toUpperCase();
        System.out.println("\n========================================");
        System.out.println("  HABITLY - SESIÓN: " + nombreUser);
        System.out.println("========================================");
        System.out.println("1. Gestión de usuarios y perfiles");
        System.out.println("2. Registrar nueva vivienda");
        System.out.println("3. Consultar inventario detallado");

        if (u instanceof Inquilino) {
            System.out.println("4. MI ESTADO DE CUENTA (Facturas y Deuda)");
        } else {
            System.out.println("4. Registrar cobro de mensualidad");
            System.out.println("5. REGISTRAR FACTURA/SUMINISTRO (v1.0.4)");
        }

        System.out.println("6. Actualización de precios (IPC)");
        System.out.println("7. Salir y Guardar");
        System.out.println("8. Borrar datos (Reset de fábrica)");
        System.out.println("----------------------------------------");
    }

    /**
     * Permite a un propietario vincular un nuevo gasto a una de sus propiedades.
     */
    private static void registrarGastoSuministro() {
        List<Vivienda> misPisos = gestor.getViviendasPorDueño(gestor.getUsuarioIdentificado().getDni());
        if (misPisos.isEmpty()) {
            System.out.println("[!] No tienes viviendas registradas a tu nombre.");
            return;
        }

        System.out.println("\n--- Seleccione Vivienda ---");
        for (int i = 0; i < misPisos.size(); i++) {
            System.out.println((i + 1) + ". " + misPisos.get(i).getDireccion());
        }
        int sel = leerEntero("Seleccione vivienda: ") - 1;

        if (sel >= 0 && sel < misPisos.size()) {
            System.out.print("Concepto (Luz, Agua, IBI...): ");
            String concepto = sc.nextLine();
            double monto = leerDouble("Importe (EUR): ");

            // Generación de ID único temporal para el gasto
            String idGasto = "G-" + System.currentTimeMillis() % 1000;
            misPisos.get(sel).registrarFactura(new Gasto(idGasto, concepto, monto));
            gestor.guardarDatos();
            System.out.println("Gasto registrado y cifrado correctamente.");
        }
    }

    /**
     * Muestra el balance pendiente del inquilino logueado.
     */
    private static void menuEstadoDeCuenta() {
        Vivienda casa = gestor.getViviendaDeInquilino(gestor.getUsuarioIdentificado().getDni());
        if (casa == null) {
            System.out.println("[!] No constas como inquilino en ninguna vivienda.");
            return;
        }
        System.out.println("\n--- ESTADO DE CUENTA ---");
        System.out.println("Dirección: " + casa.getDireccion());
        System.out.printf("TOTAL PENDIENTE: %.2f EUR\n", casa.calcularBalanceTotalPendiente());

        // Posibilidad de liquidar suministros individuales
        List<Gasto> pendientes = casa.getListaGastosPendientes();
        if (!pendientes.isEmpty() && leerBoolean("¿Desea liquidar un suministro? (S/N): ")) {
            for (int j = 0; j < pendientes.size(); j++) {
                System.out.println((j+1) + ". " + pendientes.get(j).getConcepto() + " (" + pendientes.get(j).getMonto() + " EUR)");
            }
            int op = leerEntero("Seleccione número: ") - 1;
            if (op >= 0 && op < pendientes.size()) {
                pendientes.get(op).marcarComoPagado();
                gestor.guardarDatos();
                System.out.println("Pago registrado.");
            }
        }
    }

    /**
     * Submenú para la gestión de perfiles. Restringido a Propietarios.
     */
    private static void registrarUsuarioMenu() {
        Usuario actual = gestor.getUsuarioIdentificado();

        if (!(actual instanceof Propietario) || actual.getDni().equals("GUEST-001")) {
            System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("ACCESO DENEGADO: PRIVILEGIOS INSUFICIENTES");
            System.out.println("Solo los PROPIETARIOS pueden gestionar perfiles.");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }

        int opcionUsuario;
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

    /**
     * Muestra las opciones del submenú de usuarios.
     */
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

    /**
     * Registra un nuevo Propietario en el sistema.
     */
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
            gestor.setUsuarioIdentificado(p);
            gestor.guardarDatos();
            System.out.println("Usuario registrado e identificado.");
        }
    }

    /**
     * Registra un nuevo Inquilino en el sistema.
     */
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
            gestor.setUsuarioIdentificado(i);
            gestor.guardarDatos();
            System.out.println("Usuario registrado e identificado.");
        }
    }

    /**
     * Formulario para dar de alta una nueva vivienda vinculada al propietario actual.
     */
    private static void registrarVivienda() {
        if (!(gestor.getUsuarioIdentificado() instanceof Propietario)) {
            System.out.println("[!] Solo los propietarios pueden registrar viviendas.");
            return;
        }
        System.out.println("\n--- NUEVO REGISTRO DE VIVIENDA ---");
        String dniProp = gestor.getUsuarioIdentificado().getDni();

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

        int tipo = leerEntero("¿Tipo? Piso (1) o Casa (2): ");
        if (tipo == 1) {
            int planta = leerEntero("Planta: ");
            System.out.print("Puerta: ");
            String puerta = sc.nextLine();
            gestor.añadirVivienda(new Piso(dniProp, direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, planta, puerta));
        } else {
            double parcela = leerDouble("Metros parcela: ");
            gestor.añadirVivienda(new Casa(dniProp, direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, parcela));
        }
        gestor.guardarDatos();
        System.out.println("Vivienda registrada con éxito.");
    }

    /**
     * Muestra el listado tabular de todas las viviendas en el sistema.
     */
    private static void listarInventario() {
        if (gestor.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n==========================================================================================");
        System.out.printf("%-3s | %-12s | %-15s | %-6s | %-4s | %-6s | %-8s%n", "ID", "ESTADO", "DIRECCIÓN", "TIPO", "M2", "EUR/M2", "EXTRAS");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);
            String tipo = (v instanceof Piso) ? "Piso" : "Casa";
            String extras = (v.isTieneGaraje() ? "G" : "-") + (v.isTienePiscina() ? "P" : "-") + (v.isEstaAmueblado() ? "A" : "-");
            System.out.printf("%-3d | %-12s | %-15.15s | %-6s | %-4.0f | %-6.2f | [%-3s]%n", (i + 1), v.getEstado(), v.getDireccion(), tipo, v.getSuperficie(), v.getPrecioMetroCuadrado(), extras);
        }
    }

    /**
     * Registra un pago de renta parcial o total para una vivienda.
     */
    private static void gestionarPago() {
        listarInventario();
        if (gestor.estaVacio()) return;
        int idx = leerEntero("Número de vivienda: ") - 1;
        Vivienda v = gestor.obtenerVivienda(idx);
        if (v != null) {
            double cuota = leerDouble("Cantidad a abonar: ");
            v.registrarPago(cuota);
            gestor.guardarDatos();
            System.out.println("Pago registrado.");
        }
    }

    /**
     * Aplica un incremento porcentual a la renta base de todas las viviendas.
     */
    private static void aplicarIPCGeneral() {
        double porc = leerDouble("Porcentaje incremento: ");
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);
            if (v != null) v.aplicarSubidaAnual(porc);
        }
        gestor.guardarDatos();
        System.out.println("IPC aplicado.");
    }

    /**
     * Muestra todos los usuarios registrados en el sistema.
     */
    private static void listarUsuarios() {
        List<Usuario> lista = gestor.obtenerTodosLosUsuarios();
        System.out.println("\n------------------------------------------------------------");
        for (Usuario u : lista) {
            String perfil = (u instanceof Propietario) ? "PROP" : "INQ";
            System.out.printf("%-12s | %-20s | %-5s%n", u.getDni(), u.getNombre(), perfil);
        }
    }

    /**
     * Localiza y muestra la información de un usuario por su DNI.
     */
    private static void buscarUsuario() {
        System.out.print("DNI a buscar: ");
        String dni = sc.nextLine();
        Usuario u = gestor.obtenerUsuario(dni);
        if (u != null) System.out.println("Encontrado: " + u.getNombre() + " (" + u.getEmail() + ")");
        else System.out.println("No encontrado.");
    }

    /**
     * Elimina un usuario del sistema mediante su DNI.
     */
    private static void eliminarUsuario() {
        System.out.print("DNI a eliminar: ");
        String dni = sc.nextLine();
        if (gestor.eliminarUsuario(dni)) {
            gestor.guardarDatos();
            System.out.println("Usuario eliminado.");
        }
    }

    /**
     * Realiza un borrado físico y lógico de los datos y cierra la aplicación.
     */
    private static void borrarDatosSistema() {
        System.out.print("\n¿Confirmar borrado total? Esta acción es irreversible (SI): ");
        String confirmacion = sc.nextLine().toUpperCase();

        if (confirmacion.equals("SI")) {
            if (gestor.borrarDatosAplicacion()) {
                System.out.println("\nEl sistema se ha reseteado correctamente.");
                System.out.println("Cerrando aplicación de forma segura...");
                System.exit(0);
            } else {
                System.out.println("\nERROR: No se pudo eliminar el archivo físico.");
            }
        } else {
            System.out.println("\n[!] Operación cancelada. Sus datos están a salvo.");
        }
    }

    /**
     * Utilidad para leer enteros con control de excepciones.
     */
    private static int leerEntero(String m) {
        while (true) {
            try {
                System.out.print(m);
                int v = sc.nextInt();
                sc.nextLine();
                return v;
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("ERROR: Formato numérico incorrecto. Introduzca un número entero (ej: 12).");
            }
        }
    }

    /**
     * Utilidad para leer decimales con control de excepciones.
     */
    private static double leerDouble(String m) {
        while (true) {
            try {
                System.out.print(m);
                double v = sc.nextDouble();
                sc.nextLine();
                return v;
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("ERROR: Formato decimal incorrecto. Use la coma (ej: 1200,50).");
            }
        }
    }

    /**
     * Utilidad para leer confirmaciones booleanas S/N.
     */
    private static boolean leerBoolean(String m) {
        while (true) {
            System.out.print(m);
            String in = sc.nextLine().trim().toUpperCase();
            if (in.equals("S")) return true;
            if (in.equals("N")) return false;
            System.out.println("ERROR: Por favor, responda con 'S' para Sí o 'N' para No.");
        }
    }
}