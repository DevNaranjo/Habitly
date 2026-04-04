package com.habitly.data;

import com.habitly.model.Usuario;
import com.habitly.model.Vivienda;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Gestor de persistencia y lógica de colección para Habitly (v1.0.32).
 * Coordina la memoria RAM con el almacenamiento cifrado AES-128.
 * @author DevNaranjo
 * @version V1.0.32
 * @since 03-04-26
 */
public class GestorInventario {

    private ArrayList<Vivienda> inventario;
    private HashMap<String, Usuario> usuarios;

    public GestorInventario() {
        this.inventario = new ArrayList<>();
        this.usuarios = new HashMap<>();
        // El constructor no llama a cargarDatos() directamente para
        // dejar que Habitly.java controle el flujo del Setup Wizard.
    }

    // --- MÉTODOS DE PERSISTENCIA UNIFICADOS (v1.0.3) ---

    /**
     * Serializa el inventario y los usuarios, aplica cifrado AES-128
     * y guarda el resultado en el repositorio local.
     */
    public void guardarDatos() {
        try {
            // Empaquetamos todo en la "maleta"
            CajaFuerte maleta = new CajaFuerte(this.inventario, this.usuarios);

            // Ciframos y guardamos
            CryptoManager.guardarObjetoCifrado(maleta, "sistema.dat");
        } catch (Exception e) {
            System.err.println("Error crítico al cifrar los datos: " + e.getMessage());
        }
    }

    /**
     * Recupera y descifra el archivo de sistema, reconstruyendo
     * las estructuras de datos en memoria.
     */
    public void cargarDatos() {
        Object cargado = CryptoManager.leerObjetoCifrado("sistema.dat");

        if (cargado instanceof CajaFuerte maleta) {
            if (maleta.listaViviendas != null) this.inventario = maleta.listaViviendas;
            if (maleta.mapaUsuarios != null) this.usuarios = maleta.mapaUsuarios;
        }
        // Si no hay archivo o es null, las estructuras permanecen vacías (listas para el Setup Wizard)
    }

    /**
     * Borrado de fábrica: Limpia la memoria y elimina el archivo físico.
     */
    public boolean borrarDatosAplicacion() {
        this.inventario.clear();
        this.usuarios.clear();

        File archivo = new File("sistema.dat");
        if (archivo.exists()) {
            return archivo.delete();
        }
        return true;
    }

    // --- MÉTODOS DE LÓGICA DE USUARIOS ---

    public boolean añadirUsuario(Usuario nuevoUsuario) {
        if (nuevoUsuario == null) return false;

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
        return usuarios.remove(dni) != null;
    }

    /**
     * Retorna una lista segura de todos los usuarios registrados.
     * Vital para el Setup Wizard y el listado con Iterator.
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        if (usuarios == null) return new ArrayList<>();
        return new ArrayList<>(usuarios.values());
    }

    // --- MÉTODOS DE LÓGICA DE VIVIENDAS ---

    public void añadirVivienda(Vivienda v) {
        if (v != null) inventario.add(v);
    }

    public boolean estaVacio() {
        return inventario.isEmpty();
    }

    public int tamañoInventario() {
        return inventario.size();
    }

    public Vivienda obtenerVivienda(int indice) {
        if (indice >= 0 && indice < inventario.size()) {
            return inventario.get(indice);
        }
        return null;
    }

    public ArrayList<Vivienda> getInventario() {
        return inventario;
    }
}