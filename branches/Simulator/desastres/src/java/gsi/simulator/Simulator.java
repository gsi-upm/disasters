/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gsi.simulator;

import gsi.simulator.Logger.Logger;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 *
 * @author luis
 */
public class Simulator {

    private static final Logger LOGGER = Logger.getLogger("gsi.simulator");
     public static int REFRESH_PERIOD = 60;
     //It must be done with a random period. By now, we'll do it with a constant value.
     public static int FIRE_GENERATION_PERIOD = 600;
     
    private List<Fire> fires;
    private EventQueue queue;
    private long eventCounter;
   

    public Simulator() {
        fires = new ArrayList<Fire>();
        queue = new EventQueue();
    }

    private void simulateLoop(long howLong) {
        Event currentEvent = new Event(eventCounter++, 0, EventType.FIRE_GENERATION);
        Event firstRefresh = new Event(eventCounter++, REFRESH_PERIOD, EventType.REFRESH);
        fires.add(FireGenerator.generateFire());
        queue.insert(currentEvent);
        queue.insert(firstRefresh);
        while (currentEvent.getInstant() <= howLong) {

            //Refresh fires and victims
            if(currentEvent.isRefresh()) {
                //Code that refreshes the fire and victims


                //Every refresh generates the next refresh event
                queue.insert(new Event(eventCounter++, currentEvent.getInstant()
                        + REFRESH_PERIOD, EventType.REFRESH));
                
            }

            if(currentEvent.isFireGeneration()) {
                //Generation of a new fire
                fires.add(FireGenerator.generateFire());


                /* Every fire generation event generates the next one
                 * We'll have to insert a random delay, not a constant as now */
                queue.insert(new Event(eventCounter++, currentEvent.getInstant()
                        + FIRE_GENERATION_PERIOD, EventType.FIRE_GENERATION));
            }
            LOGGER.info(currentEvent.toString());
            currentEvent = queue.extract();
        }
    }

    public static void main() throws IOException {
        //Simulation of 10.000 seconds
        long howLong = 10000;
        LOGGER.info("Simulation beginning. Duration = " + howLong);
        Simulator sim = new Simulator();
        sim.simulateLoop(howLong);
        LOGGER.info("Fin de simulacion");
    }
}

