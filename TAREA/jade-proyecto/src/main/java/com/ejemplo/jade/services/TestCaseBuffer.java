package com.ejemplo.jade.services;

import java.util.ArrayList;
import java.util.List;

import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.RequisitoFuncional;

public class TestCaseBuffer {
    private final List<RequisitoFuncional> requisitos = new ArrayList<>();
    private final List<CasoDeUso> casosUso = new ArrayList<>();

    public void addRequisito(RequisitoFuncional rf) {
        requisitos.add(rf);
    }

    public void addCasoUso(CasoDeUso cu) {
        casosUso.add(cu);
    }

    public boolean tieneCombinaciones() {
        return !requisitos.isEmpty() && !casosUso.isEmpty();
    }

    public List<RequisitoFuncional> getRequisitos() {
        return new ArrayList<>(requisitos);
    }

    public List<CasoDeUso> getCasosUso() {
        return new ArrayList<>(casosUso);
    }

    public void limpiar() {
        requisitos.clear();
        casosUso.clear();
    }
}
