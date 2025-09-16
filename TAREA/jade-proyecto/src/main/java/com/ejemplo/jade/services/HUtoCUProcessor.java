package com.ejemplo.jade.services;

import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.HistoriaUsuario;

public class HUtoCUProcessor {

    public CasoDeUso procesarHU(HistoriaUsuario hu) {
        String cuId = "CU-" + hu.getId();
        String titulo;
        String texto = hu.getTexto().toLowerCase();

        if (texto.contains("registr")) {
            titulo = "Registrar nuevo usuario";
        } else if (texto.contains("login") || texto.contains("iniciar sesión") || texto.contains("autenticar")) {
            titulo = "Iniciar sesión (login)";
        } else if (texto.contains("comprar") || texto.contains("pagar")) {
            titulo = "Proceso de compra y pago";
        } else {
            titulo = "Caso de uso genérico derivado de HU";
        }

        return new CasoDeUso(cuId, titulo);
    }
}
