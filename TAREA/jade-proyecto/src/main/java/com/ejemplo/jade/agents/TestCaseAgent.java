package com.ejemplo.jade.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ejemplo.jade.model.CasoDePrueba;
import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.RequisitoFuncional;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * TestCaseAgent:
 * - Se registra como "generar-cp"
 * - Recibe RF y CU (contenido con prefijo RF|... o CU|...)
 * - Cuando tiene combinaciones, genera uno o más casos de prueba realistas y los imprime.
 */
public class TestCaseAgent extends Agent {
    private final List<RequisitoFuncional> requisitos = new ArrayList<>();
    private final List<CasoDeUso> casosUso = new ArrayList<>();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado. Registrándome como 'generar-cp'.");

        // Registrar servicio "generar-cp"
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("generar-cp");
        sd.setName("TestCaseService");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error during DFService registration: " + e.getMessage());
        }

        addBehaviour(new jade.core.behaviours.CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (content == null) return;

                    if (content.startsWith("RF|")) {
                        RequisitoFuncional rf = RequisitoFuncional.fromContentString(content);
                        requisitos.add(rf);
                        System.out.println(getLocalName() + ": Recibí RF -> " + rf);
                    } else if (content.startsWith("CU|")) {
                        CasoDeUso cu = CasoDeUso.fromContentString(content);
                        casosUso.add(cu);
                        System.out.println(getLocalName() + ": Recibí CU -> " + cu);
                    }

                    // Si hay combinaciones disponibles, generar CPs
                    if (!requisitos.isEmpty() && !casosUso.isEmpty()) {
                        // Generar un CP por cada combinación RF x CU (o limitar si quieres)
                        for (RequisitoFuncional rf : new ArrayList<>(requisitos)) {
                            for (CasoDeUso cu : new ArrayList<>(casosUso)) {
                                String cpId = "CP-" + UUID.randomUUID().toString().substring(0, 6);
                                String descripcion = String.format("Verificar '%s' en el escenario '%s'", rf.getDescripcion(), cu.getTitulo());
                                CasoDePrueba cp = new CasoDePrueba(cpId, descripcion);
                                System.out.println(getLocalName() + ": Generé -> " + cp);
                            }
                        }
                        // limpiar después de generar
                        requisitos.clear();
                        casosUso.clear();
                    }
                } else {
                    block();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        try { 
            DFService.deregister(this); 
        } catch (FIPAException e) { 
            System.err.println("Error al desregistrar el agente: " + e.getMessage());
        }
    }
}
