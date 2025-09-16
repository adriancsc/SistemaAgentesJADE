package com.ejemplo.jade.agents;

import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.HistoriaUsuario;
import com.ejemplo.jade.services.CUDispatcher;
import com.ejemplo.jade.services.HUtoCUProcessor;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class CUAgent extends Agent {
    private CUDispatcher dispatcher;
    private HUtoCUProcessor processor;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado. Registrándome como 'procesar-hu'.");

        // Registrar servicio
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("procesar-hu");
        sd.setName("CU-Service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error al registrar: " + e.getMessage());
        }

        // Inicializar servicios auxiliares
        dispatcher = new CUDispatcher(this);
        processor = new HUtoCUProcessor();

        // Comportamiento principal
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent() != null && msg.getContent().startsWith("HU|")) {
                    HistoriaUsuario hu = HistoriaUsuario.fromContentString(msg.getContent());
                    System.out.println(getLocalName() + ": Recibí HU -> " + hu);

                    addBehaviour(new WakerBehaviour(myAgent, 1000) {
                        @Override
                        protected void onWake() {
                            // Procesar HU
                            CasoDeUso cu = processor.procesarHU(hu);
                            System.out.println(getLocalName() + ": CU generado -> " + cu);

                            // Buscar agentes destino
                            DFAgentDescription template = new DFAgentDescription();
                            ServiceDescription sd2 = new ServiceDescription();
                            sd2.setType("generar-cp");
                            template.addServices(sd2);

                            try {
                                DFAgentDescription[] gens = DFService.search(myAgent, template);
                                dispatcher.sendCU(cu, gens);

                                System.out.println(getLocalName() + 
                ": Procesamiento finalizado con éxito. CU enviado a " + gens.length + " agente(s).");
                            } catch (FIPAException fe) {
                                System.err.println("FIPAException: " + fe.getMessage());
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
            System.err.println("Error al desregistrar: " + e.getMessage());
        }
    }
}
