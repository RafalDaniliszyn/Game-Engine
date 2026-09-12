package org.game.network.processData;

import io.netty.channel.Channel;
import org.game.isometric.GameState;
import org.game.network.DataType;
import org.game.network.model.BaseModel;

public class ChannelActiveStrategy implements ProcessData {
    private static final DataType DATA_TYPE = DataType.CHANNEL_ACTIVE_MODEL;

    @Override
    public void onChannelReadProcess(Channel readChannel, BaseModel model) {
        GameState.setOnline();
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }
}
