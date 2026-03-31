package com.habitly.ui;

import com.habitly.model.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de usuario de Habitly.
 * @author DevNaranjo
 * @version BETA
 * @since 29-03-26
 */
public class GestionInmobiliaria {

    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Vivienda> inventario = new ArrayList<>();

    public static void main(String[] args) {
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
                default -> System.out.println("Opción no válida (1-5).");
            }
        } while (opcion != 5);
        sc.close();
    }

    // --- MÉTODOS DE GESTIÓN ---

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ GESTIÓN INMOBILIARIA ---");
        System.out.println("1. Registrar Vivienda");
        System.out.println("2. Listar Inventario");
        System.out.println("3. Registrar Pago");
        System.out.println("4. Aplicar IPC");
        System.out.println("5. Salir");
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
            inventario.add(new Piso(direccion, precioBase, planta, puerta));
        } else {
            // Si sale del bucle y no es 1, obligatoriamente es 2
            double metros = leerDouble("¿Metros de la parcela? ");
            inventario.add(new Casa(direccion, precioBase, metros));
        }
        System.out.println("Vivienda registrada con éxito.");
    }

    private static void listarInventario() {
        if (inventario.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n--- LISTADO DE VIVIENDAS ---");
        for (int i = 0; i < inventario.size(); i++) {
            Vivienda v = inventario.get(i);

            String etiquetaTipo = "Vivienda";
            if (v instanceof Piso) {
                etiquetaTipo = "Piso";
            } else if (v instanceof Casa) {
                etiquetaTipo = "Casa";
            }

            //Resultado: Índice. [Tipo] Dirección | Precio: XX.XX € (Pendiente (IGIC inc.): XX.XX €)
            System.out.printf("%d. [%s] %s | Base: %.2f € | Pendiente (IGIC inc.): %.2f €%n",
                    (i + 1), etiquetaTipo, v.getDireccion(), v.getPrecioBase(), v.getPendienteDePago());
        }
    }

    private static void gestionarPago() {
        if (inventario.isEmpty()) {
            System.out.println("No hay viviendas registradas para realizar cobros.");
            return;
        }

        listarInventario();
        int indice = leerEntero("Seleccione el número de vivienda para el pago: ") - 1;

        if (indice >= 0 && indice < inventario.size()) {
            Vivienda elegida = inventario.get(indice);
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
        } else {
            System.out.println("Error: Índice de vivienda no válido.");
        }
    }

    private static void aplicarIPCGeneral() {
        if (inventario.isEmpty()) {
            System.out.println("No hay viviendas para actualizar.");
            return;
        }

        double porcentaje = leerDouble("Introduzca el porcentaje de incremento (IPC): ");
        for (Vivienda v : inventario) {
            v.aplicarSubidaAnual(porcentaje);
        }
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

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = sc.nextDouble();
                sc.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Introduzca un valor numérico (use coma para decimales).");
                sc.nextLine();
            }
        }
    }
}
