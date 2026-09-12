package org.game.network.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.game.network.client.incomingDataHandler.ItemSpawnMessage;
import org.game.network.model.BaseModel;
import org.game.network.model.EntityIdLabelDto;

import java.util.List;

public class EntityModelDecoder extends ByteToMessageDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EntityModelDecoder() {
        objectMapper.registerSubtypes(
                new NamedType(EntityIdLabelDto.class, "EntityIdLabelDto"),
                new NamedType(ItemSpawnMessage.class, "ItemSpawnMessage")
                );
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return; // Wait until the length prefix is available.
        }

        in.markReaderIndex(); // Mark the current buffer position.
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] jsonBytes = new byte[length];
        in.readBytes(jsonBytes);
        BaseModel baseModel = objectMapper.readValue(jsonBytes, BaseModel.class);
        System.out.println("MODEL RECEIVED: " + baseModel.getClass().getName());
        out.add(baseModel);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}

