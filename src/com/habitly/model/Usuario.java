package com.habitly.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase base abstracta para todos los usuarios de Habitly.
 * Implementa Serializable para permitir el guardado cifrado.
 */
public abstract class Usuario implements Serializable {

    //Atributos específicos de la clase
    private String dni; //PK 4 HashMap
    private String nombre;
    private int telefono;
    private String email;
    private LocalDate fechaRegistro = LocalDate.now();

    public Usuario(String dni, String nombre, int telefono, String email)
    {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
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
    public int getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }

    // --- SETTERS ---
    public void setTelefono(int telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
}

