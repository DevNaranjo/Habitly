package com.habitly.data;

import com.habitly.model.Usuario;
import com.habitly.model.Vivienda;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Motor central de lógica de negocio y persistencia.
 * Coordina las colecciones de datos en memoria con el almacenamiento seguro
 * en disco mediante cifrado AES.
 * * @author DevNaranjo
 * @version 1.0.34
 * @since 1.0.0
 */

public class GestorInventario {

    private ArrayList<Vivienda> inventario;
    private HashMap<String, Usuario> usuarios;

    // Atributo específico para gestionar la sesión activa (v1.0.33)
    private Usuario usuarioIdentificado;

    //Definimos la carpeta y la ruta usando el separador del sistema (v1.0.33)
    private final String CARPETA_DATA = "data";
    private final String RUTA_SISTEMA = CARPETA_DATA + File.separator + "sistema.dat";

    public GestorInventario() {
        this.inventario = new ArrayList<>();
        this.usuarios = new HashMap<>();
        this.usuarioIdentificado = null;
    }

// --- MÉTODOS DE PERSISTENCIA ACTUALIZADOS (v1.0.33) ---

    /**
     * Serializa el inventario y los usuarios, aplica cifrado AES-128
     * y guarda el resultado en el repositorio local dentro de /data.
     */
    public void guardarDatos() {
        try {
            File directorio = new File(CARPETA_DATA);

            // Creamos la carpeta si no existe antes de guardar
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Empaquetamos los datos en la clase contenedor
            CajaFuerte maleta = new CajaFuerte(this.inventario, this.usuarios);

            // Ciframos y guardamos usando la ruta multiplataforma
            CryptoManager.guardarObjetoCifrado(maleta, RUTA_SISTEMA);

        } catch (Exception e) {
            // Importante: No solo mostrar el mensaje, a veces conviene ver la traza
            System.err.println("Error crítico al cifrar los datos: " + e.getMessage());
        }
    }

    /**
     * Recupera y descifra el archivo de sistema desde /data.
     * Verifica existencia y tamaño para evitar excepciones de flujo.
     */
    public void cargarDatos() {
        File archivo = new File(RUTA_SISTEMA);

        // Verificación de robustez: ¿Existe y tiene contenido?
        if (archivo.exists() && archivo.length() > 0) {
            try {
                Object cargado = CryptoManager.leerObjetoCifrado(RUTA_SISTEMA);

                if (cargado instanceof CajaFuerte maleta) {
                    if (maleta.listaViviendas != null) this.inventario = maleta.listaViviendas;
                    if (maleta.mapaUsuarios != null) this.usuarios = maleta.mapaUsuarios;
                }
            } catch (Exception e) {
                System.err.println("[!] Error al descifrar el archivo de datos.");
            }
        } else {
            // No hay error, simplemente es la primera vez que se usa la app
            System.out.println("[Habitly] Iniciando sistema nuevo (Base de datos vacía).");
        }
    }

    /**
     * Borrado de fábrica: Limpia la memoria RAM y elimina el archivo físico en /data.
     * v1.0.34 Patch: Refuerzo de limpieza de sesión.
     * @return true si el archivo se eliminó o no existía, false si hubo un error de bloqueo.
     */
    public boolean borrarDatosAplicacion() {
        //Limpiamos la memoria RAM
        this.inventario.clear();
        this.usuarios.clear();
        this.usuarioIdentificado = null;

        //Intentamos borrar el archivo físico
        File archivo = new File(RUTA_SISTEMA);

        if (archivo.exists()) {
            return archivo.delete();
        }
        return true; // Si no existe, el objetivo se cumple.
    }

    // --- MÉTODOS DE LÓGICA DE USUARIOS ---

    /**
     * Registra un nuevo usuario validando que el DNI no esté duplicado.
     * @param nuevoUsuario Objeto usuario a añadir.
     * @return true si se añadió, false si el DNI ya existe.
     */
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
     * Establece el usuario que ha iniciado sesión en la ejecución actual.
     * @param usuarioIdentificado Usuario validado.
     */
    public void setUsuarioIdentificado(Usuario usuarioIdentificado) {
        this.usuarioIdentificado = usuarioIdentificado;
    }

    /**
     * Retorna una lista de todos los usuarios registrados.
     * @return List de usuarios (ArrayList).
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

    /**
     * Obtiene el usuario que tiene la sesión activa.
     * @return Usuario identificado o null si es invitado/no logueado.
     */
    public Usuario getUsuarioIdentificado() {
        return usuarioIdentificado;
    }

    /**
     * Busca un usuario por DNI dentro del mapa de registros.
     * @param dni Cadena con el DNI/CIF.
     * @return El usuario encontrado o null.
     */
    public Usuario buscarPorDni(String dni) {
        return usuarios.getOrDefault(dni, null);
    }
}