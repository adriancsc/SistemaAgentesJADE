package com.ejemplo.jade.agents;

import com.ejemplo.jade.model.HistoriaUsuario;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * POAgent: genera HU (varias) y las envía a agentes que estén registrados como "procesar-hu".
 * Usa DFService para descubrir proveedores dinámicamente (páginas amarillas).
 */
public class POAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado. Registrándome en DF como 'creador-hu'.");

        // Registrarse en DF (opcional: para que otros agentes sepan que existe un creador de HU)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("creador-hu");
        sd.setName("PO-HU-Service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error al registrar el agente en DF: " + e.getMessage());
        }

        // Enviar varias HU (simulación)
        HistoriaUsuario[] hus = new HistoriaUsuario[] {
            new HistoriaUsuario("HU1", "Como cliente quiero registrarme para acceder a mi cuenta"),
            new HistoriaUsuario("HU2", "Como cliente quiero iniciar sesión con email y contraseña"),
            new HistoriaUsuario("HU3", "Como cliente quiero comprar un producto y pagar con tarjeta")
        };

        // Buscar proveedores del servicio "procesar-hu" en DF
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription templateSD = new ServiceDescription();
        templateSD.setType("procesar-hu");
        template.addServices(templateSD);

        try {
            DFAgentDescription[] providers = DFService.search(this, template);
            if (providers.length == 0) {
                System.out.println(getLocalName() + ": No hay agentes registrados para 'procesar-hu' (DF vacío).");
            } else {
                System.out.println(getLocalName() + ": Encontré " + providers.length + " proveedores de 'procesar-hu'. Enviando HUs...");
                for (HistoriaUsuario hu : hus) {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent(hu.toContentString());
                    // enviar a todos los proveedores encontrados
                    for (DFAgentDescription provider : providers) {
                        msg.addReceiver(provider.getName());
                    }
                    send(msg);
                    System.out.println(getLocalName() + ": Envié -> " + hu);
                }
            }
        } catch (FIPAException e) {
            System.err.println("Error al desregistrar el agente del DF: " + e.getMessage());
        }
    }

    @Override
    protected void takeDown() {
        // Desregistrarse del DF
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            System.err.println("Error al desregistrar el agente del DF: " + e.getMessage());
        }
    }
}
