package com.habitly.model;

/**
 * Representa al dueño de los inmuebles.
 */
public class Propietario extends Usuario {

    private boolean esEmpresa;

    public Propietario(String dni, String nombre, int telefono, String email, boolean esEmpresa) {
        super(dni, nombre, telefono, email);
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