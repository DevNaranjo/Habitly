package com.habitly.model;

/**
 * Representa al residente de una vivienda.
 */
public class Inquilino extends Usuario {

    // Atributo específico: Puntuación de solvencia (0-100)
    private int solvencia;

    public Inquilino(String dni, String nombre, int telefono, String email, int solvencia) {
        super(dni, nombre, telefono, email);
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
        if (nuevaSolvencia < 0 || nuevaSolvencia > 100)
        {
            System.out.println("Por favor, elija una solvencia entre 0-100.");
        }
        this.solvencia = nuevaSolvencia;
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