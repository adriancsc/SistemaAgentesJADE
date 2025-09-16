package com.ejemplo.jade.services;

import com.ejemplo.jade.model.RequisitoFuncional;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class RFDispatcher {
    private final Agent agent;

    public RFDispatcher(Agent agent) {
        this.agent = agent;
    }

    public void sendRF(RequisitoFuncional rf, DFAgentDescription[] receivers) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(rf.toContentString());
        for (DFAgentDescription r : receivers) {
            msg.addReceiver(r.getName());
        }
        agent.send(msg);
        System.out.println(agent.getLocalName() + ": Envié RF -> " + rf);
    }
}
