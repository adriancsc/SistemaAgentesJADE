package com.ejemplo.jade.services;

import com.ejemplo.jade.model.HistoriaUsuario;
import com.ejemplo.jade.model.RequisitoFuncional;

public class HUtoRFProcessor {

    public RequisitoFuncional procesarHU(HistoriaUsuario hu) {
        String rfId = "RF-" + hu.getId();
        String descripcion;
        String texto = hu.getTexto().toLowerCase();

        if (texto.contains("registr")) {
            descripcion = "El sistema debe permitir registrar usuarios con datos válidos.";
        } else if (texto.contains("login") || texto.contains("iniciar sesión") || texto.contains("autenticar")) {
            descripcion = "El sistema debe permitir inicio de sesión con credenciales válidas y gestionar errores de autenticación.";
        } else if (texto.contains("comprar") || texto.contains("pagar")) {
            descripcion = "El sistema debe permitir proceso de compra y pago con tarjeta.";
        } else {
            descripcion = "Requisito funcional general derivado de la HU.";
        }

        return new RequisitoFuncional(rfId, descripcion);
    }
}
