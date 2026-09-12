package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.isometric.event.EventHandler;
import org.game.isometric.event.EventPublisher;
import org.game.isometric.event.LoadChunkEvent;
import org.game.isometric.event.ReadyToLoadChunkEvent;
import org.game.network.model.WorldMapModel;
import java.util.HashMap;
import java.util.Map;

public class FloorMap {

    private final Map<Integer, ChunkMap> floorMap;

    public FloorMap(GameData gameData) {
        floorMap = new HashMap<>();
        EventPublisher.getInstance().addListener(LoadChunkEvent.class, new EventHandler<LoadChunkEvent>() {
            @Override
            public void handleEvent(LoadChunkEvent event) {
                WorldMapModel worldMapModel = event.getWorldMapModel();
                int floor = worldMapModel.getFloor();
                if (floorMap.containsKey(floor)) {
                    Chunk[][] chunks = floorMap.get(floor).getChunks();
                    int chunkX = worldMapModel.getChunkX();
                    int chunkY = worldMapModel.getChunkY();
                    chunks[chunkX][chunkY].fillChunk(worldMapModel);
                } else {
                    ChunkMap chunkMap = new ChunkMap(gameData);
                    Chunk[][] chunks = chunkMap.getChunks();
                    int chunkX = worldMapModel.getChunkX();
                    int chunkY = worldMapModel.getChunkY();
                    chunks[chunkX][chunkY].fillChunk(worldMapModel);
                    floorMap.put(floor, chunkMap);
                }

            }
        });
        EventPublisher.getInstance().publishToEventGroup(new ReadyToLoadChunkEvent());
    }

    public Map<Integer, ChunkMap> getFloorMap() {
        return floorMap;
    }
}
