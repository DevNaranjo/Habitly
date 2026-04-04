package com.habitly.ui;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de usuario de Habitly.
 * Coordina la interacción entre el usuario y el sistema de persistencia cifrado.
 * * @author DevNaranjo
 * @version V1.0.3 (Etapa 3 - Gestión de Usuarios y Cifrado)
 * @since 03-04-26
 */
public class Habitly {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    /**
     * Punto de entrada principal de la aplicación.
     * Gestiona el bucle de ejecución y la carga inicial de datos.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Inicialización del sistema: Carga de datos cifrados con AES
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("Aviso: Error de compatibilidad al cargar datos previos.");
            System.out.println("Se recomienda realizar un borrado de fábrica (Opción 7).");
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
        } while (opcion != 6);

        sc.close();
    }

    /**
     * Muestra el menú principal de la aplicación por consola.
     */
    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("    HABITLY - GESTIÓN INMOBILIARIA v1.0.3");
        System.out.println("========================================");
        System.out.println("1. Gestionar usuarios (Propietarios/Inquilinos)");
        System.out.println("2. Registrar nueva vivienda");
        System.out.println("3. Consultar inventario y ratios (EUR/m2)");
        System.out.println("4. Registrar cobro de mensualidad");
        System.out.println("5. Actualización de precios (IPC)");
        System.out.println("6. Salir del programa");
        System.out.println("7. Borrar datos (Reseteado de fábrica)");
        System.out.println("----------------------------------------");
    }

    // --- MÉTODOS PARA LAS VIVIENDAS ---

    /**
     * Captura los datos necesarios para registrar una nueva vivienda (Piso o Casa).
     * Tras el registro, persiste los datos automáticamente.
     */
    private static void registrarVivienda() {
        System.out.println("\n--- NUEVO REGISTRO DE VIVIENDA ---");

        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        double precioBase = leerDouble("Precio base mensual (EUR): ");
        double superficie = leerDouble("Superficie útil (m2): ");
        int habitaciones = leerEntero("Número de habitaciones: ");
        int baños = leerEntero("Número de baños: ");
        boolean tieneGaraje = leerBoolean("¿Tiene garaje? (S/N): ");

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
                    baños, tieneGaraje, conservacion, planta, puerta));
        } else {
            double metrosParcela = leerDouble("Metros de parcela/terreno: ");

            gestor.añadirVivienda(new Casa(direccion, precioBase, superficie, habitaciones,
                    baños, tieneGaraje, conservacion, metrosParcela));
        }

        gestor.guardarDatos();
        System.out.println("Registro completado y datos cifrados.");
    }

    /**
     * Muestra por consola la lista detallada de todas las viviendas registradas.
     */
    private static void listarInventario() {
        if (gestor.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n--- INVENTARIO DETALLADO ---");
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);

            String tipo = (v instanceof Piso) ? "Piso" : "Casa";
            String garaje = v.isTieneGaraje() ? "Con Garaje" : "Sin Garaje";

            System.out.printf("%d. [%s] [%s] %s%n", (i + 1), v.getEstado(), tipo, v.getDireccion());
            System.out.printf("   Detalles: %d hab, %d baños | %.1f m2 | %s | %s%n",
                    v.getHabitaciones(), v.getBaños(), v.getSuperficie(), v.getConservacion(), garaje);
            System.out.printf("   Económico: Base: %.2f EUR | Pendiente: %.2f EUR | Ratio: %.2f EUR/m2%n",
                    v.getPrecioBase(), v.getPendienteDePago(), v.getPrecioMetroCuadrado());
            System.out.println("   --------------------------------------------------------");
        }
    }

    /**
     * Permite registrar el pago de una mensualidad sobre una vivienda específica.
     */
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

    /**
     * Aplica un incremento porcentual a todas las viviendas del inventario.
     */
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

    /**
     * Elimina el archivo de datos local y vacía las estructuras de memoria.
     */
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

    // --- MÉTODOS PARA LOS USUARIOS ---

    /**
     * Gestiona el submenú de administración de usuarios.
     */
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

    /**
     * Muestra el menú de gestión de usuarios.
     */
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

    /**
     * Captura datos y registra un objeto Propietario en el sistema.
     * Permite distinguir entre propietarios particulares y empresas.
     */
    public static void registrarPropietario() {
        System.out.println("\n--- NUEVO REGISTRO DE PROPIETARIO ---");
        System.out.print("DNI/CIF: ");
        String dni = sc.nextLine();

        // Validación de duplicados en el Gestor
        if (gestor.obtenerUsuario(dni) != null) {
            System.out.println("Error: Ya existe un usuario registrado con este DNI.");
            return;
        }

        //Pedir datos
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        int telefono = leerEntero("Teléfono de contacto: ");
        System.out.print("E-mail: ");
        String email = sc.nextLine();

        // Validación de tipo de propietario
        System.out.println("\n--- TIPO DE TITULARIDAD ---");
        boolean esEmpresa = leerBoolean("¿El propietario es una empresa/sociedad? (S/N): ");

        //Añadir el propietario
        Propietario p = new Propietario(dni, nombre, telefono, email, esEmpresa);
        if (gestor.añadirUsuario(p)) {
            gestor.guardarDatos();
            System.out.println("Propietario registrado con éxito.");
        }
    }

    /**
     * Captura datos y registra un objeto Inquilino en el sistema, incluyendo solvencia.
     */
    public static void registrarInquilino() {
        System.out.println("\n--- NUEVO REGISTRO DE INQUILINO ---");
        System.out.print("DNI: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) != null) {
            System.out.println("Error: Ya existe un usuario registrado con ese DNI.");
            return;
        }

        //Pedir datos
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        int telefono = leerEntero("Teléfono de contacto: ");
        System.out.print("E-mail: ");
        String email = sc.nextLine();

        System.out.println("\n--- EVALUACIÓN DE RIESGO FINANCIERO ---");
        System.out.println("Guía de Solvencia Habitly:");
        System.out.println(" [ 0 - 40 ] Riesgo Alto (Sin avales / Ingresos bajos)");
        System.out.println(" [ 41 - 70] Riesgo Medio (Solvencia estándar / Avales)");
        System.out.println(" [ 71 - 100] Riesgo Bajo (Funcionario / Perfil Premium)");

        // Validación de rango (0-100)
        int solvencia;
        while (true) {
            solvencia = leerEntero("Introduzca el índice calculado (0-100): ");
            if (solvencia >= 0 && solvencia <= 100) {
                break;
            }
            System.out.println("Error: El índice debe estar entre 0 y 100.");
        }

        //Añadir el inquilino
        Inquilino i = new Inquilino(dni, nombre, telefono, email, solvencia);
        if (gestor.añadirUsuario(i)) {
            gestor.guardarDatos();
            System.out.println("Inquilino registrado con éxito.");
        }
    }


    /**
     * Lista todos los usuarios usando el patrón Iterator.
     * Este método demuestra cómo recorrer una colección de forma segura y profesional.
     */
    private static void listarUsuarios() {
        // Obtenemos la lista de usuarios del gestor
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

            // Lógica polimórfica para los detalles
            if (u instanceof Inquilino inq) {
                detalles = "Solvencia: " + inq.getSolvencia() + "%";
            } else if (u instanceof Propietario prop) {
                detalles = prop.isEsEmpresa() ? "Empresa/CIF" : "Particular";
            }

            System.out.printf("%-12s | %-20s | %-12s | %-15s%n",
                    u.getDni(),
                    u.getNombre(),
                    perfil,
                    detalles);
        }
        System.out.println("====================================================================");
    }

    /**
     * Busca un usuario por DNI y muestra su información detallada.
     * Utiliza polimorfismo para identificar el tipo de usuario.
     */
    public static void buscarUsuario() {
        System.out.println("\n--- BUSCADOR DE USUARIO ---");
        System.out.print("Introduce el DNI del usuario a buscar: ");
        String dni = sc.nextLine();

        Usuario u = gestor.obtenerUsuario(dni);
        if (u != null) {
            String tipo = (u instanceof Propietario) ? "PROPIETARIO" : "INQUILINO";
            System.out.println("----------------------------------------");
            System.out.println("Tipo: " + tipo);
            System.out.println("Nombre: " + u.getNombre());
            System.out.println("DNI: " + u.getDni());
            System.out.println("Email: " + u.getEmail());
            System.out.println("Teléfono: " + u.getTelefono());

            if (u instanceof Inquilino inq) {
                System.out.println("Solvencia: " + inq.getSolvencia() + "/100");
            } else if (u instanceof Propietario prop) {
                System.out.println("Es Empresa: " + (prop.isEsEmpresa() ? "SÍ" : "NO"));
            }
            System.out.println("----------------------------------------");
        } else {
            System.out.println("Error: No se encontró ningún usuario con ese DNI.");
        }
    }

    /**
     * Elimina a un usuario del sistema tras confirmación por DNI.
     */
    public static void eliminarUsuario() {
        System.out.println("\n--- ELIMINADOR DE USUARIO ---");
        System.out.print("Introduce el DNI del usuario que quiera eliminar: ");
        String dni = sc.nextLine();

        if (gestor.obtenerUsuario(dni) == null) {
            System.out.println("Error: El usuario no existe.");
            return;
        }

        System.out.print("¿Confirmar borrado de usuario? (Escriba 'SI'): ");
        String confirmacion = sc.nextLine().toUpperCase();

        if (confirmacion.equals("SI")) {
            if (gestor.eliminarUsuario(dni)) {
                gestor.guardarDatos();
                System.out.println("Usuario eliminado correctamente.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // --- UTILIDADES DE ENTRADA ---

    /**
     * Lee una entrada booleana basada en caracteres S o N.
     * @param mensaje Texto a mostrar al usuario.
     * @return true si la entrada es S, false si es N.
     */
    private static boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) return false;
            System.out.println("Error: Responda con 'S' o 'N'.");
        }
    }

    /**
     * Lee un número entero de la consola con validación.
     * @param mensaje Texto a mostrar al usuario.
     * @return El número entero introducido.
     */
    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = sc.nextInt();
                sc.nextLine(); // Limpiar buffer
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Introduzca un número entero.");
                sc.nextLine(); // Limpiar buffer en caso de error
            }
        }
    }

    /**
     * Lee un número decimal de la consola con validación de valor positivo.
     * @param mensaje Texto a mostrar al usuario.
     * @return El número decimal introducido.
     */
    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = sc.nextDouble();
                sc.nextLine(); // Limpiar buffer
                if (valor <= 0) {
                    System.out.println("Error: El valor debe ser mayor a 0.");
                    continue;
                }
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Use un formato numérico válido.");
                sc.nextLine(); // Limpiar buffer en caso de error
            }
        }
    }
}