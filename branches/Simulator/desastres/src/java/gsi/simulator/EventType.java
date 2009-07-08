package gsi.simulator;

/**
 *
 * @author Luis Delgado
 */
public enum EventType {
    REFRESH, FIRE_GENERATION, ASKING_FOR_AGENTS, FIREMEN_ARRIVAL,
    AMBULANCE_ARRIVAL;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
