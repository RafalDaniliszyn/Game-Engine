package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.system.StackUpdater;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.isometric.utils.TileUtils;

import java.util.*;

import static org.game.isometric.WorldSettings.CHUNK_SIZE;
import static org.game.isometric.WorldSettings.WORLD_SIZE;

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
        Optional<Deque<Long>> entitiesId = getEntitiesOnTile(floor, tileX, tileY);
        if (entitiesId.isPresent()) {
            List<EntityProperties> entityProperties = new LinkedList<>();
            entitiesId.get().forEach(id -> {
                Entity entity = gameData.getEntity(id);
                if (entity != null) {
                    entityProperties.add(entity.getProperties());
                }
            });
            return entityProperties;
        }
        return new ArrayList<>();
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
            stackUpdater.updateStack(floor, tileX, tileY);
            return;
        }
        Long removed = TileUtils.replaceEntityOnBottom(entityId, tileIdQueue[tilePosition.x()][tilePosition.y()]);
        gameData.removeEntity(removed);
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

        Entity entity = gameData.getEntity(entityId);
        if (entity != null) {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            if (positionComponent != null) {
                positionComponent.setFloor(floor);
            }
        }
    }

    public Optional<Deque<Long>> getEntitiesOnTile(int floor, int tileX, int tileY) {
        Optional<Deque<Long>[][]> entityQueue = getTileInfoQueuesInChunk(floor, tileX, tileY);
        return entityQueue.map(queue -> TileUtils.getEntitiesOnTile(queue, tileX, tileY));
    }

    public Optional<Deque<Side>> getEdgesQueue(int floor, int tileX, int tileY) {
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap == null || chunkMap.getChunks() == null) {
            return Optional.empty();
        }
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        Chunk chunk = chunkMap.getChunks()[tilePosition.chunkX()][tilePosition.chunkY()];
        return Optional.ofNullable(chunk.getEdgeMap()[tilePosition.x()][tilePosition.y()]);
    }

    public Long getTopEntityIdFromTile(int floor, int tileX, int tileY) {
        Optional<Deque<Long>> entitiesOnTile = getEntitiesOnTile(floor, tileX, tileY);
        return entitiesOnTile.map(Deque::peekLast).orElse(null);
    }

    public Long getBottomEntityIdFromTile(int floor, int tileX, int tileY) {
        Optional<Deque<Long>> entitiesOnTile = getEntitiesOnTile(floor, tileX, tileY);
        return entitiesOnTile.map(Deque::peekFirst).orElse(null);
    }

    public Optional<Deque<Long>[][]> getIdQueuesOnChunk(int floor, int chunkX, int chunkY) {
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap != null && chunkMap.getChunks() != null && chunkX < chunkMap.getChunks().length
                && chunkY < chunkMap.getChunks().length && chunkMap.getChunks()[chunkX][chunkY] != null) {
            return Optional.of(floorMap.getFloorMap().get(floor).getChunks()[chunkX][chunkY].getEntitiesQueue());
        }
        return Optional.empty();
    }

    public Optional<Deque<Side>[][]> getEdgeQueues(int floor) {
        Deque<Side>[][] edgeMapCopy = new ArrayDeque[WORLD_SIZE * CHUNK_SIZE][WORLD_SIZE * CHUNK_SIZE];
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap == null || chunkMap.getChunks() == null) {
            return Optional.empty();
        }
        Chunk[][] chunks = chunkMap.getChunks();
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks.length; j++) {
                Deque<Side>[][] edgeMap = chunks[i][j].getEdgeMap();
                for (int i1 = 0; i1 < edgeMap.length; i1++) {
                    for (int j1 = 0; j1 < edgeMap.length; j1++) {
                        int x = i * CHUNK_SIZE + i1;
                        int y = j * CHUNK_SIZE + j1;
                        if (edgeMap[i1][j1] != null) {
                            edgeMapCopy[x][y] = new ArrayDeque<>(edgeMap[i1][j1]);
                        }
                    }
                }
            }
        }
        return Optional.of(edgeMapCopy);
    }

    public Optional<Deque<Long>[][]> getIdQueues(int floor) {
        Deque<Long>[][] map = new ArrayDeque[WORLD_SIZE * CHUNK_SIZE][WORLD_SIZE * CHUNK_SIZE];
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap == null || chunkMap.getChunks() == null) {
            return Optional.empty();
        }
        Chunk[][] chunks = chunkMap.getChunks();
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks.length; j++) {
                Deque<Long>[][] entitiesQueue = chunks[i][j].getEntitiesQueue();
                for (int i1 = 0; i1 < entitiesQueue.length; i1++) {
                    for (int j1 = 0; j1 < entitiesQueue.length; j1++) {
                        int x = i * CHUNK_SIZE + i1;
                        int y = j * CHUNK_SIZE + j1;
                        if (entitiesQueue[i1][j1] != null) {
                            map[x][y] = new ArrayDeque<>(entitiesQueue[i1][j1]);
                        }
                    }
                }
            }
        }
        return Optional.of(map);
    }

    private Optional<Deque<Long>[][]> getTileIdMap(int floor, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap != null && chunkMap.getChunks() != null && chunkMap.getChunks()[tilePosition.chunkX()][tilePosition.chunkY()] != null) {
            return Optional.of(floorMap.getFloorMap().get(floor).getChunks()[tilePosition.chunkX()][tilePosition.chunkY()].getEntitiesQueue());
        }
        return Optional.empty();
    }

    private Optional<Deque<Long>[][]> getTileInfoQueuesInChunk(int floor, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        ChunkMap chunkMap = floorMap.getFloorMap().get(floor);
        if (chunkMap != null && chunkMap.getChunks() != null && tilePosition.chunkX() < chunkMap.getChunks().length && tilePosition.chunkY() < chunkMap.getChunks().length) {
            return Optional.of(chunkMap.getChunks()[tilePosition.chunkX()][tilePosition.chunkY()].getEntitiesQueue());
        }
        return Optional.empty();
    }

}
