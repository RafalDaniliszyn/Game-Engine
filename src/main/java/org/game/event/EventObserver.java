package org.game.event;

import java.util.List;

public interface EventObserver {
    void notify(List<Event> event);
}
