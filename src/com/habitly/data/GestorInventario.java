package com.habitly.data;

import com.habitly.config.AppConfig;
import com.habitly.model.Vivienda;
import java.io.*;
import java.nio.file.Files;
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

    // --- MÉTODOS DE PERSISTENCIA UNIFICADOS (v1.0.1A) ---

    /**
     * Serializa el inventario, aplica cifrado AES-128 mediante CryptoManager
     * y guarda el resultado en la ruta de sistema definida en AppConfig.
     */
    public void guardarDatos() {
        AppConfig.asegurarDirectorio();
        String rutaSegura = AppConfig.getFullFilePath();

        try {
            // 1. Convertimos el ArrayList en un array de bytes (en memoria)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(this.inventario);
            }
            byte[] datosPlano = baos.toByteArray();

            // 2. Ciframos los bytes con nuestra "llave fantasma"
            byte[] datosCifrados = CryptoManager.cifrar(datosPlano);

            // 3. Guardamos los bytes cifrados en el archivo físico
            try (FileOutputStream fos = new FileOutputStream(rutaSegura)) {
                fos.write(datosCifrados);
            }

            System.out.println("[SISTEMA] Datos cifrados y blindados en: " + rutaSegura);
        } catch (Exception e) {
            System.err.println("Error crítico en el blindaje de datos: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo cifrado, aplica descifrado AES-128 y reconstruye
     * el inventario de viviendas en memoria.
     */
    public void cargarDatos() {
        String rutaSegura = AppConfig.getFullFilePath();
        File archivo = new File(rutaSegura);

        if (!archivo.exists()) {
            this.inventario = new ArrayList<>();
            return;
        }

        try {
            // 1. Leemos los bytes cifrados del archivo
            byte[] datosCifrados = Files.readAllBytes(archivo.toPath());

            // 2. Desciframos los bytes
            byte[] datosPlano = CryptoManager.descifrar(datosCifrados);

            // 3. Convertimos los bytes de vuelta a objetos (Deserialización)
            ByteArrayInputStream bais = new ByteArrayInputStream(datosPlano);
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                this.inventario = (ArrayList<Vivienda>) ois.readObject();
            }

            System.out.println("[SISTEMA] Datos descifrados y cargados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al descifrar datos (¿Llave incorrecta?): " + e.getMessage());
            this.inventario = new ArrayList<>();
        }
    }

    public void guardarInventario(ArrayList<Vivienda> lista) {
        //Nos aseguramos de que la ruta externa existe
        AppConfig.asegurarDirectorio();

        //Usamos la ruta dinámica de AppConfig
        String rutaFinal = AppConfig.getFullFilePath();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaFinal))) {
            oos.writeObject(lista);
            System.out.println("Datos guardados correctamente en el sistema.");
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Realiza una limpieza integral de los datos de la aplicación.
     * Elimina el archivo cifrado del disco y vacía la lista en memoria.
     * * @return true si el archivo fue eliminado exitosamente o no existía;
     * false si hubo un error de permisos o E/S.
     */
    public boolean borrarDatosAplicacion() {
        //Vaciar la lista en la memoria RAM
        this.inventario.clear();

        //Obtener la ruta del archivo físico
        File archivo = new File(AppConfig.getFullFilePath());

        //Si el archivo existe, intentar borrarlo
        if (archivo.exists()) {
            return archivo.delete();
        }

        // Si no existe, consideramos que la limpieza ya está hecha
        return true;
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