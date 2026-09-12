package org.game.network.processData;

import io.netty.channel.Channel;
import org.game.network.model.BaseModel;

public interface ProcessData {
    void onChannelReadProcess(Channel readChannel, BaseModel model);
    String getDataType();
}
