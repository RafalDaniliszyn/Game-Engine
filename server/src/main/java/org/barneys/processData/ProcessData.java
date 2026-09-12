package org.barneys.processData;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import org.barneys.model.BaseModel;

public interface ProcessData {
    void onChannelReadProcess(Channel readChannel, ChannelGroup channelGroup, BaseModel model);
    String getDataType();
}
