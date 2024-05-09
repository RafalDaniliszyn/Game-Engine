package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.system.StackUpdater;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.isometric.utils.TileUtils;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class WorldMapData {
    private final FloorMap floorMap;
    private final GameData gameData;
    private final StackUpdater stackUpdater;

    public WorldMapData(GameData gameData) {
        this.gameData = gameData;

        this.floorMap = new FloorMap(gameData);
        this.stackUpdater = new StackUpdater(gameData, this);
    }

    public Optional<Deque<Long>[][]> getEntityIDsOnChunk(int floor, int tileX, int tileY) {
        Optional<Deque<Long>[][]> tileIdMapOptional = getTileIdMap(floor, tileX, tileY);
        if (tileIdMapOptional.isEmpty()) {
            return Optional.empty();
        }
        return tileIdMapOptional;
    }

    public List<EntityProperties> getEntityPropertiesList(int floor, int tileX, int tileY) {
        Deque<Long> entitiesId = getEntitiesOnTile(floor, tileX, tileY);
        List<EntityProperties> entityProperties = new LinkedList<>();
        entitiesId.forEach(id -> {
            Entity entity = gameData.getEntity(id);
            if (entity != null) {
                entityProperties.add(entity.getProperties());
            }
        });
        return entityProperties;
    }

    public void addEntityToTile(int floor, int tileX, int tileY, Long entityId, boolean isTerrain) {
        Optional<Deque<Long>[][]> tileIdMapOptional = getTileIdMap(floor, tileX, tileY);
        if (tileIdMapOptional.isEmpty()) {
            return;
        }

        Deque<Long>[][] tileIdQueue = tileIdMapOptional.get();
        Entity entity = gameData.getEntity(entityId);
        if (entity != null) {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            if (positionComponent != null) {
                positionComponent.setFloor(floor);
            }
        }

        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        if (!isTerrain) {
            TileUtils.addEntity(entityId, tileIdQueue[tilePosition.x()][tilePosition.y()]);
            floorMap.addEntityIdToFloor(entityId, floor);
            stackUpdater.updateStack(floor, tileX, tileY);
            return;
        }
        Long removed = TileUtils.replaceEntityOnBottom(entityId, tileIdQueue[tilePosition.x()][tilePosition.y()]);
        gameData.removeEntity(removed);
        floorMap.addEntityIdToFloor(entityId, floor);
    }


    /**
     * This method removes an entity from the tile based on the given entityId.
     * Note: chunkX and chunkY are not required as they are calculated from tileX and tileY.
     *
     * @param floor the floor of the tile
     * @param tileX the X coordinate of the tile
     * @param tileY the Y coordinate of the tile
     * @param entityId the ID of the entity to remove
     */
    public void removeEntityFromTile(int floor, int tileX, int tileY, Long entityId) {
        Optional<Deque<Long>[][]> tileIdMapOptional = getTileIdMap(floor, tileX, tileY);
        if (tileIdMapOptional.isEmpty()) {
            return;
        }
        Deque<Long>[][] tileIdQueue = tileIdMapOptional.get();

        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        TileUtils.removeEntityFromTile(entityId, tileIdQueue[tilePosition.x()][tilePosition.y()]);
        floorMap.removeEntityIdOnFloor(entityId, floor);

        Entity entity = gameData.getEntity(entityId);
        if (entity != null) {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            if (positionComponent != null) {
                positionComponent.setFloor(floor);
            }
        }
    }

    public Deque<Long> getEntitiesOnTile(int floor, int tileX, int tileY) {
        Deque<Long>[][] entityQueue = getTileInfoQueuesInChunk(floor, tileX, tileY);
        return TileUtils.getEntitiesOnTile(entityQueue, tileX, tileY);
    }

    public Long getTopEntityIdFromTile(int floor, int tileX, int tileY) {
        return getEntitiesOnTile(floor, tileX, tileY).peekLast();
    }

    public Long getBottomEntityIdFromTile(int floor, int tileX, int tileY) {
        return getEntitiesOnTile(floor, tileX, tileY).peekFirst();
    }

    private Optional<Deque<Long>[][]> getTileIdMap(int floor, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap != null && chunkMap.getChunks() != null && chunkMap.getChunks()[tilePosition.chunkX()][tilePosition.chunkY()] != null) {
            return Optional.of(floorMap.getFloorMap().get(floor).getChunks()[tilePosition.chunkX()][tilePosition.chunkY()].getEntitiesQueue());
        }
        return Optional.empty();
    }

    private Deque<Long>[][] getTileInfoQueuesInChunk(int floor, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        return floorMap.getFloorMap().get(floor).getChunks()[tilePosition.chunkX()][tilePosition.chunkY()].getEntitiesQueue();
    }

}
