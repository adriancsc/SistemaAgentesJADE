package com.ejemplo.jade.services;

import com.ejemplo.jade.model.CasoDeUso;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class CUDispatcher {
    private final Agent agent;

    public CUDispatcher(Agent agent) {
        this.agent = agent;
    }

    public void sendCU(CasoDeUso cu, DFAgentDescription[] receivers) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(cu.toContentString());
        for (DFAgentDescription r : receivers) {
            msg.addReceiver(r.getName());
        }
        agent.send(msg);
        System.out.println(agent.getLocalName() + ": Envié CU -> " + cu);
    }
}
