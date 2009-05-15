/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsi.simulator;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Luis Delgado
 */
public class EventQueue {
    
private List<Event> queue;

    public EventQueue() {
        queue = new ArrayList<Event>();
    }

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

    public Event extract() {
        if (isEmpty())
            return null;
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return (queue.size() == 0);
    }

    public int getEventNumber() {
        return (queue.size());
    }

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
