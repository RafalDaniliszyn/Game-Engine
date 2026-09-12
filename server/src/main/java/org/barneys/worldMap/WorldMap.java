package org.barneys.worldMap;

import game.isometric.entity.Entity;
import game.isometric.entity.EntityProperties;
import game.isometric.helper.IdGenerator;
import game.isometric.utils.PositionUtils;
import game.isometric.utils.TilePosition;
import game.isometric.utils.TileUtils;
import org.barneys.WorldState;
import org.barneys.model.EntityIdLabelDto;
import org.barneys.model.WorldMapModel;
import org.barneys.server.ConfigManager;
import org.barneys.server.NettyServer;
import org.barneys.worldMap.service.ChunkService;
import org.barneys.worldMap.service.ChunkServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testWFC.PixelWFC;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static game.isometric.WorldSettings.FLOORS;
import static org.barneys.worldMap.WorldMapUtils.createDir;

public class WorldMap {
    private static final Logger log = LoggerFactory.getLogger(WorldMap.class);
    private final Map<Long, Entity> entities =  Collections.synchronizedMap(new LinkedHashMap<>());
    private Chunk[] chunks;
    private final StackUpdater stackUpdater;
    private final ChunkService chunkService;
    private final int worldSize;


    public WorldMap() {
        this.worldSize = ConfigManager.config.WORLD_SIZE;
        this.stackUpdater = new StackUpdater(this);
        this.chunkService = new ChunkServiceImpl();
        if ("true".equals(NettyServer.INIT_MAP)) {
            createDir();
            initFloors();
            try {
                WorldMapUtils.saveMap(this.chunks);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        loadFloors();
    }

    private Chunk getNewChunk(int x, int y, int floor) {
        return new Chunk(x, y, floor);
    }

    public void initFloors() {
        chunks = new Chunk[worldSize * worldSize * FLOORS];
        int floor;
        PixelWFC pixelWFC = new PixelWFC();

        for (int i = 0; i < chunks.length; i++) {
            floor = i / (worldSize * worldSize);
            int shift = floor * worldSize;

            int x = i % worldSize;
            int y = (i / worldSize) - shift;

            chunks[i] = getNewChunk(x, y, floor);
            Deque<String>[][] filledChunk = pixelWFC.generateAll(floor);
            List<Entity> entityToAdd = chunkService.fillChunk(chunks[i], filledChunk);
            for (Entity entity : entityToAdd) {
                addEntity(entity);
            }
            log.info("Generating map... floor: {}, x: {}, y: {}", floor, x, y);
        }
    }

    public void loadFloors() {
        chunks = new Chunk[worldSize * worldSize * FLOORS];
        List<String> fileTree = WorldMapUtils.getFileTree("data");
        Map<Long, String> idLabelMap = WorldMapUtils.loadEntities();
        IdGenerator.importAllocatedIds(idLabelMap.keySet());

        for (int chunkIndex = 0; chunkIndex < fileTree.size(); chunkIndex++) {
            String file = fileTree.get(chunkIndex);
            Chunk loadedChunk = WorldMapUtils.loadChunk(file);
            chunks[chunkIndex] = getNewChunk(loadedChunk.getX(), loadedChunk.getY(), loadedChunk.getFloor());
            Deque<Long>[][] loadedEntitiesQueue = loadedChunk.getEntitiesQueue();

            Deque<String>[][] filledChunk = new ArrayDeque[loadedEntitiesQueue.length][loadedEntitiesQueue[0].length];
            for (int i = 0; i < loadedEntitiesQueue.length; i++) {
                for (int i1 = 0; i1 < loadedEntitiesQueue.length; i1++) {
                    filledChunk[i][i1] = new ArrayDeque<>();
                    for (Long id : loadedEntitiesQueue[i][i1]) {
                        if (idLabelMap.containsKey(id)) {
                            filledChunk[i][i1].add(idLabelMap.get(id));
                        }
                    }
                }
            }
            List<Entity> entityToAdd = chunkService.fillChunk(chunks[chunkIndex], filledChunk);
            for (Entity entity : entityToAdd) {
                addEntity(entity);
            }
        }
    }

    public Chunk[] getChunks() {
        return chunks;
    }

    public List<EntityProperties> getEntityPropertiesList(int floor, int tileX, int tileY) {
        Optional<Deque<Long>> entitiesId = getEntitiesOnTile(floor, tileX, tileY);
        if (entitiesId.isPresent()) {
            List<EntityProperties> entityProperties = new LinkedList<>();
            entitiesId.get().forEach(id -> {
                Entity entity = entities.get(id);
                if (entity != null) {
                    entityProperties.add(entity.getProperties());
                }
            });
            return entityProperties;
        }
        return new ArrayList<>();
    }

    public void addEntityToTile(int floor, int tileX, int tileY, Entity entity, boolean isTerrain) {
        Optional<Deque<Long>[][]> tileIdMapOptional = getTileIdMap(floor, tileX, tileY);
        if (tileIdMapOptional.isEmpty()) {
            return;
        }

        Deque<Long>[][] tileIdQueue = tileIdMapOptional.get();
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        if (!isTerrain) {
            TileUtils.addEntity(entity.getId(), tileIdQueue[tilePosition.x()][tilePosition.y()]);
            entities.put(entity.getId(), entity);
            stackUpdater.updateStack(floor, tileX, tileY);
            return;
        }
        Long removed = TileUtils.replaceEntityOnBottom(entity.getId(), tileIdQueue[tilePosition.x()][tilePosition.y()]);
        entities.remove(removed);
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
    }

    public Optional<Deque<Long>> getEntitiesOnTile(int floor, int tileX, int tileY) {
        Optional<Deque<Long>[][]> entityQueue = getTileIdMap(floor, tileX, tileY);
        return entityQueue.map(queue -> TileUtils.getEntitiesOnTile(queue, tileX, tileY));
    }

    public Long getTopEntityIdFromTile(int floor, int tileX, int tileY) {
        Optional<Deque<Long>> entitiesOnTile = getEntitiesOnTile(floor, tileX, tileY);
        return entitiesOnTile.map(Deque::peekLast).orElse(null);
    }

    public Long getBottomEntityIdFromTile(int floor, int tileX, int tileY) {
        Optional<Deque<Long>> entitiesOnTile = getEntitiesOnTile(floor, tileX, tileY);
        return entitiesOnTile.map(Deque::peekFirst).orElse(null);
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }

    public Entity getEntity(long entityId) {
        return entities.get(entityId);
    }

    public void removeEntity(Long entityId) {
        entities.remove(entityId);
    }

    public List<WorldMapModel> getWorldMapModelList() {
        List<WorldMapModel> worldMapModelList = new ArrayList<>();
        for (Chunk chunk : chunks) {
            Deque<Long>[][] entitiesQueue = chunk.getEntitiesQueue();
            Deque<String>[][] labelsQueue = mapIdToLabelDeque(entitiesQueue);
            Deque<EntityIdLabelDto>[][] idLabelDeque = toEntityIdLabelDeque(entitiesQueue);
            WorldMapModel worldMapModel = new WorldMapModel(chunk.getFloor(), chunk.getX(), chunk.getY(), idLabelDeque, false);
            worldMapModelList.add(worldMapModel);
        }

        worldMapModelList.forEach(worldMapModel -> {
            Deque<EntityIdLabelDto>[][] entitiesQueue = worldMapModel.getEntitiesQueue();
            int tiles = 0;
            for (int i = 0; i < entitiesQueue.length; i++) {
                for (int i1 = 0; i1 < entitiesQueue.length; i1++) {
                    if (!entitiesQueue[i][i1].isEmpty()) {
                        tiles += 1;
                    }
                }
            }
            System.out.println("WorldMapModel{" +
                    "floor=" + worldMapModel.getFloor() +
                    ", chunkX=" + worldMapModel.getChunkX() +
                    ", chunkY=" + worldMapModel.getChunkY() +
                    ", tiles=" + tiles +
                    "} ");
        });
        return worldMapModelList;
    }

    private Deque<EntityIdLabelDto>[][] toEntityIdLabelDeque(Deque<Long>[][] entitiesQueue) {
        Deque<EntityIdLabelDto>[][] result = new ArrayDeque[entitiesQueue.length][entitiesQueue.length];
        for (int i = 0; i < entitiesQueue.length; i++) {
            for (int j = 0; j < entitiesQueue.length; j++) {
                result[i][j] = new ArrayDeque<>();
            }
        }
        for (int i = 0; i < entitiesQueue.length; i++) {
            for (int j = 0; j < entitiesQueue.length; j++) {

                for (Long id : entitiesQueue[i][j]) {
                    Entity entity = WorldState.entityMapById.get(id);
                    if (entity != null) {
                        result[i][j].offerFirst(new EntityIdLabelDto(entity.getProperties().getLabel(), id));
                    }
                }

            }
        }
        return result;
    }

    private Deque<String>[][] mapIdToLabelDeque(Deque<Long>[][] entitiesQueue) {
        Deque<String>[][] result = new ArrayDeque[entitiesQueue.length][entitiesQueue.length];
        for (int i = 0; i < entitiesQueue.length; i++) {
            for (int j = 0; j < entitiesQueue.length; j++) {
                result[i][j] = new ArrayDeque<>();
            }
        }
        for (int i = 0; i < entitiesQueue.length; i++) {
            for (int j = 0; j < entitiesQueue.length; j++) {

                for (Long id : entitiesQueue[i][j]) {
                    Entity entity = WorldState.entityMapById.get(id);
                    if (entity != null) {
                        result[i][j].offerFirst(entity.getProperties().getLabel() + "|" + id);
                    }
                }

            }
        }
        return result;
    }


    private Optional<Deque<Long>[][]> getTileIdMap(int floor, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);

        return Arrays.stream(chunks)
                .filter(chunk -> chunk.getFloor() == floor && chunk.getX() == tilePosition.chunkX() && chunk.getY() == tilePosition.chunkY())
                .findFirst().map(Chunk::getEntitiesQueue);
    }

    public Map<Long, Entity> getEntities() {
        return entities;
    }
}
