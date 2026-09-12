package org.barneys.game;

import org.barneys.worldMap.WorldMap;

public interface GameEvent {
    void execute(WorldMap worldMap);
}
