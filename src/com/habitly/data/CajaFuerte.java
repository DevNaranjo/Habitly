package com.habitly.data;

import com.habitly.model.Vivienda;
import com.habitly.model.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contenedor único para la persistencia del sistema.
 * Agrupa viviendas y usuarios para un cifrado unificado.
 */
public class CajaFuerte implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<Vivienda> listaViviendas;
    public HashMap<String, Usuario> mapaUsuarios;

    public CajaFuerte(ArrayList<Vivienda> viviendas, HashMap<String, Usuario> usuarios) {
        this.listaViviendas = viviendas;
        this.mapaUsuarios = usuarios;
    }
}