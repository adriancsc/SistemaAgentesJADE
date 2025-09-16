package com.ejemplo.jade.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ejemplo.jade.model.CasoDePrueba;
import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.RequisitoFuncional;

public class TestCaseGenerator {

    public List<CasoDePrueba> generarCasos(List<RequisitoFuncional> requisitos, List<CasoDeUso> casosUso) {
        List<CasoDePrueba> casosPrueba = new ArrayList<>();
        for (RequisitoFuncional rf : requisitos) {
            for (CasoDeUso cu : casosUso) {
                String cpId = "CP-" + UUID.randomUUID().toString().substring(0, 6);
                String descripcion = String.format("Verificar '%s' en el escenario '%s'",
                        rf.getDescripcion(), cu.getTitulo());
                casosPrueba.add(new CasoDePrueba(cpId, descripcion));
            }
        }
        return casosPrueba;
    }
}
