package org.game.network.processData;

import io.netty.channel.Channel;
import org.game.network.DataType;
import org.game.network.model.BaseModel;

public class DefaultProcessDataStrategy implements ProcessData {
    private static final DataType DATA_TYPE = DataType.DEFAULT;

    @Override
    public void onChannelReadProcess(Channel readChannel, BaseModel model) {
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }
}
