/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gsi.simulator;

import gsi.simulator.Logger.Logger;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import gsi.disasters.Disaster;

/**
 *
 * @author luis
 */
public class Simulator {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger("gsi.simulator");

    /**
     * Period for simulation refresh
     */
    public static long REFRESH_PERIOD = 60;

    /**
     * Period for fire generation
     */
    //It must be done with a random period. By now, we'll do it with a constant value.
    public static long FIRE_GENERATION_PERIOD = 600;

    /**
     * List containing the fires generated in the simulation
     */
    private List<Disaster> disasters;

    /**
     * Number of active disasters, used in loops
     */
    private static int disastersCount = 0;
    /**
     * Queue where events are inserted
     */
    private EventQueue queue;

    /**
     * Random generator
     */
    private RandomGenerator generator;
   
    /**
     * Constructor for empty simulator
     */
    public Simulator() {
        disasters = new ArrayList<Disaster>();
        queue = new EventQueue();
    }

     /**
     * Constructor with file parameters
     */
    public Simulator(Parameters parameters) {
        // generator = new RandomGenerator(parameters);
        disasters = new ArrayList<Disaster>();
        queue = new EventQueue();
    }

    /**
     * Runs a simulation loop which lasts the specified time
     *
     * @param howLong length of the simulation
     */
    private void simulateLoop(long howLong) {
        Event currentEvent = Event.generateNewFire(0);
        Event firstRefresh = Event.generateRefresh(null, REFRESH_PERIOD);

        queue.insert(firstRefresh);

        while (currentEvent.getInstant() <= howLong) {

            if(currentEvent.isFireGeneration()) {
                //Generation of a new fire
                disasters.add(new Disaster(disastersCount, "Fire", "First fire", "INFO", "DESCRIPTION", "ADDRESS",
                        0, 0, "active", "small", "TRAFFIC", 5, 2, 0, 3));
                // TODO This constructor should be changed for another one with a numeric strength attribute
                disastersCount++;

                //Every fire generation event generates the next one
                //We'll have to insert a random delay, not a constant as now
                queue.insert(Event.generateNewFire(currentEvent.getInstant()
                        + FIRE_GENERATION_PERIOD));
            }

            //Refresh fires and victims
            else if(currentEvent.isRefresh()) {
                //Code that refreshes the fire
                for (int i = 0; i < disastersCount; i++) {
                    Disaster currentDisaster = disasters.remove(i);
                    if (currentDisaster.getState() == "active") {
                        if (currentDisaster.getFiremen() < 10) { // Number 10 should be changed by a value depending on the disaster size
                            //Here the disaster strength should be increased, but there
                            //is not a numeric attribute yet
                        }
                        else {
                            //Here the disaster's strength should be decreased
                            //Besides, if strength < threshold (umbral), state should change to "controlled"
                        }
                    }
                    else if (currentDisaster.getState() == "controlled") {
                        //Decrease of disaster strength
                        //If strength == 0, state should change to "erased"
                    }                    
                    disasters.add(i, currentDisaster); // So as not to modify the arraylist
                }
                //Code that refreshes the victims
 
                //Every refresh generates the next refresh event
                queue.insert(Event.generateRefresh(currentEvent, REFRESH_PERIOD));
            }
            LOGGER.info(currentEvent.toString());
            currentEvent = queue.extract();
        }
    }

    /**
     * Runs the simulation
     */
    public void runSimulation() {
        /*
		 * Inicialization
		 */
        long howLong = 10000; //Simulation of 10.000 seconds
        LOGGER.info("Simulation beginning. Duration = " + howLong);
        Simulator sim = new Simulator();

        /*
		 * Simulation loop running
		 */
        sim.simulateLoop(howLong);
        LOGGER.info("End of simulation");
    }

    /**
	 * Prepares and launches the simulation
	 */
    public static void main() throws IOException {

        /*
		 * Inicialization
		 */
        //Simulation of 10.000 seconds
        long howLong = 10000;
        LOGGER.info("Simulation beginning. Duration = " + howLong);
        Simulator sim = new Simulator();

        /*
		 * Simulation loop running
		 */
        sim.simulateLoop(howLong);
        LOGGER.info("End of simulation");
    }
}

