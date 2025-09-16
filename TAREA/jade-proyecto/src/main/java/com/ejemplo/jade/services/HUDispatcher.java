package com.ejemplo.jade.services;

import com.ejemplo.jade.model.HistoriaUsuario;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class HUDispatcher {
    private final Agent agent;

    public HUDispatcher(Agent agent) {
        this.agent = agent;
    }

    public void sendHU(HistoriaUsuario hu, DFAgentDescription[] providers) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(hu.toContentString());
        for (DFAgentDescription provider : providers) {
            msg.addReceiver(provider.getName());
        }
        agent.send(msg);
        System.out.println(agent.getLocalName() + ": Envié -> " + hu);
    }
}
