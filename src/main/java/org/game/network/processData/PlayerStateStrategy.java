package org.game.network.processData;

import io.netty.channel.Channel;
import org.game.isometric.GameState;
import org.game.network.DataType;
import org.game.network.model.BaseModel;
import org.game.network.model.PlayerStateModel;

public class PlayerStateStrategy implements ProcessData {
    private static final DataType DATA_TYPE = DataType.PLAYER_STATE_MODEL;

    @Override
    public void onChannelReadProcess(Channel readChannel, BaseModel model) {
        PlayerStateModel playerState = (PlayerStateModel) model;
        if (GameState.getUserUuid().equals(playerState.getUserUuid())) {
            return;
        }
        GameState.addPlayerState(playerState);
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }
}
