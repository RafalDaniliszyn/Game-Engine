package org.barneys.processData.gameStateProcess.move;

import game.isometric.entity.Entity;
import org.barneys.WorldState;
import org.barneys.worldMap.WorldMap;
import java.util.Deque;
import java.util.Optional;

public class CollisionChecker {
    private final WorldMap worldMap;

    public CollisionChecker() {
        this.worldMap = WorldState.getWorldMap();
    }

    public boolean checkCollision(int x, int y, int floor) {
        Optional<Deque<Long>> entitiesOnTileOptional = worldMap.getEntitiesOnTile(floor, x, y);
        if (entitiesOnTileOptional.isEmpty()) {
            return true;
        }
        Deque<Long> ids = entitiesOnTileOptional.get();
        for (Long id : ids) {
            Entity entity = worldMap.getEntity(id);
            if (entity == null) {
                continue;
            }
            boolean collidable = entity.getProperties().isCollidable();
            if (collidable) {
                return true;
            }
        }
        return false;
    }
}
