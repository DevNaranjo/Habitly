package com.habitly.ui;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de usuario de Habitly.
 * @author DevNaranjo
 * @version V1.0.1A
 * @since 29-03-26
 */
public class GestionInmobiliaria {

    private static Scanner sc = new Scanner(System.in);
    private static GestorInventario gestor = new GestorInventario();

    public static void main(String[] args) {
        // Cargamos datos al iniciar el sistema (ahora desde ruta segura y con AES)
        gestor.cargarDatos();

        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ");

            switch (opcion) {
                case 1 -> registrarVivienda();
                case 2 -> listarInventario();
                case 3 -> gestionarPago();
                case 4 -> aplicarIPCGeneral();
                case 5 -> System.out.println("Saliendo del sistema de Gestión Inmobiliaria... ¡Hasta pronto!");
                case 6 -> borrarDatosSistema(); // Nueva funcionalidad v1.0.1A
                default -> System.out.println("Opción no válida (1-6).");
            }
        } while (opcion != 5);

        sc.close();
        // Guardado final de seguridad al salir
        gestor.guardarDatos();
    }

    // --- MÉTODOS DE GESTIÓN ---

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ GESTIÓN INMOBILIARIA ---");
        System.out.println("1. Registrar Vivienda");
        System.out.println("2. Listar Inventario");
        System.out.println("3. Registrar Pago");
        System.out.println("4. Aplicar IPC");
        System.out.println("5. Salir");
        System.out.println("6. Borrar datos de aplicación");
    }

    /**
     * Gestiona el proceso de borrado físico y lógico de los datos de la aplicación.
     * Incluye una confirmación explícita para evitar pérdidas accidentales.
     */
    private static void borrarDatosSistema() {
        System.out.println("\nADVERTENCIA: Esta acción eliminará permanentemente todos los datos.");
        System.out.print("¿Está seguro de que desea continuar? (Escriba 'SI' para confirmar): ");
        String confirmacion = sc.nextLine().toUpperCase();

        if (confirmacion.equals("SI")) {
            if (gestor.borrarDatosAplicacion()) {
                System.out.println("Todos los datos han sido eliminados correctamente del sistema.");
            } else {
                System.out.println("Error: No se pudo completar la eliminación de los archivos.");
            }
        } else {
            System.out.println("Operación cancelada. Sus datos están a salvo.");
        }
    }

    private static void registrarVivienda() {
        System.out.println("\nUsted ha elegido: Registrar vivienda.");
        System.out.print("¿Cuál es su dirección? ");
        String direccion = sc.nextLine();

        double precioBase = leerDouble("¿Cuál es el precio base? ");

        // Bucle de validación específica para el tipo de vivienda
        int tipo;
        do {
            tipo = leerEntero("¿Es un Piso (1) o una Casa (2)? ");
            if (tipo != 1 && tipo != 2) {
                System.out.println("Error: Por favor, introduzca 1 para Piso o 2 para Casa.");
            }
        } while (tipo != 1 && tipo != 2);

        if (tipo == 1) {
            int planta = leerEntero("Indique la planta: ");
            System.out.print("¿Cuál es la puerta? ");
            String puerta = sc.nextLine();
            // Usamos el gestor para añadir el nuevo Piso
            gestor.añadirVivienda(new Piso(direccion, precioBase, planta, puerta));
            gestor.guardarDatos();
        } else {
            // Si sale del bucle y no es 1, obligatoriamente es 2
            double metros = leerDouble("¿Metros de la parcela? ");
            // Usamos el gestor para añadir la nueva Casa
            gestor.añadirVivienda(new Casa(direccion, precioBase, metros));
            gestor.guardarDatos();
        }
        System.out.println("Vivienda registrada con éxito.");
    }

    private static void listarInventario() {
        if (gestor.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n--- LISTADO DE VIVIENDAS ---");
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            Vivienda v = gestor.obtenerVivienda(i);

            String etiquetaTipo = "Vivienda";
            if (v instanceof Piso) {
                etiquetaTipo = "Piso";
            } else if (v instanceof Casa) {
                etiquetaTipo = "Casa";
            }

            // Resultado: Índice. [Estado] [Tipo] Dirección | Base: XX.XX € | Pendiente (IGIC inc.): XX.XX €
            System.out.printf("%d. [%s] [%s] %s | Base: %.2f € | Pendiente (IGIC inc.): %.2f €%n",
                    (i + 1), v.getEstado(), etiquetaTipo, v.getDireccion(), v.getPrecioBase(), v.getPendienteDePago());
        }
    }

    private static void gestionarPago() {
        if (gestor.estaVacio()) {
            System.out.println("No hay viviendas registradas para realizar cobros.");
            return;
        }

        // Mostramos primero el inventario para que el usuario pueda ver las pendiente de pago
        listarInventario();
        int indice = leerEntero("Seleccione el número de vivienda para el pago: ") - 1;

        if (indice >= 0 && indice < gestor.tamañoInventario()) {
            Vivienda elegida = gestor.obtenerVivienda(indice);
            System.out.println("Vivienda seleccionada: " + elegida.getDireccion());
            System.out.printf("Pendiente actual: %.2f €%n", elegida.getPendienteDePago());

            double cuota = leerDouble("¿Cantidad a abonar? ");
            elegida.registrarPago(cuota);

            System.out.println("Pago registrado satisfactoriamente.");
            if (elegida.isPagadoCompleto()) {
                System.out.println("¡Mensualidad completada!");
            } else {
                System.out.printf("Pendiente restante: %.2f €%n", elegida.getPendienteDePago());
            }
            // Guardamos el pago realizado
            gestor.guardarDatos();
        } else {
            System.out.println("Error: Índice de vivienda no válido.");
        }
    }

    private static void aplicarIPCGeneral() {
        if (gestor.estaVacio()) {
            System.out.println("No hay viviendas para actualizar.");
            return;
        }

        double porcentaje = leerDouble("Introduzca el porcentaje de incremento (IPC): ");
        // Recorremos el inventario a través del gestor
        for (int i = 0; i < gestor.tamañoInventario(); i++) {
            gestor.obtenerVivienda(i).aplicarSubidaAnual(porcentaje);
        }
        // Guardamos el incremento ya aplicado en todas las viviendas
        gestor.guardarDatos();
        System.out.println("Precios actualizados en todo el inventario.");
    }

    // --- UTILIDADES DE ENTRADA ---
    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Introduzca un número entero válido.");
                sc.nextLine();
            }
        }
    }

    /**
     * Utilidad para leer números decimales de forma segura.
     * Valida que el dato sea numérico y que sea un valor positivo coherente para el negocio.
     * @param mensaje Texto que se muestra al usuario pidiendo el dato.
     * @return double Valor validado mayor que cero.
     */
    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = sc.nextDouble();
                sc.nextLine(); // Limpiar el buffer

                // --- VALIDACIÓN DE NEGOCIO v1.0 ---
                if (valor <= 0) {
                    System.out.println("Error: El valor debe ser mayor que 0. Inténtelo de nuevo.");
                    continue; // Vuelve al principio del bucle sin salir
                }

                return valor;

            } catch (Exception e) {
                System.out.println("Error: Introduzca un valor numérico (use coma para decimales).");
                sc.nextLine(); // Limpiar el buffer en caso de error de tipo
            }
        }
    }
}