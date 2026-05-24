package com.habitly.services;

import com.habitly.data.GestorInventario;
import com.habitly.model.*;
import java.util.List;

/**
 * Servicio de aplicación para la gestión de usuarios.
 * Separa la lógica de negocio de la interfaz de usuario por consola.
 */
public class UsuarioService {
    private final GestorInventario gestor;

    public UsuarioService(GestorInventario gestor) {
        this.gestor = gestor;
    }

    /**
     * Registra un nuevo Propietario en el sistema si el DNI no existe.
     * @return true si se registró con éxito.
     */
    public boolean registrarPropietario(String dni, String nombre, String tlf, String email, boolean esEmpresa, String password) {
        if (gestor.obtenerUsuario(dni) != null) {
            return false;
        }
        String saltHex = "";
        String hashHex = "";
        try {
            byte[] salt = com.habitly.data.CryptoManager.generarSalt();
            saltHex = com.habitly.data.CryptoManager.bytesToHex(salt);
            hashHex = com.habitly.data.CryptoManager.hashPassword(password, salt);
        } catch (Exception e) {
            System.err.println("Error al generar hash de contraseña: " + e.getMessage());
            return false;
        }
        Propietario p = new Propietario(dni, nombre, tlf, email, esEmpresa, hashHex, saltHex);
        boolean success = gestor.añadirUsuario(p);
        if (success) {
            if (gestor.getUsuarioIdentificado() == null) {
                gestor.setUsuarioIdentificado(p);
            }
            gestor.guardarDatos();
        }
        return success;
    }

    /**
     * Registra un nuevo Inquilino en el sistema si el DNI no existe.
     * @return true si se registró con éxito.
     */
    public boolean registrarInquilino(String dni, String nombre, String tlf, String email, int solvencia, String password) {
        if (gestor.obtenerUsuario(dni) != null) {
            return false;
        }
        String saltHex = "";
        String hashHex = "";
        try {
            byte[] salt = com.habitly.data.CryptoManager.generarSalt();
            saltHex = com.habitly.data.CryptoManager.bytesToHex(salt);
            hashHex = com.habitly.data.CryptoManager.hashPassword(password, salt);
        } catch (Exception e) {
            System.err.println("Error al generar hash de contraseña: " + e.getMessage());
            return false;
        }
        Inquilino i = new Inquilino(dni, nombre, tlf, email, solvencia, hashHex, saltHex);
        boolean success = gestor.añadirUsuario(i);
        if (success) {
            if (gestor.getUsuarioIdentificado() == null) {
                gestor.setUsuarioIdentificado(i);
            }
            gestor.guardarDatos();
        }
        return success;
    }

    /**
     * Elimina un usuario del sistema mediante su DNI, previniendo el autoborrado.
     * @return true si se eliminó con éxito.
     */
    public boolean eliminarUsuario(String dni) {
        Usuario actual = gestor.getUsuarioIdentificado();
        if (actual != null && dni.equals(actual.getDni())) {
            return false; // No se puede eliminar a sí mismo
        }
        boolean success = gestor.eliminarUsuario(dni);
        if (success) {
            gestor.guardarDatos();
        }
        return success;
    }

    public Usuario obtenerUsuario(String dni) {
        return gestor.obtenerUsuario(dni);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return gestor.obtenerTodosLosUsuarios();
    }
}
