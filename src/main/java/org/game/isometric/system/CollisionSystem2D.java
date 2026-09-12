package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
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
            int range = 3;
            boolean[][] collisionMap = scanTiles(tileXToCheck, tileYToCheck, currentFloor, range);

            if (collisionMap[range][range-1]) {
                moveComponent.setDirectionBlocked(MoveComponent2D.Direction.UP, true);
                anyCollisionUp.set(true);
            }
            if (collisionMap[range][range+1]) {
                moveComponent.setDirectionBlocked(MoveComponent2D.Direction.DOWN, true);
                anyCollisionDown.set(true);
            }
            if (collisionMap[range-1][range]) {
                moveComponent.setDirectionBlocked(MoveComponent2D.Direction.LEFT, true);
                anyCollisionLeft.set(true);
            }
            if (collisionMap[range+1][range]) {
                moveComponent.setDirectionBlocked(MoveComponent2D.Direction.RIGHT, true);
                anyCollisionRight.set(true);
            }

            Optional<Deque<Long>> entitiesOnTileLeft = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck - 1, tileYToCheck);
            entitiesOnTileLeft.ifPresent(ids -> ids.forEach(entityLeft -> {
                if (gameData.getEntity(entityLeft) != null && gameData.getEntity(entityLeft).getProperties().isCollidable()) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.LEFT, true);
                    anyCollisionLeft.set(true);
                }
            }));

            Optional<Deque<Long>> entitiesOnTileRight = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck + 1, tileYToCheck);
            entitiesOnTileRight.ifPresent(ids -> ids.forEach(entityRight -> {
                if (gameData.getEntity(entityRight) != null && gameData.getEntity(entityRight).getProperties().isCollidable()) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.RIGHT, true);
                    anyCollisionRight.set(true);
                }
            }));

            Optional<Deque<Long>> entitiesOnTileDown = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck, tileYToCheck - 1);
            entitiesOnTileDown.ifPresent(ids -> ids.forEach(entityDown -> {
                if (gameData.getEntity(entityDown) != null && gameData.getEntity(entityDown).getProperties().isCollidable()) {
                    moveComponent.setDirectionBlocked(MoveComponent2D.Direction.DOWN, true);
                    anyCollisionDown.set(true);
                }
            }));

            Optional<Deque<Long>> entitiesOnTileUp = worldMapData.getEntitiesOnTile(currentFloor, tileXToCheck, tileYToCheck + 1);
            entitiesOnTileUp.ifPresent(ids -> ids.forEach(entityUp -> {
                if (gameData.getEntity(entityUp) != null && gameData.getEntity(entityUp).getProperties().isCollidable()) {
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

    private boolean[][] scanTiles(int x, int y, int floor, int range) {
        GameData gameData = getGameData();
        WorldMapData worldMapData = gameData.getWorldMapData();

        int xAbs = x - range;
        int yAbs = y + range;

        boolean[][] collisionMap = new boolean[1 + (range * 2)][1 + (range * 2)];

        for (int i = 0; i <= range * 2; i++) {
            for (int j = 0; j <= range * 2; j++) {
                int tileX = xAbs + i;
                int tileY = yAbs - j;
                Optional<Deque<Long>> entities = worldMapData.getEntitiesOnTile(floor, tileX, tileY);
                int finalI = i;
                int finalJ = j;
                entities.ifPresent(idQueue -> idQueue.forEach(id -> {
                    Entity entity = gameData.getEntity(id);
                    if (entity == null) {
                        return;
                    }
                    boolean[][] collisionZone = entity.getProperties().getCollisionZone();
                    if (collisionZone != null) {
                        for (int i1 = 0; i1 < collisionZone.length; i1++) {
                            for (int j1 = 0; j1 < collisionZone[0].length; j1++) {
                                if (collisionZone[i1][j1]) {
                                    int yIndexOffset = collisionZone[0].length;
                                    int localX = i1 + finalI;
                                    int localY = (yIndexOffset - (j1 + 1)) + finalJ;
                                    if (localX < collisionMap.length && localY < collisionMap[0].length) {
                                        collisionMap[localX][localY] = true;
                                    }
                                }
                            }
                        }
                    }
                }));
            }
        }
        return collisionMap;
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
