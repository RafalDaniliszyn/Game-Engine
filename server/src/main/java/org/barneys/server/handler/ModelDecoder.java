package org.barneys.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.barneys.game.PickupItemMessage;
import org.barneys.game.ToolUseRequestMessage;
import org.barneys.model.BaseModel;

import java.util.List;

public class ModelDecoder extends ByteToMessageDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ModelDecoder() {
        objectMapper.registerSubtypes(
                new NamedType(PickupItemMessage.class, "PickupItemMessage"),
                new NamedType(ToolUseRequestMessage.class, "ToolUseRequestMessage")
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
        System.out.println("decoder: " + baseModel.getClass().getName());
        out.add(baseModel);
    }
}

