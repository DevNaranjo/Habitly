package com.habitly.model;

/**
 * Representa a un usuario con rol de arrendatario.
 * Incluye atributos de calificación crediticia y solvencia para el filtrado de alquileres.
 * * @author DevNaranjo
 * @version 1.0.7-F
 * @since 1.0.3
 */

public class Inquilino extends Usuario {

    //ID de versión estático
    private static final long serialVersionUID = 1L;

    // Atributo específico: Puntuación de solvencia (0-100)
    private int solvencia;

    public Inquilino(String dni, String nombre, String telefono, String email, int solvencia, String passwordHash, String passwordSalt) {
        super(dni, nombre, telefono, email, passwordHash, passwordSalt);
        this.solvencia = comprobarSolvencia(solvencia);
    }

    @Override
    public String getTipoUsuario() {
        return "INQUILINO";
    }

    // --- GETTER ---
    public int getSolvencia(){ return solvencia;}

    // --- SETTER ---
    public void setSolvencia(int nuevaSolvencia){
        this.solvencia = comprobarSolvencia(nuevaSolvencia);
    }

    //--- MÉTODOS COMPROBACIÓN ---
    /**
     * Asegura que el valor esté en el rango [0-100].
     */
    private int comprobarSolvencia(int valor) {
        if (valor < 0) return 0;
        if (valor > 100) return 100;
        return valor;
    }
}