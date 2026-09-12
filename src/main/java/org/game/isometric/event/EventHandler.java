package org.game.isometric.event;

public interface EventHandler<T> {
    void handleEvent(T event);
}
