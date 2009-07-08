package gsi.simulator;

import gsi.simulator.Logger.Logger;

/**
 *
 * @author Luis Delgado
 */
public class Event {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger("Event");

    /**
     * Identification of the event
     */
    private int idEvent;

    /**
     * Moment when event happens
     */
    private int instant;

    /**
     * Type of event
     */
    private EventType type;
    
    /**
     * Event counter
     */
    private static int eventCount = 0;

    /**
     * Constructor of event associated to a fire
     *
     * @param instant when event happens
     * @param type event type
     */
    public Event(int instant, EventType type) {
        this.idEvent = eventCount++;
        this.instant = instant;
        this.type = type;
    }

    /**
     * Factory generating any event at specific instant.
     * It's used to generate events in order to test EventQueue.
     *
     * @param instant when event happens
     * @return event
     */
    public static Event generateEvent(int instant) {
        Event newEvent = new Event(instant, null);
        LOGGER.info("Generate any event: " + newEvent);
        return newEvent;
    }

    /**
     * Factory generating a new fire event
     *
     * @param instant when fire will be generated
     * @return event
     */
    public static Event generateNewFire(int instant) {
        Event newEvent = new Event(instant, EventType.FIRE_GENERATION);
        LOGGER.info("Generate New Fire " + newEvent);
        return newEvent;
    }
    /**
     * Factory generating a refresh event
     *
     * @param lastRefresh last REFRESH event happened or null if it doesn't exist
     * @param period amount of time between two refresh events
     * @return event
     */
    public static Event generateRefresh(Event lastRefresh, int period) {
        int instant = period;
        if (lastRefresh != null)
            instant += lastRefresh.getInstant();
        Event newEvent = new Event(instant, EventType.REFRESH);
        LOGGER.info("Generate Refresh " + newEvent);
        return newEvent;
    }

    /**
     * Factory generating a new event to ask for agents
     *
     * @param lastAsking last ASKING FOR AGENTS event happened or null if it doesn't exist
     * @param period amount of time beween two asking for agents events
     * @return event
     */
    public static Event generateAskingAgents(Event lastAsking, int period) {
        int instant = period;
        if (lastAsking != null)
            instant += lastAsking.getInstant();
        Event newEvent = new Event(instant, EventType.ASKING_FOR_AGENTS);
        LOGGER.info("Generate Asking for Agents " + newEvent);
        return newEvent;
    }

    /**
     * Factory generating a firemen arrival event
     *
     * @param time amount of time needed for firemen to arrive at the disaster
     * @return event
     */
    public static Event generateFiremenArrival(int time) {
        Event newEvent = new Event(time, EventType.FIREMEN_ARRIVAL);
        LOGGER.info("Generate Firemen Arrival " + newEvent);
        return newEvent;
    }


    /**
     * Factory generating an ambulance arrival event
     *
     * @param time amount of time needed for ambulances to arrive at the disaster
     * @return event
     */
    public static Event generateAmbulanceArrival(int time) {
        Event newEvent = new Event(time, EventType.AMBULANCE_ARRIVAL);
        LOGGER.info("Generate Ambulance Arrival " + newEvent);
        return newEvent;
    }


    /**
     * Method returning the event identifier
     *
     * @return event identifier
     */
    public int getIdEvent() {
        return idEvent;
    }

    /**
     * Method setting the event identifier
     *
     * @param idEvent event identifier
     */
    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    /**
     * Method returning the instant when the event happens
     *
     * @return moment when the event happens
     */
    public int getInstant() {
        return instant;
    }

    /**
     * Method setting the event instant
     *
     * @param instant when event happens
     */
    public void setInstant(int instant) {
        this.instant = instant;
    }

    /**
     * Method returning the event type
     *
     * @return event type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Method setting the event type
     *
     * @param type event type
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * Method checking if event type is REFRESH
     *
     * @return true if event type is REFRESH
     */
    public boolean isRefresh() {
        return this.type == EventType.REFRESH;
    }

    /**
     * Method checking if event type is FIRE GENERATION
     *
     * @return true if event type is FIRE GENERATION
     */
    public boolean isFireGeneration() {
        return this.type == EventType.FIRE_GENERATION;
    }

    /**
     * Method checking if event type is ASKING FOR AGENTS
     *
     * @return true if event type is ASKING FOR AGENTS
     */
    public boolean isAskingForAgents() {
        return this.type == EventType.ASKING_FOR_AGENTS;
    }

    /**
     * Method checking if event type is FIREMEN ARRIVAL
     *
     * @return true if event type is FIREMEN ARRIVAL
     */
    public boolean isFiremenArrival() {
        return this.type == EventType.FIREMEN_ARRIVAL;
    }

    /**
     * Method checking if event type is AMBULANCE ARRIVAL
     *
     * @return true if event type is AMBULANCE ARRIVAL
     */
    public boolean isAmbulanceArrival() {
        return this.type == EventType.AMBULANCE_ARRIVAL;
    }

    /**
     * Compares the instant two events happen
     *
     * @return true if this event happens strictly before another event
     */
    public boolean beforeThan(Event another) {
        return (this.instant < another.getInstant());
    }

    /**
     * Prints the event
     *
     * @return String describing the event: (identifier, instant, type)
     */
    @Override
    public String toString() {
        return "Event - idEvent: " + this.idEvent + ", T = " + this.instant + " , tipo: " + this.type;
    }
    
}
