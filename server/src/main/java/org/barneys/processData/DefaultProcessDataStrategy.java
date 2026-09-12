package org.barneys.processData;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import org.barneys.model.BaseModel;
import org.barneys.DataType;

public class DefaultProcessDataStrategy implements ProcessData {
    private static final DataType DATA_TYPE = DataType.DEFAULT;

    @Override
    public void onChannelReadProcess(Channel readChannel, ChannelGroup channelGroup, BaseModel model) {
        System.out.println("default process data strategy");
    }

    @Override
    public String getDataType() {
        return DATA_TYPE.getType();
    }
}
