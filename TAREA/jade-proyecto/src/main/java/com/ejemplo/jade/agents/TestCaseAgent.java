package com.ejemplo.jade.agents;

import java.util.List;

import com.ejemplo.jade.model.CasoDePrueba;
import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.RequisitoFuncional;
import com.ejemplo.jade.services.TestCaseBuffer;
import com.ejemplo.jade.services.TestCaseGenerator;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class TestCaseAgent extends Agent {
    private TestCaseBuffer buffer;
    private TestCaseGenerator generator;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado. Registrándome como 'generar-cp'.");

        // Registro en DF
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

        // Inicializar helpers
        buffer = new TestCaseBuffer();
        generator = new TestCaseGenerator();

        // Comportamiento principal
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent() != null) {
                    String content = msg.getContent();

                    if (content.startsWith("RF|")) {
                        RequisitoFuncional rf = RequisitoFuncional.fromContentString(content);
                        buffer.addRequisito(rf);
                        System.out.println(getLocalName() + ": Recibí RF -> " + rf);
                    } else if (content.startsWith("CU|")) {
                        CasoDeUso cu = CasoDeUso.fromContentString(content);
                        buffer.addCasoUso(cu);
                        System.out.println(getLocalName() + ": Recibí CU -> " + cu);
                    }

                    if (buffer.tieneCombinaciones()) {
                        addBehaviour(new WakerBehaviour(myAgent, 1000 + (int)(Math.random() * 6000)) {
                            @Override
                            protected void onWake() {
                                List<CasoDePrueba> cps = generator.generarCasos(buffer.getRequisitos(), buffer.getCasosUso());
                                for (CasoDePrueba cp : cps) {
                                    System.out.println(getLocalName() + ": CP generado -> " + cp);
                                }
                                buffer.limpiar();

                                System.out.println(getLocalName() + 
                ": Procesamiento finalizado. Se generaron " + cps.size() + " CP(s).");
                            }
                        });
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
