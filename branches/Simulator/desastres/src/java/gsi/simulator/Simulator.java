package gsi.simulator;

import gsi.simulator.Logger.Logger;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import gsi.disasters.Disaster;
import gsi.disasters.People;
import gsi.disasters.DisasterType;
import gsi.disasters.StateType;
import gsi.disasters.SizeType;
import gsi.disasters.DensityType;
import gsi.disasters.InjuryDegree;

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
     * List containing the disasters generated in the simulation
     */
    private List<Disaster> disasters;

    /**
     * Number of active disasters, used in loops
     */
    private static int disastersCount = 0;

    /**
     * List containing the people generated with each disaster
     */
    private List<People> people;

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
        people = new ArrayList<People>();
        queue = new EventQueue();
    }

    /**
     * Constructor with file parameters
     */
    public Simulator(Parameters parameters) {
        generator = new RandomGenerator(parameters);
        disasters = new ArrayList<Disaster>();
        people = new ArrayList<People>();
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
                String size = "small"; // TODO RandomGenerator method to get random size (as String)
                int strength = generator.randomInteger(1,100); // Limits should come from Parameters
                String density = "medium"; //TODO RandomGenerator method to get random density (as String)
                int slight = generator.randomInteger(0,10); // Limits should come from Parameters
                int severe = generator.randomInteger(0,10); // Limits should come from Parameters
                int dead = generator.randomInteger(0,10); // Limits should come from Parameters
                int trapped = generator.randomInteger(0,10); // Limits should come from Parameters

                disasters.add(new Disaster(disastersCount, DisasterType.FIRE, "First fire", "INFO", "DESCRIPTION", "ADDRESS",
                    0, 0, StateType.ACTIVE, SizeType.getType(size), strength, DensityType.getType(density), slight, severe, dead, trapped));

                people.add(new People(disastersCount, InjuryDegree.SLIGHT, "NAME", "INFO", "DESCRIPTION", 0, slight));
                people.add(new People(disastersCount, InjuryDegree.SEVERE, "NAME", "INFO", "DESCRIPTION", 0, severe));
                people.add(new People(disastersCount, InjuryDegree.DEAD, "NAME", "INFO", "DESCRIPTION", 0, dead));
                people.add(new People(disastersCount, InjuryDegree.TRAPPED, "NAME", "INFO", "DESCRIPTION", 0, trapped));
                disastersCount++;

                //Every fire generation event generates the next one
                //We'll have to insert a random delay, not a constant as now
                queue.insert(Event.generateNewFire(currentEvent.getInstant()
                        + FIRE_GENERATION_PERIOD));
            }

            //Refresh fires and victims
            else if(currentEvent.isRefresh()) {

                for (Disaster currentDisaster : disasters) {

                    //Code that refreshes the fire
                    if (currentDisaster.isActive()) {
                        int increase = 1; // Should be random
                        currentDisaster.increaseStrength(increase);

                        if (currentDisaster.getFiremen() >= currentDisaster.necessaryFiremen()) {
                            currentDisaster.setState(StateType.CONTROLLED);
                        }
                    }

                    else if (currentDisaster.isControlled()) {
                        int decrease = 1; // Should be random
                        currentDisaster.reduceStrength(decrease);

                        if (currentDisaster.getStrength() <= 0) {
                            currentDisaster.setState(StateType.ERASED);
                        }
                    }

                    //Code that refreshes the victims
                    for (People currentPeople: people) {
                        if (currentPeople.getId() == currentDisaster.getId()) {
                            if (currentDisaster.isActive()) {
                                if (currentPeople.areSlight() || currentPeople.areSevere()) {
                                    if (currentDisaster.getAmbulances() < currentPeople.necessaryAmbulances()) {
                                        currentPeople.reduceHealthPoints(generator.healthPointsDecrease());
                                    }
                                }
                                if (currentPeople.areTrapped()) {
                                    //If people are trapped, they can be victims (their health decreases) or not
                                    //(they stay as before).
                                        if(Math.random() < currentDisaster.getStrength()*0.05) {
                                            currentPeople.reduceHealthPoints(generator.healthPointsDecrease());
                                        }
                                }    
                            }
                        }
                    }
                }

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

