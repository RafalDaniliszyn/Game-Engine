package org.game.event;

public abstract class Event {
    private EventEnum eventEnum;

    public Event(EventEnum eventEnum) {
        this.eventEnum = eventEnum;
    }

    public EventEnum getEventEnum() {
        return eventEnum;
    }
}
