package com.habitly.data;

import com.habitly.config.AppConfig;
import com.habitly.model.Usuario;
import com.habitly.model.Vivienda;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Gestor de persistencia y lógica de colección para Habitly (v1.0).
 * @author DevNaranjo
 * @version V1.0.3
 * @since 02-04-26
 */
public class GestorInventario {

    private ArrayList<Vivienda> inventario;
    private HashMap<String, Usuario> usuarios;

    public GestorInventario() {
        this.inventario = new ArrayList<>();
        this.usuarios = new HashMap<>();

        //Intentamos rellenar el inventario y los usuarios con lo que haya en el archivo cifrado
        cargarDatos();
    }

    // --- MÉTODOS DE PERSISTENCIA UNIFICADOS (v1.0.3) ---

    /**
     * Serializa el inventario y los usuarios, aplica cifrado AES-128
     * mediante CryptoManager y guarda el resultado.
     */
    public void guardarDatos() {
        // Empaquetamos todo en la "maleta"
        CajaFuerte maleta = new CajaFuerte(this.inventario, this.usuarios);

        // La pasamos al CryptoManager para que la cifre y la guarde en disco
        // Usamos el nombre de archivo unificado
        CryptoManager.guardarObjetoCifrado(maleta, "sistema.dat");
        System.out.println("Sistema cifrado y guardado correctamente.");
    }

    /**
     * Lee el archivo cifrado, aplica descifrado AES-128 y reconstruye
     * tanto el inventario como el mapa de usuarios en memoria.
     */
    public void cargarDatos() {
        Object cargado = CryptoManager.leerObjetoCifrado("sistema.dat");

        if (cargado instanceof CajaFuerte maleta) {
            // Desempaquetamos y recuperamos nuestras estructuras
            this.inventario = maleta.listaViviendas;
            this.usuarios = maleta.mapaUsuarios;
            System.out.println("Datos recuperados con éxito.");
        } else {
            // Si el archivo no existe, las estructuras ya están vacías por el constructor
            System.out.println("No se detectó archivo previo. Iniciando sistema limpio.");
        }
    }

    /**
     * Realiza una limpieza integral de los datos de la aplicación.
     * Elimina el archivo cifrado del disco y vacía las listas en memoria.
     * @return true si el archivo fue eliminado exitosamente; false si hubo error.
     */
    public boolean borrarDatosAplicacion() {
        // Vaciar la memoria RAM
        this.inventario.clear();
        this.usuarios.clear();

        // Borrar el archivo físico
        File archivo = new File("sistema.dat");
        if (archivo.exists()) {
            return archivo.delete();
        }
        return true;
    }

    // --- MÉTODOS DE LÓGICA DE USUARIOS ---

    /**
     * Registra un nuevoUsuario en el sistema utilizando su DNI como identificador único.
     */
    public boolean añadirUsuario(Usuario nuevoUsuario) {
        String dni = nuevoUsuario.getDni();
        if (usuarios.containsKey(dni)) {
            return false;
        } else {
            usuarios.put(dni, nuevoUsuario);
            return true;
        }
    }

    public Usuario obtenerUsuario(String dni) {
        return usuarios.get(dni);
    }

    public boolean eliminarUsuario(String dni) {
        if (usuarios.containsKey(dni)) {
            usuarios.remove(dni);
            return true;
        }
        return false;
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        // Esto asume que usas un HashMap o ArrayList para usuarios
        return new ArrayList<>(usuarios.values());
    }

    // --- MÉTODOS DE LÓGICA DE VIVIENDAS ---

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

    public ArrayList<Vivienda> getInventario() {
        return inventario;
    }
}