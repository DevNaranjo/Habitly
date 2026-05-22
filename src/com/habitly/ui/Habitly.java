package com.habitly.ui;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

/**
 * Clase principal que gestiona la interfaz de usuario (UI) de Habitly.
 * Coordina la interacción entre el usuario y el sistema de persistencia.
 * * @author DevNaranjo
 * @version 1.0.6
 * @since 1.0.0
 */
public class Habitly {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    /**
     * Punto de entrada principal. Carga datos y gestiona el bucle de la aplicación.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        // Carga inicial de archivos binarios
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("[!] Aviso: Error de compatibilidad al cargar datos previos.");
        }

        while (true) {
            // Obliga a identificarse antes de entrar al menú principal
            while (gestor.getUsuarioIdentificado() == null) {
                mostrarAsistenteInicial();
            }

            do {
                mostrarMenu();
                Usuario u = gestor.getUsuarioIdentificado();
                if (u == null) {
                    break;
                }
                
                if (u.getDni().equals("GUEST-001")) {
                    int opcion = leerEntero("Seleccione una operación (1-2): ");
                    switch (opcion) {
                        case 1 -> listarInventario();
                        case 2 -> cerrarSesionSegura();
                        default -> System.out.println("Error: Opción no válida.");
                    }
                } else if (u instanceof Inquilino) {
                    int opcion = leerEntero("Seleccione una operación (1-3): ");
                    switch (opcion) {
                        case 1 -> listarInventario();
                        case 2 -> menuEstadoDeCuenta();
                        case 3 -> cerrarSesionSegura();
                        default -> System.out.println("Error: Opción no válida.");
                    }
                } else {
                    int opcion = leerEntero("Seleccione una operación (1-11): ");
                    switch (opcion) {
                        case 1 -> registrarUsuarioMenu();
                        case 2 -> registrarVivienda();
                        case 3 -> listarInventario();
                        case 4 -> gestionarPago();
                        case 5 -> registrarGastoSuministro();
                        case 6 -> eliminarViviendaMenu();
                        case 7 -> verBeneficioPropietario();
                        case 8 -> gestionarContratosMenu();
                        case 9 -> aplicarIPCGeneral();
                        case 10 -> cerrarSesionSegura();
                        case 11 -> borrarDatosSistema();
                        default -> System.out.println("Error: Opción no válida.");
                    }
                }
            } while (gestor.getUsuarioIdentificado() != null);
        }
    }

    /**
     * Gestiona el cierre de sesión y la persistencia final de datos.
     */
    private static void cerrarSesionSegura() {
        Usuario u = gestor.getUsuarioIdentificado();
        if (u != null && u.getDni().equals("GUEST-001")) {
            System.out.println("\nCerrando sesión de invitado...");
            System.out.println("¡Hasta pronto!");
            gestor.setUsuarioIdentificado(null);
            return;
        }
        System.out.println("\nGuardando cambios y cerrando sesión...");
        gestor.guardarDatos();
        System.out.println("¡Hasta pronto, " + (u != null ? u.getNombre() : "Usuario") + "!");
        gestor.setUsuarioIdentificado(null);
    }

