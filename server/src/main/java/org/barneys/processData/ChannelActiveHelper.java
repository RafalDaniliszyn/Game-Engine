package org.barneys.processData;

import game.isometric.utils.PositionUtils;
import game.isometric.utils.TilePosition;
import io.netty.channel.Channel;
import org.barneys.PlayerEntityModel;
import org.barneys.WorldState;
import org.barneys.model.ChannelActiveModel;
import org.barneys.model.WorldMapModel;
import org.barneys.server.PlayerChannelRegistry;
import org.barneys.worldMap.WorldMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChannelActiveHelper {
    public static void sendOnActiveData(Channel channel) {
        ChannelActiveModel model = WorldState.channelActiveModel;
        System.out.println(model);
        channel.writeAndFlush(model);
        WorldMap worldMap = WorldState.getWorldMap();
        List<WorldMapModel> worldMapModelList = worldMap.getWorldMapModelList();
        for (int i = 0; i < worldMapModelList.size(); i++) {
            WorldMapModel worldMapModel = worldMapModelList.get(i);
            Optional<UUID> uuid = PlayerChannelRegistry.getPlayerUuid(channel);
            if (uuid.isPresent()) {
                PlayerEntityModel playerEntityModel = WorldState.getByUuid(uuid.get()).getPlayerEntityModel();
                Integer x = playerEntityModel.getPositionX();
                Integer y = playerEntityModel.getPositionY();
                Integer floor = playerEntityModel.getFloor();
                TilePosition playerTilePos = PositionUtils.getTilePosition(x, y);
                if (worldMapModel.getFloor() == floor
                                && worldMapModel.getChunkX() == playerTilePos.chunkX()
                                && worldMapModel.getChunkY() == playerTilePos.chunkY()) {
                    worldMapModel.setComplete(true);
                    channel.writeAndFlush(worldMapModel);
                }
            }
        }
    }
}
