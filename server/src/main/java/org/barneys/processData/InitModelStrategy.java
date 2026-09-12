package org.barneys.processData;

import database.DatabaseConnector;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import org.barneys.*;
import org.barneys.model.BaseModel;
import org.barneys.model.Direction;
import org.barneys.model.InitModel;
import org.barneys.model.PlayerStateModel;
import org.barneys.server.PlayerChannelRegistry;
import org.barneys.server.modelHandler.PlayerInitDataModel;

import java.util.UUID;

public class InitModelStrategy implements ProcessData {
    private static final DataType DATA_TYPE = DataType.INIT_MODEL;

    @Override
    public void onChannelReadProcess(Channel readChannel, ChannelGroup channelGroup, BaseModel model) {
        InitModel initModel = (InitModel) model;
        UUID userUuid = initModel.getUserUuid();
        WorldState.registerUser(readChannel.id(), userUuid);
        PlayerChannelRegistry.register(userUuid, readChannel);
        KeySettings keySettings = new KeySettings(initModel.getInputMap());
        User user = DatabaseConnector.read(userUuid);
        if (user == null || user.getPlayerEntityModel() == null) {
            return;
        }
        user.setKeySettings(keySettings);
        PlayerEntityModel playerEntityModel = user.getPlayerEntityModel();

        PlayerStateModel playerStateModel = new PlayerStateModel(
                user.getUuid(),
                playerEntityModel.getName(),
                playerEntityModel.getPositionX(),
                playerEntityModel.getPositionY(),
                playerEntityModel.getFloor(),
                playerEntityModel.getTextureLabel(),
                Direction.UP
        );
        WorldState.users.put(user.getUuid(), user);
        channelGroup.writeAndFlush(playerStateModel);
        readChannel.writeAndFlush(
                new PlayerInitDataModel(
                        playerStateModel.getTileX(), playerStateModel.getTileY(),
                        playerStateModel.getFloor(), playerEntityModel.getSessionEntityId()));
        System.out.println(user);
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }

}
