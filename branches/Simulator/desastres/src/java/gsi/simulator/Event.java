/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gsi.simulator;

/**
 *
 * @author Luis Delgado
 */
public class Event {
    private long idEvent;
    private long instant;
    private EventType type;

    public Event(long idEvent, long instant, EventType type) {
        this.idEvent = idEvent;
        this.instant = instant;
        this.type = type;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    public long getInstant() {
        return instant;
    }

    public void setInstant(long instant) {
        this.instant = instant;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public boolean isRefresh() {
        return this.type == EventType.REFRESH;
    }

    public boolean isFireGeneration() {
        return this.type == EventType.FIRE_GENERATION;
    }

    public boolean isAskingForAgents() {
        return this.type == EventType.ASKING_FOR_AGENTS;
    }

    public boolean isFiremenArrival() {
        return this.type == EventType.FIREMEN_ARRIVAL;
    }

    public boolean isAmbulanceArrival() {
        return this.type == EventType.AMBULANCE_ARRIVAL;
    }

    public boolean beforeThan(Event another) {
        return this.instant < another.getInstant();
    }

    public String toString() {
        return "Event - idEvent: " + this.idEvent + ", T = " + this.instant;
    }
    
}
