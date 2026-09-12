package org.game.network.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.game.network.client.model.PickupItemMessage;
import org.game.network.client.model.ToolUseRequestMessage;
import org.game.network.model.BaseModel;
import org.game.network.model.EntityIdLabelDto;

public class EntityModelEncoder extends MessageToByteEncoder<BaseModel> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EntityModelEncoder() {
        objectMapper.registerSubtypes(
                new NamedType(PickupItemMessage.class, "PickupItemMessage"),
                new NamedType(ToolUseRequestMessage.class, "ToolUseRequestMessage")
        );
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BaseModel model, ByteBuf out) throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(model);
        out.writeInt(jsonBytes.length);
        out.writeBytes(jsonBytes);
    }
}