    /**
     * Muestra el asistente de acceso para usuarios no identificados.
     */
    private static void mostrarAsistenteInicial() {
        System.out.println("\n========================================");
        System.out.println("       BIENVENIDO A HABITLY (v1.0 OFICIAL)");
        System.out.println("========================================");
        System.out.println("1. Registrarme como PROPIETARIO");
        System.out.println("2. Registrarme como INQUILINO");
        System.out.println("3. Acceder como INVITADO (Solo lectura)");

        boolean hayUsuarios = !gestor.obtenerTodosLosUsuarios().isEmpty();
        if (hayUsuarios) {
            System.out.println("4. Acceder con mi DNI (Login)");
        }
        System.out.println("0. Salir de la aplicación");
        System.out.println("----------------------------------------");

        int opcion = leerEntero("¿Cómo quieres empezar hoy?: ");

        switch (opcion) {
            case 0 -> {
                System.out.println("\n¡Gracias por usar Habitly! Cerrando...");
                System.exit(0);
            }
            case 1 -> registrarPropietario();
            case 2 -> registrarInquilino();
            case 3 -> activarModoInvitado();
            case 4 -> {
                if (hayUsuarios) accederSistema();
                else System.out.println("[!] Opción no disponible.");
            }
            default -> System.out.println("[!] Elija una opción válida.");
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
            // Guardamos el usuario encontrado en la sesión del gestor
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
     * Muestra el menú principal de operaciones adaptado al rol.
     */
    private static void mostrarMenu() {
        Usuario u = gestor.getUsuarioIdentificado();
        String nombreUser = u.getNombre().toUpperCase();
        System.out.println("\n=======================================================");
        
        if (u.getDni().equals("GUEST-001")) {
            System.out.println("  HABITLY - SESIÓN INVITADO: " + nombreUser);
            System.out.println("=======================================================");
            System.out.println("1. Consultar inventario detallado");
            System.out.println("2. Salir");
        } else if (u instanceof Inquilino) {
            System.out.println("  HABITLY - SESIÓN INQUILINO: " + nombreUser);
            System.out.println("=======================================================");
            System.out.println("1. Consultar inventario detallado");
            System.out.println("2. MI ESTADO DE CUENTA (Facturas y Deuda)");
            System.out.println("3. Salir y Guardar");
        } else {
            System.out.println("  HABITLY - SESIÓN PROPIETARIO: " + nombreUser);
            System.out.println("=======================================================");
            System.out.println("1. Gestión de usuarios y perfiles");
            System.out.println("2. Registrar nueva vivienda");
            System.out.println("3. Consultar inventario detallado");
            System.out.println("4. Registrar cobro de mensualidad");
            System.out.println("5. REGISTRAR FACTURA/SUMINISTRO");
            System.out.println("6. ELIMINAR VIVIENDA");
            System.out.println("7. DASHBOARD DE BENEFICIO TOTAL");
            System.out.println("8. GESTIÓN DE CONTRATOS Y LEGAL");
            System.out.println("9. Actualización de precios (IPC)");
            System.out.println("10. Salir y Guardar");
            System.out.println("11. Borrar datos (Reset de fábrica)");
        }
        System.out.println("-------------------------------------------------------");
    }

    /**
     * Permite a un propietario vincular un nuevo gasto a una de sus propiedades.
     */
    private static void registrarGastoSuministro() {
        // Filtramos para que solo vea sus propias viviendas
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
     * Muestra el balance pendiente del inquilino logueado y permite liquidar suministros.
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
        System.out.println("    HABITLY - GESTIÓN USUARIOS");
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
            if (gestor.getUsuarioIdentificado() == null) {
                gestor.setUsuarioIdentificado(p);
                System.out.println("Usuario registrado e identificado.");
            } else {
                System.out.println("Usuario registrado correctamente.");
            }
            gestor.guardarDatos();
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
            if (gestor.getUsuarioIdentificado() == null) {
                gestor.setUsuarioIdentificado(i);
                System.out.println("Usuario registrado e identificado.");
            } else {
                System.out.println("Usuario registrado correctamente.");
            }
            gestor.guardarDatos();
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
        // Capturamos el DNI del dueño que está en sesión
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
            // Constructor de Piso con 12 parámetros
            gestor.añadirVivienda(new Piso(dniProp, direccion, precio, m2, hab, banos, garaje, piscina, amueblado, cons, planta, puerta));
        } else {
            double parcela = leerDouble("Metros parcela: ");
            // Constructor de Casa con 11 parámetros
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
        System.out.println("\nID | ESTADO | DIRECCIÓN | TIPO | M2 | EUR/M2");
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);
            String tipo = (v instanceof Piso) ? "Piso" : "Casa";
            System.out.printf("%d | %s | %s | %s | %.0f | %.2f\n", (i+1), v.getEstado(), v.getDireccion(), tipo, v.getSuperficie(), v.getPrecioMetroCuadrado());
        }
    }

