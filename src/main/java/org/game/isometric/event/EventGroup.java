package org.game.isometric.event;

import java.util.HashSet;
import java.util.Set;

public class EventGroup {
    private final Set<Event> eventSet;
    private final Set<Class<? extends Event>> requiredEvent;

    public EventGroup(Set<Class<? extends Event>> requiredEvent) {
        this.eventSet = new HashSet<>();
        this.requiredEvent = requiredEvent;
    }

    public void addEvent(Event event) {
        if (requiredEvent.contains(event.getClass())) {
            eventSet.add(event);
        }

        if (new HashSet<>(eventSet.stream().map(Event::getClass).toList()).containsAll(requiredEvent)) {
            eventSet.forEach(e -> {
                EventPublisher.getInstance().publish(e);
            });
            eventSet.removeIf(Event::isToRemove);
        }
    }

    public Set<Event> getEventSet() {
        return eventSet;
    }
}
