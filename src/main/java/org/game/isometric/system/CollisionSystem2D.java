package org.game.isometric.system;

import org.game.GameData;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.CollisionComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.worldMap.WorldMapData;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

public class CollisionSystem2D extends BaseSystem {

    public CollisionSystem2D(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        WorldMapData worldMapData = getGameData().getWorldMapData();
        getGameData().getEntities(CollisionComponent2D.class, PositionComponent2D.class, MoveComponent2D.class).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            Vector2f position = positionComponent.getPosition();
            MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);

            int tileXToCheck = (int) (position.x / WorldSettings.TILE_SIZE);
            int tileYToCheck = (int) (position.y / WorldSettings.TILE_SIZE);
            AtomicReference<Boolean> anyCollisionLeft = new AtomicReference<>(false);
            AtomicReference<Boolean> anyCollisionRight = new AtomicReference<>(false);
            AtomicReference<Boolean> anyCollisionUp = new AtomicReference<>(false);
            AtomicReference<Boolean> anyCollisionDown = new AtomicReference<>(false);

            int currentFloor = GameState.getCurrentFloor();
            Deque<Long> entitiesOnTileLeft = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck - 1, tileYToCheck);
            entitiesOnTileLeft.forEach(entityLeft -> {
                boolean collidable = getGameData().getEntity(entityLeft).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.LEFT, true);
                    anyCollisionLeft.set(true);
                }
            });
            Deque<Long> entitiesOnTileRight = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck + 1, tileYToCheck);
            entitiesOnTileRight.forEach(entityRight -> {
                boolean collidable = getGameData().getEntity(entityRight).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.RIGHT, true);
                    anyCollisionRight.set(true);
                }
            });
            Deque<Long> entitiesOnTileDown = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck, tileYToCheck - 1);
            entitiesOnTileDown.forEach(entityDown -> {
                boolean collidable = getGameData().getEntity(entityDown).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.DOWN, true);
                    anyCollisionDown.set(true);
                }
            });
            Deque<Long> entitiesOnTileUp = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck, tileYToCheck + 1);
            entitiesOnTileUp.forEach(entityUp -> {
                boolean collidable = getGameData().getEntity(entityUp).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.UP, true);
                    anyCollisionUp.set(true);
                }
            });

            moveComponent.setDirectionBlocked(MoveComponent2D.Direction.UP, anyCollisionUp.get());
            moveComponent.setDirectionBlocked(MoveComponent2D.Direction.DOWN, anyCollisionDown.get());
            moveComponent.setDirectionBlocked(MoveComponent2D.Direction.LEFT, anyCollisionLeft.get());
            moveComponent.setDirectionBlocked(MoveComponent2D.Direction.RIGHT, anyCollisionRight.get());
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
