package com.habitly.data;

import com.habitly.model.Vivienda;
import com.habitly.model.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Estructura de empaquetado para la persistencia cifrada.
 * Permite agrupar las colecciones de usuarios e inventario en un único objeto serializable.
 * * @author DevNaranjo
 * @version 1.0.34
 * @since 1.0.3
 */

public class CajaFuerte implements Serializable {
    //ID de versión estático
    private static final long serialVersionUID = 1L;

    public ArrayList<Vivienda> listaViviendas;
    public HashMap<String, Usuario> mapaUsuarios;

    public CajaFuerte(ArrayList<Vivienda> viviendas, HashMap<String, Usuario> usuarios) {
        this.listaViviendas = viviendas;
        this.mapaUsuarios = usuarios;
    }
}