package com.ejemplo.jade.model;

/**
 * Modelo simple de Requisito Funcional.
 */
public class RequisitoFuncional {
    private final String id;
    private final String descripcion;

    public RequisitoFuncional(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() { return id; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return "RF[" + id + "]: " + descripcion;
    }

    public String toContentString() {
        return "RF|" + id + "|" + descripcion;
    }

    public static RequisitoFuncional fromContentString(String s) {
        String[] parts = s.split("\\|", 3);
        return new RequisitoFuncional(parts[1], parts[2]);
    }
}
