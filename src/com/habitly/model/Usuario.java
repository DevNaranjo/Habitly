package com.habitly.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase abstracta que define la estructura base de un usuario en Habitly.
 * Proporciona los atributos comunes de identificación y contacto.
 * * @author DevNaranjo
 * @version 1.0.7-F
 * @since 1.0.3
 */

public abstract class Usuario implements Serializable {

    //ID de versión estático
    private static final long serialVersionUID = 1L;

    //Atributos específicos de la clase
    private String dni; //PK 4 HashMap
    private String nombre;
    private String telefono;
    private String email;
    private LocalDate fechaRegistro = LocalDate.now();
    private String passwordHash;
    private String passwordSalt;

    public Usuario(String dni, String nombre, String telefono, String email, String passwordHash, String passwordSalt)
    {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

// --- MÉTODOS ABSTRACTOS ---
    /**
     * Define el rol del usuario en el sistema.
     * @return "PROPIETARIO" o "INQUILINO"
     */
    public abstract String getTipoUsuario();

    // --- GETTERS ---
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public String getPasswordHash() { return passwordHash; }
    public String getPasswordSalt() { return passwordSalt; }

    // --- SETTERS ---
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }
}

