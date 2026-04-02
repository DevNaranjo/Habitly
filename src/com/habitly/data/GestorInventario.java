package com.habitly.data;

import com.habitly.config.AppConfig;
import com.habitly.model.Vivienda;
import java.io.*;
import java.util.ArrayList;

/**
 * Gestor de persistencia y lógica de colección para Habitly (v1.0).
 * @author DevNaranjo
 *  @version V1.0
 *  @since 02-04-26
 */
public class GestorInventario {

    private ArrayList<Vivienda> inventario;

    public GestorInventario() {
        this.inventario = new ArrayList<>();
    }

    // --- MÉTODOS DE PERSISTENCIA (Guardar y Cargar datos) ---

    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AppConfig.NOMBRE_ARCHIVO))) {
            oos.writeObject(inventario);
            System.out.println("[SISTEMA] Datos guardados correctamente en " + AppConfig.NOMBRE_ARCHIVO);
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    public void cargarDatos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(AppConfig.NOMBRE_ARCHIVO))) {
            inventario = (ArrayList<Vivienda>) ois.readObject();
            System.out.println("[SISTEMA] Datos cargados con éxito.");
        } catch (FileNotFoundException e) {
            System.out.println("[SISTEMA] No hay datos previos, iniciando inventario vacío.");
            inventario = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            inventario = new ArrayList<>();
        }
    }

    // --- MÉTODOS PUENTE ---

    public void añadirVivienda(Vivienda v) {
        inventario.add(v);
    }

    public boolean estaVacio() {
        return inventario.isEmpty();
    }

    public int tamañoInventario() {
        return inventario.size();
    }

    public Vivienda obtenerVivienda(int indice) {
        return inventario.get(indice);
    }
}