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
     * List containing the disasters generated in the simulation
     */
    private List<Disaster> disasters;

    /**
     * Number used to assign an id to disasters, people and resources
     */
    private static int idCount = 0;

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
    private void simulateLoop(int howLong) {
        Event currentEvent = Event.generateNewFire(0);
        Event firstRefresh = Event.generateRefresh(null, generator.refreshPeriod());

        queue.insert(firstRefresh);

        while (currentEvent.getInstant() <= howLong) {

            if(currentEvent.isFireGeneration()) {
                //Generation of a new fire
                String size = generator.fireDefineSize(); // Size of the fire
                int strength = generator.fireDefineStrength(); // Strength of the fire
                String density = generator.trafficDefineDensity(); // Traffic density around the fire

                // TODO: determine these limits (in parameters.ini)
                // numbers of initial victims (and their status)
                int trapped = generator.initialTrappedVictims();
                int slight = generator.initialSlightVictims();
                int severe = generator.initialSevereVictims();
                int dead = generator.initialDeadVictims();

                disasters.add(new Disaster(idCount, DisasterType.FIRE, "First fire", "INFO", "DESCRIPTION", "ADDRESS", 0, 0,
                        StateType.ACTIVE, SizeType.getType(size), strength, DensityType.getType(density), slight, severe, dead, trapped));

                int disasterId = idCount; // We save the disaster id so that we can assign people to it

                // These lines generate People with assigned injury degree (health points assigned automatically).
                // It is possible to create People with assigned health points (automatic injury degree).
                people.add(new People(++idCount, InjuryDegree.SLIGHT, "NAME", "INFO", "DESCRIPTION", disasterId, slight));
                people.add(new People(++idCount, InjuryDegree.SEVERE, "NAME", "INFO", "DESCRIPTION", disasterId, severe));
                people.add(new People(++idCount, InjuryDegree.DEAD, "NAME", "INFO", "DESCRIPTION", disasterId, dead));
                people.add(new People(++idCount, InjuryDegree.TRAPPED, "NAME", "INFO", "DESCRIPTION", disasterId, trapped));

                queue.insert(Event.generateNewFire(currentEvent.getInstant()
                        + generator.fireGeneratePeriod()));

                //TODO: Save fire and people in database and print them in the map
            }

            //Refresh fires and victims
            else if(currentEvent.isRefresh()) {

                for (Disaster currentDisaster : disasters) {

                    //Code that refreshes the fire
                    if (currentDisaster.isActive()) {
                        currentDisaster.increaseStrength(RandomGenerator.randomInteger(0,10));

                        if (currentDisaster.getFiremen() >= currentDisaster.necessaryFiremen()) {
                            currentDisaster.setState(StateType.CONTROLLED);
                            //TODO: Change fire image in the map
                        }
                    }

                    else if (currentDisaster.isControlled()) {
                        if (currentDisaster.getStrength() > 0)
                            currentDisaster.reduceStrength(RandomGenerator.randomInteger(0,10));
                        else if (currentDisaster.getStrength() < 0)
                            currentDisaster.setStrength(0);
                        else {
                            if ((currentDisaster.getTrapped() == 0) && (currentDisaster.getSerious() == 0)
                                    && (currentDisaster.getSlight() == 0))
                                currentDisaster.setState(StateType.ERASED);
                                //TODO: Erase fire image
                        }
                    }

                    //Code that refreshes the victims assigned to the current disaster
                    for (People currentPeople: people) {
                        if (currentPeople.getIdAssigned() == currentDisaster.getId()) {
                            if (currentDisaster.isActive() || currentDisaster.isControlled()) {
                                if (currentPeople.areTrapped()) {
                                    int decrease = RandomGenerator.healthPointsDecrease(currentDisaster.getStrength());
                                    currentPeople.reduceHealthPoints(decrease);
                                }
                                else if (currentPeople.areSlight() || currentPeople.areSevere()) {
                                    if (currentDisaster.getAmbulances() < currentPeople.necessaryAmbulances()) {
                                        int decrease = RandomGenerator.healthPointsDecrease(currentDisaster.getStrength());
                                        currentPeople.reduceHealthPoints(decrease);

                                        //Checks if injury degree should change. If so, we change the number of
                                        //victims in the disaster.
                                        if (currentPeople.getHealthPoints() <= 0) {
                                            currentPeople.setType(InjuryDegree.DEAD);
                                            currentDisaster.setDead(currentDisaster.getDead() + currentPeople.getQuantity());
                                            //TODO: Change people image from severe to dead (we may have two images of dead people)
                                        }
                                        else if ((currentPeople.getType() == InjuryDegree.SLIGHT) && (currentPeople.getHealthPoints() < 50)) {
                                            currentPeople.setType(InjuryDegree.SEVERE);
                                            currentDisaster.setSerious(currentDisaster.getSerious() + currentPeople.getQuantity());
                                            //TODO: Change people image from slight to severe (we may have two images of severe people)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //Every refresh generates the next refresh event
                queue.insert(Event.generateRefresh(currentEvent, generator.refreshPeriod()));
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
        Simulator sim = new Simulator();
        LOGGER.info("Simulation beginning. Length = " + sim.generator.params.getLength());

        /*
         * Simulation loop running
         */
        sim.simulateLoop(sim.generator.params.getLength());
        LOGGER.info("End of simulation");
    }

    /**
      * Prepares and launches the simulation
      */
    public static void main() throws IOException {

        /*
         * Inicialization
         */
        Parameters parameters = new Parameters();
        Simulator sim = new Simulator(parameters);
        LOGGER.info("Simulation beginning. Length = " + sim.generator.params.getLength());
        
        /*
         * Simulation loop running
         */
        sim.simulateLoop(sim.generator.params.getLength());
        LOGGER.info("End of simulation");
    }
}

