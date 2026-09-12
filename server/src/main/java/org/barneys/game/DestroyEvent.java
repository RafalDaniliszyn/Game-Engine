package org.barneys.game;

import org.barneys.worldMap.WorldMap;

public class DestroyEvent implements GameEvent {

    private final long playerId;

    public DestroyEvent(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public void execute(WorldMap worldMap) {
        System.out.println("testEventSystem: " + playerId);
    }
}
