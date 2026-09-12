package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityType;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.EntityMapper;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.system.SwapEdgeHelper;
import org.game.isometric.utils.TileUtils;
import org.game.network.client.DataSync;
import org.game.network.client.incomingDataHandler.fileTransfer.FileTransferRequestModel;
import org.game.network.model.EntityIdLabelDto;
import org.game.network.model.WorldMapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.game.isometric.WorldSettings.CHUNK_SIZE;

public class Chunk {
    private static final Logger log = LoggerFactory.getLogger(Chunk.class);
    private final Deque<Long>[][] entitiesQueue;
    private final Deque<Side>[][] edgeMap;
    private final Set<String> textureRequestSet;
    private final GameData gameData;
    private final int chunkX;
    private final int chunkY;

    public Chunk(GameData gameData, Integer chunkX, Integer chunkY) {
        this.gameData = gameData;
        this.chunkX = chunkX;
        this.chunkY = chunkY;

        this.edgeMap = new ArrayDeque[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                this.edgeMap[i][j] = new ArrayDeque<>();
            }
        }

        this.entitiesQueue = new ArrayDeque[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                this.entitiesQueue[i][j] = new ArrayDeque<>();
            }
        }
        this.textureRequestSet = new HashSet<>();
    }

    public void fillChunk(WorldMapModel mapModel) {
        log.debug("fillChunk floor: {}, X: {}, Y: {}", mapModel.getFloor(), mapModel.getChunkX(), mapModel.getChunkY());

        Deque<EntityIdLabelDto>[][] entitiesQue = mapModel.getEntitiesQueue();
        for (int i = 0; i < entitiesQue.length; i++) {
            for (int j = 0; j < entitiesQue.length; j++) {
                for (EntityIdLabelDto entityData : entitiesQue[i][j]) {
                    String label = entityData.getLabel();
                    long id = entityData.getId();

                    Entity entityBase = BlocksReader.getEntity(label);
                    if (entityBase == null) {
                        entityBase = BlocksReader.getEntity(label);
                    }

                    if (entityBase.getId() != id) {
                        long currentId = entityBase.getId();
                        gameData.removeEntity(currentId);
                        entityBase.setId(id);
                        gameData.addEntity(entityBase);
                    }

                    TileUtils.addEntityOnBottom(id, entitiesQueue[i][j]);
                    //SwapEdgeHelper.changeAround(entitiesQueue, i, j);
                    SwapEdgeHelper.changeEdges(edgeMap, entitiesQueue, i, j);
                }
            }
        }
    }

    public Deque<Long>[][] getEntitiesQueue() {
        return entitiesQueue;
    }

    public Deque<Side>[][] getEdgeMap() {
        return edgeMap;
    }
}
