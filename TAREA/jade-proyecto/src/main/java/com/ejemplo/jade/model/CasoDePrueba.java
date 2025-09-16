package com.ejemplo.jade.model;

/**
 * Modelo de Caso de Prueba (resultado).
 */
public class CasoDePrueba {
    private final String id;
    private final String descripcion;

    public CasoDePrueba(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "CP[" + id + "]: " + descripcion;
    }
}
