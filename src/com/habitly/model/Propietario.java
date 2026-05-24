package com.habitly.model;

/**
 * Representa a un usuario con rol de dueño de inmuebles.
 * Gestiona la titularidad de las viviendas y su naturaleza jurídica (Persona/Empresa).
 * * @author DevNaranjo
 * @version 1.0.7-F
 * @since 1.0.3
 */

public class Propietario extends Usuario {

    private boolean esEmpresa;

    public Propietario(String dni, String nombre, String telefono, String email, boolean esEmpresa, String passwordHash, String passwordSalt) {
        super(dni, nombre, telefono, email, passwordHash, passwordSalt);
        this.esEmpresa = esEmpresa;
    }

    @Override
    public String getTipoUsuario() {
        return "PROPIETARIO";
    }

    // --- GETTER ---
    public boolean isEsEmpresa(){ return esEmpresa;}

    // --- SETTER ---
    public void setEsEmpresa(boolean esEmpresa){ this.esEmpresa = esEmpresa;}
}