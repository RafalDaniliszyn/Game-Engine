package org.game.network.client.incomingDataHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.isometric.GameState;
import org.game.isometric.utils.MapEditorUtils;
import org.game.network.client.model.PutModel;

/**
 * Client handler for incoming PutItem model.
 */
public class PutItemHandler extends SimpleChannelInboundHandler<PutModel> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PutModel model) throws Exception {
        if (!GameState.getUserUuid().equals(model.getUserUuid())) {
            HandlerAction.add(() -> MapEditorUtils.setEntityOnTile(model.getFloor(), model.getTileX(), model.getTileY(), model.getLabel(), false));
        }
    }

}
