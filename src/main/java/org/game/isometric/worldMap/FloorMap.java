package org.game.isometric.worldMap;

import org.game.GameData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.game.isometric.WorldSettings.FLOORS;

public class FloorMap {

    private final Map<Integer, ChunkMap> floorMap;

    /**
     * Key - floor
     * Value - List with entity ids
     */
    private final Map<Integer, List<Long>> entityIdMap;

    public FloorMap(GameData gameData) {
        floorMap = new HashMap<>();
        this.entityIdMap = new HashMap<>();
        for (int i = 0; i < FLOORS; i++) {
            entityIdMap.put(i, new ArrayList<>());
            ChunkMap chunkMap = new ChunkMap(gameData);
            Chunk[][] chunks = chunkMap.getChunks();
            for (Chunk[] chunk : chunks) {
                for (int j = 0; j < chunks.length; j++) {
                    List<Long> entityIdList = chunk[j].fillChunk(i);
                    entityIdMap.get(i).addAll(entityIdList);
                }
            }
            floorMap.put(i, chunkMap);
        }
    }

    public Map<Integer, ChunkMap> getFloorMap() {
        return floorMap;
    }

    public void addEntityIdToFloor(Long entityId, Integer floor) {
        if (entityId == null || floor < 0 || floor > FLOORS) {
            return;
        }
        entityIdMap.get(floor).add(entityId);
    }

    public void removeEntityIdOnFloor(Long entityId, Integer floor) {
        if (entityId == null || floor < 0 || floor > FLOORS) {
            return;
        }
        entityIdMap.get(floor).remove(entityId);
    }
}
