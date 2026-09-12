package org.game.network.client.serverMap;

import org.game.isometric.event.EventPublisher;
import org.game.isometric.event.LoadChunkEvent;
import org.game.network.model.WorldMapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    private static final Logger log = LoggerFactory.getLogger(MapLoader.class);
    public static boolean mapReceived = false;
    public static List<WorldMapModel> mapModelList;


    static {
        mapModelList = new ArrayList<>();
    }

    public static void loadMap(WorldMapModel mapModel) {
        loadIncomingChunk(mapModel);
    }

    private static void loadIncomingChunk(WorldMapModel mapModel) {
        if (mapModel.isComplete()) {
            log.info("LoadChunkEvent toEventGroup: [Chunk x: {}, y: {}, floor: {}]", mapModel.getChunkX(), mapModel.getChunkY(), mapModel.getFloor());
            EventPublisher.getInstance().publishToEventGroup(new LoadChunkEvent(mapModel));
            mapReceived = true;
        }
    }

}
