package com.ejemplo.jade;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true"); // abre la GUI de JADE
        AgentContainer container = rt.createMainContainer(profile);

        try {
            AgentController po = container.createNewAgent("POAgent", "com.ejemplo.jade.agents.POAgent", null);
            AgentController rf = container.createNewAgent("RFAgent", "com.ejemplo.jade.agents.RFAgent", null);
            AgentController cu = container.createNewAgent("CUAgent", "com.ejemplo.jade.agents.CUAgent", null);
            AgentController tc = container.createNewAgent("TestCaseAgent", "com.ejemplo.jade.agents.TestCaseAgent", null);

            po.start();
            rf.start();
            cu.start();
            tc.start();

        } catch (StaleProxyException e) {
            System.err.println("Error launching agents: " + e.getMessage());
        }
    }
}
