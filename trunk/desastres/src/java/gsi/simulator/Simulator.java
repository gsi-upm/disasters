package gsi.simulator;

import gsi.simulator.Logger.Logger;
import java.util.*;
import java.io.IOException;
import gsi.disasters.*;
import gsi.simulator.rest.EventsManagement;

/**
 *
 * @author Luis Delgado
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
     * Saves if the simulation is paused
     */
     private static boolean pause;

    /**
     * Constructor for empty simulator
     */
    public Simulator() {
        this.parameters = new Parameters();
        this.generator = new RandomGenerator(parameters);
        this.disasters = new ArrayList<Disaster>();
        this.queue = new EventQueue();
    }

    /**
     * Constructor with file parameters
     */
    public Simulator(Parameters parameters) {
        this.parameters = parameters;
        this.generator = new RandomGenerator(parameters);
        this.disasters = new ArrayList<Disaster>();
        this.queue = new EventQueue();
    }

    /**
     * Runs a simulation loop which lasts the specified time
     * @param howLong length of the simulation
     */
    private void simulateLoop(int howLong, int victims0, int fires0) {
        pause = false;
        Event currentEvent = Event.generateNewFire(0);
        Event firstRefresh = Event.generateRefresh(null, generator.refreshPeriod());
        queue.insert(firstRefresh);

        if (fires0 > 1) {
            for (int i = fires0; i>1; i--) {
                queue.insert(Event.generateNewFire(0));
            }
        }

        while (currentEvent.getInstant() <= howLong && !pause) {
            LOGGER.info("Processing: " + currentEvent);
            //Generation of a new fire
            if (currentEvent.isFireGeneration()) {

                SizeType size = generator.fireDefineSize(); // Size of the fire
                int strength = generator.fireDefineStrength(); // Strength of the fire
                DensityType density = generator.trafficDefineDensity(); // Traffic density around the fire
                int initialSlight; //Number of slight victims
                int initialSerious; //Number of serious victims
                int initialTrapped; //Number of trapped victims
                int initialDead; //Number of deads

                // If it is one of the initial fires, we use the "initial victims" parameter
                if ((victims0 > 0) && (currentEvent.getInstant() == 0)) {
                    // We divide the number of victims among the vicitm types
                    int mean = victims0/4;
                    int rest = victims0 - 3*mean;
                    
                    initialSlight = rest; 
                    initialSerious = mean; 
                    initialTrapped = mean; 
                    initialDead = mean;
                }

                //If it's not an initial fire or there aren't initial victims, we get a random number
                else {
					int victimas = generator.initialSlightVictims();
					int mean = victimas/4;
                    int rest = victimas - 3*mean;

                    initialSlight = rest;
                    initialSerious = mean;
                    initialTrapped = mean;
                    initialDead = mean;
                    //initialSlight = generator.initialSlightVictims();
                    //initialSerious = generator.initialSeriousVictims();
                    //initialTrapped = generator.initialTrappedVictims();
                    //initialDead = generator.initialDeadVictims();
                }
                
                //List to insert in the Disaster instance
                List<Person> slight = new ArrayList<Person>();
                List<Person> serious = new ArrayList<Person>();
                List<Person> trapped = new ArrayList<Person>();
                List<Person> dead = new ArrayList<Person>();

                //Random latitude and longitude (in Madrid!)
                double latitude = generator.randomLatitude();
                double longitude = generator.randomLongitude();

                //Saves the disaster in the DB and gets its id
                int idDisaster = EventsManagement.insertFire(latitude, longitude,
                        "Simulated_Fire", size, density);

                //Saves the people in the DB
                if (initialSlight > 0) {
                    int id = EventsManagement.insertResourcesOrVictims("slight", "Simulated_slight",
                            initialSlight,
                            latitude + parameters.SLIGHT_LATITUDE_DIFFERENCE,
                            longitude + parameters.SLIGHT_LONGITUDE_DIFFERENCE,
                            idDisaster);
                    for (int i = 0; i < initialSlight; i++) {
                        //All the Person's of the same type will have the same id
                        slight.add(VictimManager.generateSlightVictim(id, parameters));
                    }
                }
                if (initialSerious > 0) {
                    int id = EventsManagement.insertResourcesOrVictims("serious", "Simulated_serious",
                            initialSerious,
                            latitude + parameters.SERIOUS_LATITUDE_DIFFERENCE,
                            longitude + parameters.SERIOUS_LONGITUDE_DIFFERENCE,
                            idDisaster);
                    for (int i = 0; i < initialSerious; i++) {
                        serious.add(VictimManager.generateSeriousVictim(id, parameters));
                    }
                }
                if (initialTrapped > 0) {
                    int id = EventsManagement.insertResourcesOrVictims("trapped", "Simulated_trapped",
                            initialTrapped,
                            latitude + parameters.TRAPPED_LATITUDE_DIFFERENCE,
                            longitude + parameters.TRAPPED_LONGITUDE_DIFFERENCE,
                            idDisaster);
                    for (int i = 0; i < initialTrapped; i++) {
                        trapped.add(VictimManager.generateDefaultTrapped(id));
                    }
                }
                if (initialDead > 0) {
                    int id = EventsManagement.insertResourcesOrVictims("dead", "Simulated_dead",
                            initialDead,
                            latitude + parameters.DEAD_LATITUDE_DIFFERENCE,
                            longitude + parameters.DEAD_LONGITUDE_DIFFERENCE,
                            idDisaster);
                    for (int i = 0; i < initialDead; i++) {
                        dead.add(VictimManager.generateDefaultDead(id));
                    }
                }
                //Generates the Disaster object
                Disaster newDisaster = new Disaster(idDisaster, DisasterType.FIRE,
                        "First_fire", "INFO", "DESCRIPTION", "ADDRESS", longitude, latitude,
                        StateType.ACTIVE, size, density, slight, serious, dead,
                        trapped, 0, 0, 0, strength);

                //Saves it in the disasters list of the simulator
                disasters.add(newDisaster);

                LOGGER.info("New fire. Id: " + newDisaster.getId() +
                        ". Strength: " + newDisaster.getStrength());
                LOGGER.info(" Victims: " + newDisaster.getSlightNum() + ", " + newDisaster.getSeriousNum() +
                        ", " + newDisaster.getDeadNum() + ", " + newDisaster.getTrappedNum());
                //Generates the next generation fire event
                queue.insert(Event.generateNewFire(currentEvent.getInstant() + generator.fireGeneratePeriod()));
            } //Refresh fires and victims
            else if (currentEvent.isRefresh()) {

                for (Disaster currentDisaster : disasters) {

                    //Code that refreshes the fire
                    if (currentDisaster.isActive()) {
                        if (currentDisaster.isEnoughFiremen()) {
                            currentDisaster.setState(StateType.CONTROLLED);
                            //TODO: I don't know if it's changed this way
                            //EventsManagement.modify(currentDisaster.getId(), "state", "controlled");
                        } else {
                            generator.increaseRandomStrength(currentDisaster);
                        }
                    }

                    else if (currentDisaster.isControlled()) {
                        if (currentDisaster.getStrength() > 0) {
                            generator.reduceRandomStrength(currentDisaster);
                        }
                        if ((currentDisaster.getStrength() == 0)
                                && (currentDisaster.getTrappedNum() == 0)
                                && (currentDisaster.getSeriousNum() == 0)
                                && (currentDisaster.getSlightNum() == 0)) {
                            currentDisaster.setState(StateType.ERASED);
                            EventsManagement.delete(currentDisaster.getId());
                        }
                    }

                    /* Refreshes the victims if its active or controlled (not erased)
                     * and the number of ambulances is not enough */
                    if (!currentDisaster.isErased()) {
                        //Save the previous amount of each kind of victim to update then
                        int slightBefore = currentDisaster.getSlightNum();
                        int seriousBefore = currentDisaster.getSeriousNum();
                        int trappedBefore = currentDisaster.getTrappedNum();
                        int deadBefore = currentDisaster.getDeadNum();

                        //Save the previous marker id's
                        int slightBeforeId = 0;
                        int seriousBeforeId = 0;
                        int trappedBeforeId = 0;
                        int deadBeforeId = 0;
                        if(slightBefore > 0) {
                            slightBeforeId = currentDisaster.getSlight().get(0).getId();
                        }
                        if(seriousBefore > 0) {
                            seriousBeforeId = currentDisaster.getSerious().get(0).getId();
                        }
                        if(trappedBefore > 0) {
                            trappedBeforeId = currentDisaster.getTrapped().get(0).getId();
                        }
                        if(deadBefore > 0) {
                            deadBeforeId = currentDisaster.getDead().get(0).getId();
                        }

                        //Tries trapped to victim
                        Iterator iterator = currentDisaster.getTrapped().iterator();
                        while (iterator.hasNext()) {
                            Person trapped = (Person) iterator.next();
                            if (generator.doesTrappedPassToVictim (currentDisaster.getStrength())) {
                                trapped.setInjuryDegree(InjuryDegree.SLIGHT);
                                trapped.setHealthPoints(generator.initialHealthPointsSlight());
                                if(slightBefore > 0) {
                                    trapped.setId(slightBeforeId);
                                }
                                iterator.remove(); //The victim is removed from the trapped list
                                currentDisaster.addSlight(trapped);   
                                LOGGER.info("Fire: " + currentDisaster.getId() + ". Trapped to victim.");
                            }
                        }

                        //Refreshes the serious and the slight
                        if (!currentDisaster.isEnoughAmbulances()) {
                            
                            //Iterates the serious victims
                            iterator = currentDisaster.getSerious().iterator();
                            while (iterator.hasNext()) {
                                Person serious = (Person) iterator.next();
                                int decrease = generator.healthPointsDecrease(currentDisaster.getStrength());
                                serious.reduceHealthPoints(decrease);
                                //If he/she dies
                                if (serious.getInjuryDegree() == InjuryDegree.DEAD) {
                                    if(deadBefore > 0) {
                                        serious.setId(deadBeforeId);
                                    }
                                    iterator.remove();
                                    currentDisaster.addDead(serious);
                                    LOGGER.info("Fire: " + currentDisaster.getId() + ". Serious to dead.");
                                }
                            }

                            //Iterates the slight victims
                            iterator = currentDisaster.getSlight().iterator();
                            while (iterator.hasNext()) {
                                Person slight = (Person) iterator.next();
                                int decrease = generator.healthPointsDecrease(currentDisaster.getStrength());
                                slight.reduceHealthPoints(decrease);
                                //If he/she gets serious
                                if (slight.getInjuryDegree() == InjuryDegree.SERIOUS) {
                                    if(seriousBefore > 0) {
                                        slight.setId(seriousBeforeId);
                                    }
                                    iterator.remove();
                                    currentDisaster.addSerious(slight);                                    
                                    LOGGER.info("Fire: " + currentDisaster.getId() + ". Slight to serious.");
                                }
                                //If he/she dies
                                if (slight.getInjuryDegree() == InjuryDegree.DEAD) {
                                    if(deadBefore > 0) {
                                        slight.setId(deadBeforeId);
                                    }
                                    iterator.remove();
                                    currentDisaster.addDead(slight);
                                    LOGGER.info("Fire: " + currentDisaster.getId() + ". Slight to dead.");
                                }
                            }
                        }

                        //Update the markers

                        //SLIGHT
                        //There weren't slight victims and now there are
                        if(currentDisaster.getSlightNum() > 0 &&  slightBefore == 0) {
                            int id = EventsManagement.insertResourcesOrVictims("slight", "NAMES",
                                    currentDisaster.getSlightNum(),
                                    currentDisaster.getLatitude() + parameters.SLIGHT_LATITUDE_DIFFERENCE,
                                    currentDisaster.getLatitude() + parameters.SLIGHT_LONGITUDE_DIFFERENCE,
                                    currentDisaster.getId());
                            for (Person slight : currentDisaster.getSlight()) {
                                slight.setId(id);
                            }
                        }
                        //There were slight victims and now there aren't
                        else if(currentDisaster.getSlightNum() == 0 &&  slightBefore > 0) {
                            EventsManagement.delete(slightBeforeId);
                        }
                        //The number of slight victims has changed
                        else if(currentDisaster.getSlightNum() !=  slightBefore) {
                            EventsManagement.modify(slightBeforeId, "quantity",
                                    "" + currentDisaster.getSlightNum());
                        }

                        //SERIOUS
                        //There weren't serious victims and now there are
                        if(currentDisaster.getSeriousNum() > 0 &&  seriousBefore == 0) {
                            int id = EventsManagement.insertResourcesOrVictims("serious", "NAMES",
                                    currentDisaster.getSeriousNum(),
                                    currentDisaster.getLatitude() + parameters.SERIOUS_LATITUDE_DIFFERENCE,
                                    currentDisaster.getLatitude() + parameters.SERIOUS_LONGITUDE_DIFFERENCE,
                                    currentDisaster.getId());
                            for (Person serious : currentDisaster.getSerious()) {
                                serious.setId(id);
                            }
                        }
                        //There were serious victims and now there aren't
                        else if(currentDisaster.getSeriousNum() == 0 &&  seriousBefore > 0) {
                            EventsManagement.delete(seriousBeforeId);
                        }
                        //The number of serious victims has changed
                        else if(currentDisaster.getSeriousNum() !=  seriousBefore) {
                            EventsManagement.modify(seriousBeforeId, "quantity",
                                    "" + currentDisaster.getSeriousNum());
                        }


                        //TRAPPED
                        //Trapped victims can't be generated after the creation of the fire
                        //There were trapped victims and now there aren't
                        if(currentDisaster.getTrappedNum() == 0 &&  trappedBefore > 0) {
                            EventsManagement.delete(trappedBeforeId);
                        }
                        //The number of trapped victims has changed
                        else if(currentDisaster.getTrappedNum() !=  trappedBefore) {
                            EventsManagement.modify(trappedBeforeId, "quantity",
                                    "" + currentDisaster.getTrappedNum());
                        }

                        //DEAD
                        //There weren't dead victims and now there are
                        if(currentDisaster.getDeadNum() > 0 &&  deadBefore == 0) {
                            int id = EventsManagement.insertResourcesOrVictims("dead", "NAMES",
                                    currentDisaster.getDeadNum(),
                                    currentDisaster.getLatitude() + parameters.DEAD_LATITUDE_DIFFERENCE,
                                    currentDisaster.getLatitude() + parameters.DEAD_LONGITUDE_DIFFERENCE,
                                    currentDisaster.getId());
                            for (Person dead : currentDisaster.getDead()) {
                                dead.setId(id);
                            }
                        }
                        //Dead cannot change to another Degree (unfortunately ^^)
                        //The number of dead victims has changed
                        else if(currentDisaster.getDeadNum() !=  deadBefore) {
                            EventsManagement.modify(deadBeforeId, "quantity",
                                    "" + currentDisaster.getDeadNum());
                        }
                    }
                    LOGGER.info("End of refresh. Fire: " + currentDisaster.getId() + ". Strength: " + currentDisaster.getStrength());
                    LOGGER.info("Victims: " + currentDisaster.getSlightNum() + ", " + currentDisaster.getSeriousNum() +
                            ", " + currentDisaster.getDeadNum() + ", " + currentDisaster.getTrappedNum());
                }

                //Every refresh generates the next refresh event
                queue.insert(Event.generateRefresh(currentEvent, generator.refreshPeriod()));
            }

        int lastEventInstant = currentEvent.getInstant();
        currentEvent = queue.extract();

        //Sleeps the thread if the simulation is in real time
        if(parameters.IS_CONSTANT) {
            try {
                Thread.sleep((long) (currentEvent.getInstant() - lastEventInstant) * 1000);
            }
            catch (InterruptedException e) {
                System.err.println("Error interrupting the Thread: " + e.getMessage());
            }
        }
        }
    }

    /**
     * Runs the simulation with a random value of initial victims and fires
     */
    public void runSimulation() {
        //Inicialization
        LOGGER.info("Simulation beginning. Length = " + this.generator.params.LENGTH);

        //Simulation loop running
        this.simulateLoop(this.generator.params.LENGTH, 0, 0);
        LOGGER.info("End of simulation");
    }

    /**
     * Runs the simulation with the specified number of victims and fires
     * @param victims0 number of initial victims
     * @param fires0 number of initial fires
     */
    public void runSimulation(int victims0, int fires0) {
        //Inicialization
        LOGGER.info("Simulation beginning. Length = " + this.generator.params.LENGTH);

        //Simulation loop running
        this.simulateLoop(this.generator.params.LENGTH, victims0, fires0);
        LOGGER.info("End of simulation");
    }

    /**
     * Pauses the simulation
     */
    public static void pauseSimulation() {
        pause = true;
    }

    /**
     * Prepares and launches the simulation
     */
    public static void main(String[] args) throws IOException {
        //Inicialization
        Simulator sim = new Simulator();
        LOGGER.info("Simulation beginning. Length = " + sim.generator.params.LENGTH);

         //Simulation loop running
        sim.simulateLoop(sim.generator.params.LENGTH, 0, 0);
        LOGGER.info("End of simulation");
    }
}

