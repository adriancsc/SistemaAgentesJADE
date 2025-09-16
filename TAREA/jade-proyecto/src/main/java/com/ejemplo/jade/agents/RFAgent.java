package com.ejemplo.jade.agents;

import com.ejemplo.jade.model.HistoriaUsuario;
import com.ejemplo.jade.model.RequisitoFuncional;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * RFAgent: especialista en extraer Requisitos Funcionales desde una HU.
 * - Se registra en DF con type="procesar-hu"
 * - Al recibir HU genera RF y busca en DF al agente "generar-cp" para enviarle el RF
 */
public class RFAgent extends Agent {
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

        addBehaviour(new jade.core.behaviours.CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (content != null && content.startsWith("HU|")) {
                        HistoriaUsuario hu = HistoriaUsuario.fromContentString(content);
                        System.out.println(getLocalName() + ": Recibí HU -> " + hu);

                        // Generar RF basado en palabras clave (simulación de lógica)
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

                        RequisitoFuncional rf = new RequisitoFuncional(rfId, descripcion);
                        System.out.println(getLocalName() + ": Generé -> " + rf);

                        // Buscar en DF al agente que ofrece "generar-cp"
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
                                out.setContent(rf.toContentString());
                                for (DFAgentDescription g : gens) {
                                    out.addReceiver(g.getName());
                                }
                                send(out);
                                System.out.println(getLocalName() + ": Envié RF a generador de CP.");
                            }
                        } catch (FIPAException fe) {
                            System.err.println("Error al buscar el servicio 'generar-cp': " + fe.getMessage());
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
        try { DFService.deregister(this); } catch (FIPAException e) { System.err.println("Error al desregistrar el agente: " + e.getMessage()); }
    }
}
