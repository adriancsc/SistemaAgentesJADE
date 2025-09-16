package com.ejemplo.jade.utils;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class DFUtils {
    public static void registerService(Agent agent, String type, String name) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);
        dfd.addServices(sd);
        try {
            DFService.register(agent, dfd);
            System.out.println(agent.getLocalName() + ": registrado en DF como " + type);
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + ": Error al registrar en DF -> " + e.getMessage());
        }
    }

    public static DFAgentDescription[] searchService(Agent agent, String type) throws FIPAException {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        template.addServices(sd);
        return DFService.search(agent, template);
    }

    public static void deregisterService(Agent agent) {
        try {
            DFService.deregister(agent);
        } catch (FIPAException e) {
            System.err.println(agent.getLocalName() + ": Error al desregistrar -> " + e.getMessage());
        }
    }
}
