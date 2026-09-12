package org.barneys.processData.gameStateProcess;

import game.isometric.utils.PositionUtils;
import game.isometric.utils.TilePosition;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import org.barneys.DataType;
import org.barneys.Input;
import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.WorldState;
import org.barneys.model.BaseModel;
import org.barneys.model.GameStateModel;
import org.barneys.model.WorldMapModel;
import org.barneys.processData.ProcessData;
import org.barneys.processData.gameStateProcess.move.MoveStrategy;
import org.barneys.worldMap.WorldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GameStateStrategy implements ProcessData {
    private static final DataType DATA_TYPE = DataType.GAME_STATE_MODEL;
    private static final Logger log = LoggerFactory.getLogger(GameStateStrategy.class);


    @Override
    public void onChannelReadProcess(Channel readChannel, ChannelGroup channelGroup, BaseModel model) {
        GameStateModel gameStateModel = (GameStateModel) model;
        log.info("received: {}", gameStateModel);

        sendChunk(gameStateModel, readChannel);
        PlayerActionStrategy strategy = getStrategy(gameStateModel.getInput());
        if (strategy != null) {
            strategy.execute(gameStateModel);
        }
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }

    private PlayerActionStrategy getStrategy(Input input) {
        switch (input) {
            case MOVE_FORWARD, MOVE_LEFT, MOVE_BACKWARD, MOVE_RIGHT -> {
                return new MoveStrategy();
            }
        }
        return null;
    }

    private void sendChunk(GameStateModel gameStateModel, Channel channel) {
        if (gameStateModel != null && WorldState.getByUuid(gameStateModel.getUserUuid()) != null) {
            User user = WorldState.getByUuid(gameStateModel.getUserUuid());
            int clientX = gameStateModel.getTileX();
            int clientY = gameStateModel.getTileY();
            PlayerEntityModel playerEntityModel = user.getPlayerEntityModel();
            Integer positionX = playerEntityModel.getPositionX();
            Integer positionY = playerEntityModel.getPositionY();
            TilePosition tilePos = PositionUtils.getTilePosition(positionX, positionY);
            TilePosition updatedPos = PositionUtils.getTilePosition(clientX, clientY);

            int clientFloor = gameStateModel.getFloor();
            if (playerEntityModel.getLastFloor() == -1 || tilePos.chunkX() != updatedPos.chunkX() || tilePos.chunkY() != updatedPos.chunkY() || playerEntityModel.getLastFloor() != clientFloor) {
                WorldMapModel worldMapModel = getWorldMapModel(updatedPos.chunkX(), updatedPos.chunkY(), clientFloor);
                if (worldMapModel != null) {
                    channel.writeAndFlush(worldMapModel);
                    playerEntityModel.setLastFloor(clientFloor);
                }
            }
        }

    }

    private WorldMapModel getWorldMapModel(int x, int y, int floor) {
        WorldMap worldMap = WorldState.getWorldMap();
        List<WorldMapModel> worldMapModelList = worldMap.getWorldMapModelList();
        for (WorldMapModel worldMapModel : worldMapModelList) {
            if (worldMapModel.getChunkX() == x && worldMapModel.getChunkY() == y && worldMapModel.getFloor() == floor) {
                worldMapModel.setComplete(true);
                return worldMapModel;
            }
        }
        return null;
    }

}
