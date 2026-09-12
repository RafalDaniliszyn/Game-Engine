package org.barneys.game;

import org.barneys.worldMap.WorldMap;

public class PickupItemEvent implements GameEvent {
    int x;
    int y;

    public PickupItemEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(WorldMap worldMap) {
        System.out.println("PickupItemEvent: " + x + " " + y);
    }
}
