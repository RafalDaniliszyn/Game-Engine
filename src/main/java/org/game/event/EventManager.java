package org.game.event;

import java.util.ArrayList;
import java.util.List;

public abstract class EventManager {
    private final List<EventObserver> eventObservers = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public void addObserver(EventObserver eventObserver) {
        eventObservers.add(eventObserver);
    }

    public void notifyObservers() {
        eventObservers.forEach(eventObserver -> eventObserver.notify(events));
        destroyEvents();
    }

    public void publishEvent(Event event) {
        events.add(event);
    }

    private void destroyEvents() {
        events.clear();
    }
}
