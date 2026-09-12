package org.game.isometric.event;

public abstract class Event {
    private final boolean toRemove;

    public Event() {
        toRemove = true;
    }

    public Event(boolean toRemove) {
        this.toRemove = toRemove;
    }

    public boolean isToRemove() {
        return toRemove;
    }

}
