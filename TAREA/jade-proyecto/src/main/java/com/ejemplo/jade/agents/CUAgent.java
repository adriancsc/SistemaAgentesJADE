package com.ejemplo.jade.agents;

import com.ejemplo.jade.model.CasoDeUso;
import com.ejemplo.jade.model.HistoriaUsuario;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * CUAgent: especialista en extraer Casos de Uso desde una HU.
 * - Se registra en DF con type="procesar-hu"
 * - Al generar CU lo manda a agentes "generar-cp"
 */
public class CUAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado. Registrándome como 'procesar-hu'.");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("procesar-hu");
        sd.setName("CU-Service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error al registrar el agente en DF: " + e.getMessage());
        }

        addBehaviour(new jade.core.behaviours.CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (content != null && content.startsWith("HU|")) {
                        HistoriaUsuario hu = HistoriaUsuario.fromContentString(content);
                        System.out.println(getLocalName() + ": Recibí HU -> " + hu);

                        // Generar CU simple basado en palabras clave (simulación)
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

                        CasoDeUso cuObj = new CasoDeUso(cuId, titulo);
                        System.out.println(getLocalName() + ": Generé -> " + cuObj);

                        // Buscar "generar-cp" en DF y enviar
                        DFAgentDescription template = new DFAgentDescription();
                        ServiceDescription sd2 = new ServiceDescription();
                        sd2.setType("generar-cp");
                        template.addServices(sd2);
                        try {
                            DFAgentDescription[] gens = DFService.search(myAgent, template);
                            if (gens.length == 0) {
                                System.out.println(getLocalName() + ": No encontré servicio 'generar-cp'.");
                            } else {
                                ACLMessage out = new ACLMessage(ACLMessage.INFORM);
                                out.setContent(cuObj.toContentString());
                                for (DFAgentDescription g : gens) {
                                    out.addReceiver(g.getName());
                                }
                                send(out);
                                System.out.println(getLocalName() + ": Envié CU a generador de CP.");
                            }
                        } catch (FIPAException fe) {
                            System.err.println("FIPAException: " + fe.getMessage());
                        }
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
