package org.game.isometric.event;

import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventPublisher {

    private static EventPublisher instance;

    private final Map<Class<?>, List<EventHandler<?>>> listeners;
    private final Queue<Event> glContextTasks;
    private final Set<EventGroup> eventGroupSet;
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private EventPublisher() {
        this.listeners = new HashMap<>();
        this.glContextTasks = new ConcurrentLinkedQueue<>();
        this.eventGroupSet = new ConcurrentSet<>();
        this.eventGroupSet.add(new EventGroup(Set.of(LoadChunkEvent.class, ReadyToLoadChunkEvent.class)));
    }

    public static EventPublisher getInstance() {
        if (instance == null) {
            instance = new EventPublisher();
        }
        return instance;
    }

    public <T> void addListener(Class<T> eventType, EventHandler<T> listener) {
        listeners.computeIfAbsent(eventType, listenerList -> new ArrayList<>()).add(listener);
    }

    public <T> void publish(T event) {
        List<EventHandler<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventHandler<?> listener : eventListeners) {
                EventHandler<T> typedListener = (EventHandler<T>) listener;
                logger.debug("{} handleEvent", typedListener);
                typedListener.handleEvent(event);
            }
        }
    }

    public void addEventGroup(EventGroup eventGroup) {
        eventGroupSet.add(eventGroup);
    }

    public void publishToEventGroup(Event event) {
        logger.debug("event: {}", event);
        for (EventGroup eventGroup : eventGroupSet) {
            eventGroup.addEvent(event);
        }
    }

    public void fireGlContextTasks() {
        while (!glContextTasks.isEmpty()) {
            Event event = glContextTasks.poll();
            List<EventHandler<?>> eventListeners = listeners.get(event.getClass());
            if (eventListeners != null) {
                for (EventHandler listener : eventListeners) {
                    listener.handleEvent(event);
                }
            }
        }
    }
}