    /**
     * Registra un pago de renta para una vivienda seleccionada.
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
     * Aplica un incremento porcentual a la renta de todas las viviendas (IPC).
     */
    private static void aplicarIPCGeneral() {
        double porc = leerDouble("Porcentaje incremento: ");
        for (Vivienda v : gestor.getInventario()) {
            if (v != null) v.aplicarSubidaAnual(porc);
        }
        gestor.guardarDatos();
        System.out.println("IPC aplicado.");
    }

    /**
     * Muestra todos los usuarios registrados en el sistema.
     */
    private static void listarUsuarios() {
        for (Usuario u : gestor.obtenerTodosLosUsuarios()) {
            System.out.printf("%s | %s [%s]\n", u.getDni(), u.getNombre(), (u instanceof Propietario ? "PROP" : "INQ"));
        }
    }

    /**
     * Localiza y muestra la información de un usuario por su DNI.
     */
    private static void buscarUsuario() {
        System.out.print("DNI a buscar: ");
        Usuario u = gestor.obtenerUsuario(sc.nextLine());
        if (u != null) System.out.println("Encontrado: " + u.getNombre() + " (" + u.getEmail() + ")");
        else System.out.println("No encontrado.");
    }

    /**
     * Elimina un usuario del sistema mediante su DNI.
     */
    private static void eliminarUsuario() {
        System.out.print("DNI a eliminar: ");
        if (gestor.eliminarUsuario(sc.nextLine())) {
            gestor.guardarDatos();
            System.out.println("Usuario eliminado.");
        }
    }

    /**
     * Realiza un borrado físico y lógico de los datos y cierra la aplicación.
     */
    private static void borrarDatosSistema() {
        System.out.print("\n¿Confirmar borrado total? (SI): ");
        if (sc.nextLine().equalsIgnoreCase("SI")) {
            if (gestor.borrarDatosAplicacion()) {
                System.out.println("\nSistema reseteado. Cerrando...");
                System.exit(0);
            }
        }
    }

    /**
     * Utilidad para leer enteros con control de excepciones.
     * @param m Mensaje a mostrar.
     * @return Valor entero leído.
     */
    private static int leerEntero(String m) {
        while (true) {
            try {
                System.out.print(m);
                String entrada = sc.nextLine().trim();
                if (entrada.isEmpty()) {
                    System.out.println("ERROR: La entrada no puede estar vacía.");
                    continue;
                }
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Formato numérico incorrecto. Ingrese un entero.");
            }
        }
    }

    /**
     * Utilidad para leer decimales con control de excepciones y validación de rango positivo (>0).
     * Reemplaza comas por puntos para soporte de localización en español.
     * @param m Mensaje a mostrar.
     * @return Valor double leído.
     */
    private static double leerDouble(String m) {
        while (true) {
            System.out.print(m);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("ERROR: La entrada no puede estar vacía.");
                continue;
            }
            entrada = entrada.replace(',', '.');
            try {
                double v = Double.parseDouble(entrada);
                if (v <= 0) {
                    System.out.println("ERROR: El valor debe ser estrictamente mayor que cero.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Formato decimal incorrecto. Ingrese un número válido.");
            }
        }
    }

    /**
     * Utilidad para leer confirmaciones booleanas S/N.
     * @param m Mensaje a mostrar.
     * @return true si es 'S', false si es 'N'.
     */
    private static boolean leerBoolean(String m) {
        while (true) {
            System.out.print(m);
            String in = sc.nextLine().trim().toUpperCase();
            if (in.equals("S")) return true;
            if (in.equals("N")) return false;
            System.out.println("ERROR: Use S o N.");
        }
    }

    /**
     * Menú para eliminar una vivienda del propietario actual.
     * Solo permite eliminar viviendas en estado DISPONIBLE.
     */
    private static void eliminarViviendaMenu() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        if (misViviendas.isEmpty()) {
            System.out.println("[!] No tienes viviendas registradas a tu nombre.");
            return;
        }

