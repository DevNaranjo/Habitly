package com.habitly.test;

import com.habitly.data.GestorInventario;
import java.io.File;

public class HabitlyPersistenciaTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTS DE PERSISTENCIA ===");
        GestorInventario gestor = new GestorInventario();

        // Forzamos un guardado
        gestor.guardarDatos();

        // Comprobamos la ruta que definiste: data/sistema.dat
        File carpeta = new File("data");
        File archivo = new File("data" + File.separator + "sistema.dat");

        if (carpeta.exists() && carpeta.isDirectory()) {
            System.out.println("[OK] Carpeta /data creada correctamente.");
        } else {
            System.out.println("[ERROR] La carpeta /data no existe.");
        }

        if (archivo.exists()) {
            System.out.println("[OK] Archivo sistema.dat encontrado en la nueva ruta.");
        } else {
            System.out.println("[ERROR] No se encuentra el archivo de datos.");
        }
    }
}