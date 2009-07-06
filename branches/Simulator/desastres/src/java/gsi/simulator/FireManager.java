/*
package gsi.simulator;

import gsi.simulator.VictimManager;
import gsi.disasters.Person;

/**
 *
 * @author Luis Delgado
 
public class FireManager {

    /**
     *
    
    private static final double TRAPPED_TO_VICTIMS_INTENSITY1 = 0.05;


    /**
     * Refreshes the status of a fire: refrehes its size, its victims and
     * if the trapped have turned into victims.
     * @param fire
     
    public static void refreshFire(Fire fire) {
        refreshFireSize(fire);
        refreshFireTrapped(fire);
        refreshFireVictims(fire);

    }

    /**
     * Refreshes de size of the fire. The change must be function of the
     * strength of the fire, the number of firemen and a random value.
     * @param fire
     * @param generator
     
    public static void refreshFireSize(Fire fire) {

    }

    /**
     * Looks if the trapped have turned into victims. If so, they are moved
     * to the victims list.
     * @param fire
     * @param generator
     
    public static void refreshFireTrapped(Fire fire) {
        int numAsociateTrapped = fire.getAssociateTrapped().size();
        for(int i = 0; i < numAsociateTrapped; i++) {
            double random = Math.random();
            if(random < fire.getStrength() * TRAPPED_TO_VICTIMS_INTENSITY1) {
                Person trapped = fire.getAssociateTrapped().remove(0);
                fire.getAssociateVictims().add(trapped);
            }
        }
    }

    /**
     * Refreshes the status of the victims associate to a fire. Dead people are
     * moved into the dead list.
     * @param fire
     * @param generator
     
    public static void refreshFireVictims(Fire fire) {
        for(Person victim: fire.getAssociateVictims()) {
            VictimManager.refreshVictim(victim);
            if(victim.isDead()) {
                fire.getAssociateVictims().remove(victim);
                fire.getAssociateVictims().add(victim);
            }
        }
    }
}
*/