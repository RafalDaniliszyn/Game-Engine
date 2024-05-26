package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.worldMap.WorldMapData;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CollisionSystem2D extends BaseSystem {

    public CollisionSystem2D(GameData gameData) {
        super(gameData);
        addRequiredComponent(ComponentEnum.CollisionComponent2D, ComponentEnum.PositionComponent2D, ComponentEnum.MoveComponent2D);
    }

    @Override
    public void update(float deltaTime) {
        GameData gameData = getGameData();
        WorldMapData worldMapData = gameData.getWorldMapData();

        getEntitiesToProcess().forEach(id -> {
            Entity entity = gameData.getEntity(id);
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            Vector2f position = positionComponent.getPosition();
            MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);
            float tileSize = WorldSettings.TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH;
            int tileXToCheck = (int) (position.x / tileSize);
            int tileYToCheck = (int) (position.y / tileSize);
            AtomicReference<Boolean> anyCollisionLeft = new AtomicReference<>(false);
            AtomicReference<Boolean> anyCollisionRight = new AtomicReference<>(false);
            AtomicReference<Boolean> anyCollisionUp = new AtomicReference<>(false);
            AtomicReference<Boolean> anyCollisionDown = new AtomicReference<>(false);

            int currentFloor = GameState.getCurrentFloor();
            Optional<Deque<Long>> entitiesOnTileLeft = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck - 1, tileYToCheck);
            entitiesOnTileLeft.ifPresent(ids -> ids.forEach(entityLeft -> {
                boolean collidable = gameData.getEntity(entityLeft).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.LEFT, true);
                    anyCollisionLeft.set(true);
                }
            }));

            Optional<Deque<Long>> entitiesOnTileRight = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck + 1, tileYToCheck);
            entitiesOnTileRight.ifPresent(ids -> ids.forEach(entityRight -> {
                boolean collidable = gameData.getEntity(entityRight).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.RIGHT, true);
                    anyCollisionRight.set(true);
                }
            }));

            Optional<Deque<Long>> entitiesOnTileDown = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck, tileYToCheck - 1);
            entitiesOnTileDown.ifPresent(ids -> ids.forEach(entityDown -> {
                boolean collidable = gameData.getEntity(entityDown).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.DOWN, true);
                    anyCollisionDown.set(true);
                }
            }));

            Optional<Deque<Long>> entitiesOnTileUp = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck, tileYToCheck + 1);
            entitiesOnTileUp.ifPresent(ids -> ids.forEach(entityUp -> {
                boolean collidable = gameData.getEntity(entityUp).getProperties().isCollidable();
                if (collidable) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.UP, true);
                    anyCollisionUp.set(true);
                }
            }));

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
