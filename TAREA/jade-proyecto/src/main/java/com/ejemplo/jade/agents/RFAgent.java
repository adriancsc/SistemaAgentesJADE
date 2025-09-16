package com.ejemplo.jade.agents;

import com.ejemplo.jade.model.HistoriaUsuario;
import com.ejemplo.jade.model.RequisitoFuncional;
import com.ejemplo.jade.services.HUtoRFProcessor;
import com.ejemplo.jade.services.RFDispatcher;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class RFAgent extends Agent {
    private RFDispatcher dispatcher;
    private HUtoRFProcessor processor;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado. Registrándome como 'procesar-hu'.");

        // Registrar servicio en DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("procesar-hu");
        sd.setName("RF-Service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error al registrar el agente en DF: " + e.getMessage());
        }

        // Inicializar helpers
        dispatcher = new RFDispatcher(this);
        processor = new HUtoRFProcessor();

        // Comportamiento principal
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent() != null && msg.getContent().startsWith("HU|")) {
                    HistoriaUsuario hu = HistoriaUsuario.fromContentString(msg.getContent());
                    System.out.println(getLocalName() + ": Recibí HU -> " + hu);

                    addBehaviour(new WakerBehaviour(myAgent, 700 + (int)(Math.random() * 3000)) {
                        @Override
                        protected void onWake() {
                            RequisitoFuncional rf = processor.procesarHU(hu);
                            System.out.println(getLocalName() + ": RF generado -> " + rf);

                            // Buscar agentes "generar-cp"
                            DFAgentDescription template = new DFAgentDescription();
                            ServiceDescription sd2 = new ServiceDescription();
                            sd2.setType("generar-cp");
                            template.addServices(sd2);

                            try {
                                DFAgentDescription[] gens = DFService.search(myAgent, template);
                                dispatcher.sendRF(rf, gens);

                                System.out.println(getLocalName() + 
                ": Procesamiento finalizado con éxito. RF enviado a " + gens.length + " agente(s).");
                            } catch (FIPAException fe) {
                                System.err.println("Error al buscar el servicio 'generar-cp': " + fe.getMessage());
                            }
                        }
                    });
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
