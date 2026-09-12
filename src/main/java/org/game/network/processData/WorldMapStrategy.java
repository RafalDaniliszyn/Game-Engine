package org.game.network.processData;

import io.netty.channel.Channel;
import org.game.network.DataType;
import org.game.network.client.serverMap.MapLoader;
import org.game.network.model.BaseModel;
import org.game.network.model.WorldMapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class WorldMapStrategy implements ProcessData {

    private final List<ChunkLoaded> chunkLoadedList;
    private static final Logger logger = LoggerFactory.getLogger(WorldMapStrategy.class);

    public WorldMapStrategy() {
        this.chunkLoadedList = new ArrayList<>();
    }

    private static final DataType DATA_TYPE = DataType.WORLD_MAP_MODEL;
    @Override
    public void onChannelReadProcess(Channel readChannel, BaseModel model) {
        WorldMapModel mapModel = (WorldMapModel) model;
        logger.debug("received chunk: x: {}, y: {}, floor: {}", mapModel.getChunkX(), mapModel.getChunkY(), mapModel.getFloor());

        if (chunkLoadedList.stream()
                .noneMatch(chunkLoaded -> chunkLoaded.getX() == mapModel.getChunkX()
                        && chunkLoaded.getY() == mapModel.getChunkY()
                        && chunkLoaded.getFloor() == mapModel.getFloor())) {
            MapLoader.loadMap(mapModel);
            chunkLoadedList.add(new ChunkLoaded(mapModel.getChunkX(), mapModel.getChunkY(), mapModel.getFloor()));
        }
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }
}
