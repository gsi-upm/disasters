package gsi.simulator;

import java.util.List;
import java.util.ArrayList;

/**
 * Event queue.
 * Events can be inserted in any order, but they must be extracted by order
 * (just like method Event.beforeThan).
 * If two events occur at the same time, the first which was inserted will be extracted.
 *
 * @author Luis Delgado
 */
public class EventQueue {

    /**
     * The queue where events will be inserted
     */
    private List<Event> queue;

    /**
     * Creates a new unlimited size event queue
     */
    public EventQueue() {
        queue = new ArrayList<Event>();
    }

    /**
     * Inserts one event into the queue. If event is null, nothing happens
     *
     * @param event event to insert
     */
    public void insert(Event event) {
        if (event == null) {
            return;
        }
        if (isEmpty()) {
            queue.add(event);
            return;
        }
        for (int i = (queue.size() - 1); i >= 0; i--) {
            if (!(event.beforeThan(queue.get(i)))) {
                queue.add(i+1, event);
                return;
            }
        }
        queue.add(0, event);
    }

    /**
     * It extracts the first non yet extracted event (by time order). If two events
     * occur at the same time, it selects the first which was inserted
     *
     * @return first event in the queue; or NULL if it's empty
     */
    public Event extract() {
        if (isEmpty())
            return null;
        return queue.remove(0);
    }

    /**
     * This tells us if the queue is empty
     *
     * @return TRUE if the queue is empty
     */
    public boolean isEmpty() {
        return (queue.size() == 0);
    }

    /**
     * Gets the number of events inside the queue
     *
     * @return number of events inside the queue
     */
    public int getEventNumber() {
        return (queue.size());
    }


    @Override
    /**
     * List of events inside the queue
     *
     * @return all events inside the queue
     */
    public String toString() {
        if (isEmpty())
            return null;
        String all = "";
        for (int i = 0; i <= (queue.size() - 1); i++) {
            Event e = queue.get(i);
            all = all + e.toString() + "\n";
        }
        return all;
    }
}
