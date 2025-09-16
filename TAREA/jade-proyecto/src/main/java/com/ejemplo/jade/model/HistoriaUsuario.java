package com.ejemplo.jade.model;

/**
 * Modelo simple de Historia de Usuario.
 */
public class HistoriaUsuario {
    private final String id;
    private final String texto;

    public HistoriaUsuario(String id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public String getId() { return id; }
    public String getTexto() { return texto; }

    @Override
    public String toString() {
        return "HU[" + id + "]: " + texto;
    }

    // formato para envío/recepción simple
    public String toContentString() {
        return "HU|" + id + "|" + texto;
    }

    public static HistoriaUsuario fromContentString(String s) {
        // espera "HU|id|texto"
        String[] parts = s.split("\\|", 3);
        return new HistoriaUsuario(parts[1], parts[2]);
    }
}
