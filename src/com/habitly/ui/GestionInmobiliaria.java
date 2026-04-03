package com.habitly.ui;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de usuario de Habitly.
 * @author DevNaranjo
 * @version V1.0.2 (Etapa 2 - Ampliación del Modelo)
 * @since 03-04-26
 */
public class GestionInmobiliaria {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    public static void main(String[] args) {
        // Inicialización del sistema: Carga de datos cifrados con AES
        try {
            gestor.cargarDatos();
        } catch (Exception e) {
            System.out.println("Aviso: Error de compatibilidad al cargar datos previos.");
            System.out.println("Se recomienda realizar un borrado de fábrica (Opción 6).");
        }

        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una operación (1-6): ");

            switch (opcion) {
                case 1 -> registrarVivienda();
                case 2 -> listarInventario();
                case 3 -> gestionarPago();
                case 4 -> aplicarIPCGeneral();
                case 5 -> {
                    System.out.println("Guardando cambios en el repositorio local cifrado...");
                    gestor.guardarDatos();
                    System.out.println("Sesión finalizada. ¡Hasta pronto!");
                }
                case 6 -> borrarDatosSistema();
                default -> System.out.println("Error: Opción no válida.");
            }
        } while (opcion != 5);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("    HABITLY - GESTIÓN INMOBILIARIA v1.0.2");
        System.out.println("========================================");
        System.out.println("1. Registrar nueva vivienda");
        System.out.println("2. Consultar inventario y ratios (€/m²)");
        System.out.println("3. Registrar cobro de mensualidad");
        System.out.println("4. Actualización masiva de precios (IPC)");
        System.out.println("5. Salir del programa");
        System.out.println("6. Borrar datos (Reseteado de fábrica)");
        System.out.println("----------------------------------------");
    }

    private static void registrarVivienda() {
        System.out.println("\n--- NUEVO REGISTRO DE VIVIENDA ---");

        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        double precioBase = leerDouble("Precio base mensual (€): ");
        double superficie = leerDouble("Superficie útil (m²): ");
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
            System.out.printf("   Detalles: %d hab, %d baños | %.1f m² | %s | %s%n",
                    v.getHabitaciones(), v.getBaños(), v.getSuperficie(), v.getConservacion(), garaje);
            System.out.printf("   Económico: Base: %.2f€ | Pendiente: %.2f€ | Ratio: %.2f€/m²%n",
                    v.getPrecioBase(), v.getPendienteDePago(), v.getPrecioMetroCuadrado());
            System.out.println("   --------------------------------------------------------");
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
            double cuota = leerDouble("Cantidad a abonar (€): ");
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

    // --- UTILIDADES DE ENTRADA ---

    private static boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) return false;
            System.out.println("Error: Responda con 'S' o 'N'.");
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
                if (valor <= 0) {
                    System.out.println("Error: El valor debe ser mayor a 0.");
                    continue;
                }
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Use un formato numérico válido.");
                sc.nextLine();
            }
        }
    }
}