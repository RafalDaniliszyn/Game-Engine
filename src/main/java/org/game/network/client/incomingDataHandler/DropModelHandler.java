package org.game.network.client.incomingDataHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.isometric.utils.MapEditorUtils;
import org.game.network.client.model.DropModel;

public class DropModelHandler extends SimpleChannelInboundHandler<DropModel> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DropModel model) throws Exception {
        HandlerAction.add(() -> MapEditorUtils.setEntityOnTile(model.getFloor(), model.getTileX(), model.getTileY(), model.getLabel(), false));
    }
}
