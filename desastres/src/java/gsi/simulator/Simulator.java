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
import gsi.disasters.Person;
import gsi.disasters.InjuryDegree;
import gsi.simulator.VictimManager;
import java.util.Hashtable;

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
     * ¿????? Who generates the id's in the api?
     */
    private static int idCount = 0;

    /**
     * List containing the people generated with each disaster
     */
    //private List<People> people;

    /**
     * Queue where events are inserted
     */
    private EventQueue queue;

    /**
     * Random generator
     */
    private RandomGenerator generator;
    /**
     * Parameters
     */
     private Parameters parameters;
    /**
     * Constructor for empty simulator
     */
    public Simulator() {
        this.disasters = new ArrayList<Disaster>();
        //this.people = new ArrayList<People>();
        this.queue = new EventQueue();
    }

    /**
     * Constructor with file parameters
     */
    public Simulator(Parameters parameters) {
        this.parameters = parameters;
        this.generator = new RandomGenerator(parameters);
        this.disasters = new ArrayList<Disaster>();
        //this.people = new ArrayList<People>();
        this.queue = new EventQueue();
    }

    /**
     * Runs a simulation loop which lasts the specified time
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
                List<Person> slight = new ArrayList<Person>();
                for(int i = 0; i < generator.initialSlightVictims(); i++) {
                    slight.add(VictimManager.generateSlightVictim(0, parameters));
                }
                List<Person> serious = new ArrayList<Person>();
                for(int i = 0; i < generator.initialSeriousVictims(); i++) {
                    serious.add(VictimManager.generateSeriousVictim(0, parameters));
                }
                List<Person> trapped = new ArrayList<Person>();
                for(int i = 0; i < generator.initialTrappedVictims(); i++) {
                    trapped.add(VictimManager.generateDefaultTrapped(0));
                }
                List<Person> dead = new ArrayList<Person>();
                for(int i = 0; i < generator.initialDeadVictims(); i++) {
                    dead.add(VictimManager.generateDefaultDead(0));
                }

                disasters.add(new Disaster(idCount, DisasterType.FIRE, "First fire",
                        "INFO", "DESCRIPTION", "ADDRESS", 0, 0, StateType.ACTIVE,
                        SizeType.getType(size), DensityType.getType(density),
                        slight, serious, dead, trapped, null, null, null,
                        strength));

                /**
                int disasterId = idCount; // We save the disaster id so that we can assign people to it

                // These lines generate People with assigned injury degree (health points assigned automatically).
                // It is possible to create People with assigned health points (automatic injury degree).
                people.add(new People(++idCount, InjuryDegree.SLIGHT, "NAME", "INFO", "DESCRIPTION", disasterId, slight));
                people.add(new People(++idCount, InjuryDegree.SERIOUS, "NAME", "INFO", "DESCRIPTION", disasterId, severe));
                people.add(new People(++idCount, InjuryDegree.DEAD, "NAME", "INFO", "DESCRIPTION", disasterId, dead));
                people.add(new People(++idCount, InjuryDegree.TRAPPED, "NAME", "INFO", "DESCRIPTION", disasterId, trapped));
                */
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

                        if (currentDisaster.getFireEnginesNum() >= currentDisaster.necessaryFiremen()) {
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
                            if ((currentDisaster.getTrappedNum() == 0)
                                    && (currentDisaster.getSeriousNum() == 0)
                                    && (currentDisaster.getSlightNum() == 0))
                                currentDisaster.setState(StateType.ERASED);
                                //TODO: Erase fire image
                        }
                    }
                    /*
                    //Code that refreshes the victims assigned to the current disaster
                    for (People currentPeople: people) {
                        if (currentPeople.getIdAssigned() == currentDisaster.getId()) {
                            if (currentDisaster.isActive() || currentDisaster.isControlled()) {
                                if (currentPeople.areTrapped()) {
                                    int decrease = generator.healthPointsDecrease(currentDisaster.getStrength());
                                    currentPeople.reduceHealthPoints(decrease);
                                }
                                else if (currentPeople.areSlight() || currentPeople.areSevere()) {
                                    if (currentDisaster.getAmbulancesNum() < currentPeople.necessaryAmbulances()) {
                                        int decrease = generator.healthPointsDecrease(currentDisaster.getStrength());
                                        currentPeople.reduceHealthPoints(decrease);

                                        //Checks if injury degree should change. If so, we change the number of
                                        //victims in the disaster.
                                        if (currentPeople.getHealthPoints() <= 0) {
                                            currentPeople.setType(InjuryDegree.DEAD);
                                            //currentDisaster.setDead(currentDisaster.getDeadNum() + currentPeople.getQuantity());
                                            //TODO: Change people image from severe to dead (we may have two images of dead people)
                                        }
                                        else if ((currentPeople.getType() == InjuryDegree.SLIGHT) && (currentPeople.getHealthPoints() < 50)) {
                                            currentPeople.setType(InjuryDegree.SERIOUS);
                                            //currentDisaster.setSerious(currentDisaster.getSerious() + currentPeople.getQuantity());
                                            //TODO: Change people image from slight to severe (we may have two images of severe people)
                                        }
                                    }
                                }
                            }
                        }
                    }
                     * */
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

