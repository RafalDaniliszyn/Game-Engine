package org.barneys.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.barneys.game.itemSpawn.ItemSpawnMessage;
import org.barneys.model.BaseModel;
import org.barneys.model.EntityIdLabelDto;

public class ModelEncoder extends MessageToByteEncoder<BaseModel> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ModelEncoder() {
        objectMapper.registerSubtypes(
                new NamedType(EntityIdLabelDto.class, "EntityIdLabelDto"),
                new NamedType(ItemSpawnMessage.class, "ItemSpawnMessage")
                );
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BaseModel model, ByteBuf out) throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(model);
        out.writeInt(jsonBytes.length);
        out.writeBytes(jsonBytes);
    }


}
