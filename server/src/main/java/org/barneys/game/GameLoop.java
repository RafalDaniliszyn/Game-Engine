package org.barneys.game;

import org.barneys.worldMap.WorldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameLoop implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GameLoop.class);
    private final BlockingQueue<GameEvent> eventQueue = new LinkedBlockingQueue<>();
    private final WorldMap worldMap;
    private volatile boolean running = true;

    public GameLoop(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void run() {
        GameEvent event;
        while (running) {
            while ((event = eventQueue.poll()) != null) {
                event.execute(worldMap);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void submit(GameEvent event) {
        eventQueue.offer(event);
    }

    public BlockingQueue<GameEvent> getEventQueue() {
        return eventQueue;
    }

    public void stop() {
        running = false;
    }
}