        System.out.println("\n--- TUS VIVIENDAS ---");
        for (int i = 0; i < misViviendas.size(); i++) {
            Vivienda v = misViviendas.get(i);
            System.out.printf("%d. %s [%s]\n", (i + 1), v.getDireccion(), v.getEstado());
        }
        int sel = leerEntero("Seleccione la vivienda a eliminar: ") - 1;

        if (sel >= 0 && sel < misViviendas.size()) {
            Vivienda v = misViviendas.get(sel);
            if (v.getEstado() != EstadoVivienda.DISPONIBLE) {
                System.out.println("[!] Error: Solo se pueden eliminar viviendas en estado DISPONIBLE.");
                return;
            }
            
            int indexGlobal = gestor.getInventario().indexOf(v);
            if (indexGlobal != -1 && gestor.eliminarVivienda(indexGlobal)) {
                System.out.println("Vivienda eliminada correctamente.");
            } else {
                System.out.println("[!] Error al intentar eliminar la vivienda.");
            }
        } else {
            System.out.println("Selección inválida.");
        }
    }

    /**
     * Dashboard de beneficio total del propietario.
     */
    private static void verBeneficioPropietario() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        double totalIngresos = 0;
        double totalGastos = 0;
        
        System.out.println("\n========================================");
        System.out.println("    DASHBOARD DE RENTABILIDAD");
        System.out.println("========================================");
        
        if (misViviendas.isEmpty()) {
            System.out.println("No tienes viviendas registradas a tu nombre.");
            return;
        }
        
        System.out.printf("%-30s | %-12s | %-12s | %-12s\n", "Dirección", "Ingresos", "Gastos", "Neto");
        System.out.println("------------------------------------------------------------------------");
        
        for (Vivienda v : misViviendas) {
            double ingresosVivienda = v.getTotalPagadoMes();
            double gastosVivienda = 0;
            for (Gasto g : v.getHistorialGastos()) {
                gastosVivienda += g.getMonto();
            }
            double netoVivienda = ingresosVivienda - gastosVivienda;
            
            totalIngresos += ingresosVivienda;
            totalGastos += gastosVivienda;
            
            System.out.printf("%-30s | %10.2f€ | %10.2f€ | %10.2f€\n", 
                v.getDireccion().length() > 30 ? v.getDireccion().substring(0, 27) + "..." : v.getDireccion(), 
                ingresosVivienda, gastosVivienda, netoVivienda);
        }
        
        double beneficioNeto = gestor.getBeneficioTotal(dniProp);
        
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-30s | %10.2f€ | %10.2f€ | %10.2f€\n", "TOTAL GENERAL", totalIngresos, totalGastos, beneficioNeto);
        System.out.println("========================================");
    }

    /**
     * Submenú para gestionar los contratos y el cumplimiento legal.
     */
    /**
     * Submenú para gestionar los contratos y el cumplimiento legal.
     */
    private static void gestionarContratosMenu() {
        int opcion;
        do {
            System.out.println("\n===========================================");
            System.out.println("   HABITLY - GESTIÓN DE CONTRATOS Y LEGAL");
            System.out.println("===========================================");
            System.out.println("1. Formalizar nuevo contrato de alquiler");
            System.out.println("2. Consultar contratos activos");
            System.out.println("3. Asignar límite IRAV a vivienda");
            System.out.println("4. Aplicar actualización anual (IRAV)");
            System.out.println("5. Registrar depósito de fianza (ICAVI)");
            System.out.println("6. Ver fianzas pendientes de depósito");
            System.out.println("7. Dashboard de cumplimiento legal");
            System.out.println("8. Volver");
            System.out.println("-------------------------------------------");
            opcion = leerEntero("Seleccione una operación (1-8): ");
            switch (opcion) {
                case 1 -> formalizarContratoMenu();
                case 2 -> consultarContratosActivosMenu();
                case 3 -> asignarLimiteIravMenu();
                case 4 -> aplicarActualizacionIravMenu();
                case 5 -> registrarFianzaMenu();
                case 6 -> verFianzasPendientesMenu();
                case 7 -> dashboardCumplimientoLegal();
                case 8 -> System.out.println("Volviendo al menú del propietario...");
                default -> System.out.println("Error: Opción no válida.");
            }
        } while (opcion != 8);
    }

    private static void formalizarContratoMenu() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        List<Vivienda> disponibles = new ArrayList<>();
        for (Vivienda v : misViviendas) {
            if (v.getEstado() == EstadoVivienda.DISPONIBLE) {
                disponibles.add(v);
            }
        }
        
        if (disponibles.isEmpty()) {
            System.out.println("[!] No tienes viviendas disponibles para alquilar.");
            return;
        }
        
        System.out.println("\n--- SELECCIONAR VIVIENDA DISPONIBLE ---");
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println((i + 1) + ". " + disponibles.get(i).getDireccion() + " (Límite IRAV: " + disponibles.get(i).getLimiteMaximoIrav() + "€)");
        }
        int selV = leerEntero("Seleccione vivienda: ") - 1;
        if (selV < 0 || selV >= disponibles.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Vivienda vivienda = disponibles.get(selV);
        
        System.out.print("DNI del inquilino: ");
        String dniInq = sc.nextLine().trim();
        Usuario inq = gestor.obtenerUsuario(dniInq);
        if (inq == null || !(inq instanceof Inquilino)) {
            System.out.println("[!] Error: El DNI no corresponde a ningún inquilino registrado.");
            return;
        }
        
        System.out.println("Tipo de Arrendador:");
        System.out.println("1. FISICO (Persona física - Mínimo legal 5 años)");
        System.out.println("2. JURIDICO (Empresa - Mínimo legal 7 años)");
        int selTipo = leerEntero("Seleccione opción: ");
        TipoArrendador tipo = (selTipo == 2) ? TipoArrendador.JURIDICO : TipoArrendador.FISICO;
        
        double renta = leerDouble("Renta mensual acordada (EUR): ");
        int duracion = leerEntero("Duración del contrato (meses): ");
        
        double fianza = leerDouble("Importe de fianza (EUR) [Sugerido: " + renta + "€]: ");
        double garantias = leerDouble("Garantías adicionales (EUR) [Máx legal: " + (2 * renta) + "€]: ");

        System.out.print("Serie del contrato (un carácter, ej: A): ");
        String serieStr = sc.nextLine().trim();
        char serie = (serieStr.isEmpty()) ? 'A' : serieStr.charAt(0);
        
        ContratoAlquiler contrato = new ContratoAlquiler(vivienda.getDireccion(), dniInq, tipo, renta, duracion, serie, fianza, garantias);
        
        if (!contrato.cumpleDuracionMinimaLegal()) {
            System.out.println("[!] ERROR LEGAL: Duración insuficiente. La ley exige un mínimo de " +
                ((tipo == TipoArrendador.FISICO) ? "60" : "84") + " meses.");
            return;
        }

        if (!contrato.validarFianzaLegal()) {
            System.out.println("[!] ERROR LEGAL: El importe de la fianza debe ser exactamente 1 mes de renta (" + renta + "€) y las garantías adicionales no pueden superar las 2 mensualidades (" + (2 * renta) + "€).");
            return;
        }
        
        String res = gestor.formalizarContrato(contrato, vivienda);
        System.out.println(res);
    }

    private static void consultarContratosActivosMenu() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        System.out.println("\n========================================");
        System.out.println("          CONTRATOS DE ALQUILER");
        System.out.println("========================================");
        
        boolean hayContratos = false;
        for (Vivienda v : misViviendas) {
            ContratoAlquiler c = v.getContratoActivo();
            if (c != null) {
                hayContratos = true;
                Usuario inq = gestor.obtenerUsuario(c.getDniInquilino());
                String nombreInq = inq != null ? inq.getNombre() : "Desconocido";
                System.out.printf("Vivienda: %s\n", v.getDireccion());
                System.out.printf("  ID Contrato:  %s\n", c.getIdContrato());
                System.out.printf("  Inquilino:    %s (DNI: %s)\n", nombreInq, c.getDniInquilino());
                System.out.printf("  Renta:        %.2f€/mes\n", c.getRentaMensual());
                System.out.printf("  Fecha Firma:  %s | Vencimiento: %s\n", c.getFechaFirma(), c.getFechaVencimiento());
                System.out.printf("  Fianza Legal: %.2f€ (%s)\n", c.getImporteFianza(), c.isFianzaDepositada() ? "DEPOSITADA" : "PENDIENTE");
                System.out.printf("  Garantías:    %.2f€\n", c.getGarantiasAdicionales());
                System.out.printf("  Estado:       %s\n", c.getEstado());
                System.out.println("----------------------------------------");
            }
        }
        if (!hayContratos) {
            System.out.println("No tienes ningún contrato de alquiler activo.");
        }
        System.out.println("========================================");
    }

    private static void aplicarActualizacionIravMenu() {
        System.out.println("\n========================================");
        System.out.println("    ACTUALIZACIÓN ANUAL DE RENTA (IRAV)");
        System.out.println("========================================");
        System.out.println("Índice de referencia actual (IRAV mayo 2026: 2.47%)");
        double porcentaje = leerDouble("Introduzca el porcentaje de actualización (%): ");
        
        System.out.println("¿Desea forzar la actualización para pruebas?");
        System.out.println("1. No (Solo aplicar si es la fecha exacta del aniversario)");
        System.out.println("2. Sí (Aplicar a todos los contratos vigentes de inmediato)");
        int sel = leerEntero("Seleccione opción: ");
        boolean forzar = (sel == 2);
        
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        int actualizados = gestor.aplicarActualizacionAnualIRAV(dniProp, porcentaje, forzar);
        
        System.out.printf("[+] Se han actualizado %d contratos.\n", actualizados);
        System.out.println("========================================");
    }

    private static void dashboardCumplimientoLegal() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        int totalContratos = 0;
        int pendientesFianza = 0;
        int fueraPlazoFianza = 0;
        
        List<ContratoAlquiler> proximosVencer = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        
        for (Vivienda v : misViviendas) {
            ContratoAlquiler c = v.getContratoActivo();
            if (c != null && c.estaVigente()) {
                totalContratos++;
                if (!c.isFianzaDepositada()) {
                    pendientesFianza++;
                    if (c.estaPendienteDepositoIcavi()) {
                        fueraPlazoFianza++;
                    }
                }
                
                // Alerta de vencimiento (4 meses de antelación para el arrendador)
                LocalDate vto = c.getFechaVencimiento();
                if (vto != null) {
                    LocalDate limiteAviso = hoy.plusMonths(4);
                    if (vto.isEqual(hoy) || (vto.isAfter(hoy) && vto.isBefore(limiteAviso.plusDays(1)))) {
                        proximosVencer.add(c);
                    }
                }
            }
        }
        
        System.out.println("\n========================================");
        System.out.println("     DASHBOARD DE CUMPLIMIENTO LEGAL");
        System.out.println("========================================");
        System.out.printf("Total Contratos Activos:            %d\n", totalContratos);
        System.out.printf("Fianzas Pendientes de Depósito:    %d\n", pendientesFianza);
        System.out.printf("  -> Fuera de plazo legal (>30d):   %d\n", fueraPlazoFianza);
        System.out.printf("Contratos Próximos a Vencer (<4m):  %d\n", proximosVencer.size());
        System.out.println("----------------------------------------");
        
        if (fueraPlazoFianza > 0) {
            System.out.println("[⚠️ ALERTA DE COMPLIANCE]: Tienes fianzas fuera de plazo legal.");
            for (Vivienda v : misViviendas) {
                ContratoAlquiler c = v.getContratoActivo();
                if (c != null && !c.isFianzaDepositada() && c.estaPendienteDepositoIcavi()) {
                    System.out.printf("  - Contrato: %s (Vivienda: %s). Límite de depósito expiró el %s.\n",
                        c.getIdContrato(), v.getDireccion(), c.getPlazoLimiteDeposito());
                }
            }
            System.out.println("----------------------------------------");
        }
        
        if (!proximosVencer.isEmpty()) {
            System.out.println("[⚠️ ALERTA DE VENCIMIENTO]: Contratos próximos a vencer/renovar:");
            for (ContratoAlquiler c : proximosVencer) {
                long mesesRestantes = java.time.temporal.ChronoUnit.MONTHS.between(hoy, c.getFechaVencimiento());
                System.out.printf("  - Contrato: %s (Vivienda: %s). Vence el: %s (en %d meses). Plazo preaviso: 4 meses.\n",
                    c.getIdContrato(), c.getDireccionVivienda(), c.getFechaVencimiento(), mesesRestantes);
            }
            System.out.println("----------------------------------------");
        }
        
        System.out.println("Normativa aplicada: LAU 2026 + Ley 12/2023");
        System.out.println("========================================");
    }

    private static void asignarLimiteIravMenu() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        if (misViviendas.isEmpty()) {
            System.out.println("[!] No tienes viviendas registradas a tu nombre.");
            return;
        }
        
        System.out.println("\n--- TUS VIVIENDAS ---");
        for (int i = 0; i < misViviendas.size(); i++) {
            Vivienda v = misViviendas.get(i);
            System.out.printf("%d. %s [Límite IRAV actual: %.2f€]\n", (i + 1), v.getDireccion(), v.getLimiteMaximoIrav());
        }
        int sel = leerEntero("Seleccione vivienda: ") - 1;
        if (sel < 0 || sel >= misViviendas.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Vivienda v = misViviendas.get(sel);
        double limite = leerDouble("Nuevo límite máximo IRAV (EUR): ");
        if (limite < 0) {
            System.out.println("[!] El límite no puede ser negativo.");
            return;
        }
        v.setLimiteMaximoIrav(limite);
        gestor.guardarDatos();
        System.out.println("Límite IRAV asignado correctamente.");
    }

    private static void registrarFianzaMenu() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        List<Vivienda> conPendiente = new ArrayList<>();
        for (Vivienda v : misViviendas) {
            if (v.getContratoActivo() != null && !v.getContratoActivo().isFianzaDepositada()) {
                conPendiente.add(v);
            }
        }
        
        if (conPendiente.isEmpty()) {
            System.out.println("No tienes contratos con fianza pendiente de depósito.");
            return;
        }
        
        System.out.println("\n--- CONTRATOS CON FIANZA PENDIENTE (ICAVI) ---");
        for (int i = 0; i < conPendiente.size(); i++) {
            Vivienda v = conPendiente.get(i);
            System.out.printf("%d. Vivienda: %s | Contrato: %s\n", (i + 1), v.getDireccion(), v.getContratoActivo().getIdContrato());
        }
        
        int sel = leerEntero("Seleccione contrato: ") - 1;
        if (sel < 0 || sel >= conPendiente.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        
        Vivienda v = conPendiente.get(sel);
        v.getContratoActivo().registrarDepositoFianza();
        gestor.guardarDatos();
        System.out.println("Fianza registrada en ICAVI correctamente.");
    }

    private static void verFianzasPendientesMenu() {
        String dniProp = gestor.getUsuarioIdentificado().getDni();
        List<Vivienda> misViviendas = gestor.getViviendasPorDueño(dniProp);
        
        List<Vivienda> conPendiente = new ArrayList<>();
        for (Vivienda v : misViviendas) {
            if (v.getContratoActivo() != null && !v.getContratoActivo().isFianzaDepositada()) {
                conPendiente.add(v);
            }
        }
        
        System.out.println("\n========================================");
        System.out.println("   FIANZAS PENDIENTES DE DEPÓSITO");
        System.out.println("========================================");
        if (conPendiente.isEmpty()) {
            System.out.println("No hay fianzas pendientes.");
            return;
        }
        
        for (Vivienda v : conPendiente) {
            ContratoAlquiler c = v.getContratoActivo();
            System.out.printf("Contrato: %s | Inquilino DNI: %s | Renta: %.2f€ | Vivienda: %s\n",
                c.getIdContrato(), c.getDniInquilino(), c.getRentaMensual(), v.getDireccion());
        }
        System.out.println("========================================");
    }
}