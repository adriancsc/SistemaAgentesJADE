package com.ejemplo.jade.model;

/**
 * Modelo simple de Caso de Uso.
 */
public class CasoDeUso {
    private final String id;
    private final String titulo;

    public CasoDeUso(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }

    @Override
    public String toString() {
        return "CU[" + id + "]: " + titulo;
    }

    public String toContentString() {
        return "CU|" + id + "|" + titulo;
    }

    public static CasoDeUso fromContentString(String s) {
        String[] parts = s.split("\\|", 3);
        return new CasoDeUso(parts[1], parts[2]);
    }
}
