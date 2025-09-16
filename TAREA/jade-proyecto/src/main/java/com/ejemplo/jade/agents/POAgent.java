package com.ejemplo.jade.agents;

import com.ejemplo.jade.model.HistoriaUsuario;
import com.ejemplo.jade.services.HUDispatcher;
import com.ejemplo.jade.utils.DFUtils;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;

public class POAgent extends Agent {
    private HUDispatcher dispatcher;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": iniciado.");
        DFUtils.registerService(this, "creador-hu", "PO-HU-Service");

        dispatcher = new HUDispatcher(this);

        HistoriaUsuario[] hus = {
            new HistoriaUsuario("HU1", "Como cliente quiero registrarme para acceder a mi cuenta"),
            new HistoriaUsuario("HU2", "Como cliente quiero iniciar sesión con email y contraseña"),
            new HistoriaUsuario("HU3", "Como cliente quiero comprar un producto y pagar con tarjeta")
        };

        try {
            DFAgentDescription[] providers = DFUtils.searchService(this, "procesar-hu");
            if (providers.length == 0) {
                System.out.println(getLocalName() + ": No hay agentes para 'procesar-hu'.");
            } else {
                for (HistoriaUsuario hu : hus) {
                    addBehaviour(new WakerBehaviour(this, 500 + (int)(Math.random() * 5000)) {
                        @Override
                        protected void onWake() {
                            dispatcher.sendHU(hu, providers);

                            System.out.println(getLocalName() + 
                            ": HU procesada y enviada con éxito -> " + hu.getId());
                        }
                    });
                }
            }
        } catch (FIPAException e) {
            System.err.println(getLocalName() + ": Error al buscar en DF -> " + e.getMessage());
        }
    }

    @Override
    protected void takeDown() {
        DFUtils.deregisterService(this);
    }
}
