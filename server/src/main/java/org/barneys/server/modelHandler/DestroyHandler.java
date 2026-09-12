package org.barneys.server.modelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.barneys.server.handler.SimpleServerHandler;
import org.barneys.worldMap.WorldMapUtils;

/**
 * The DestroyHandler class manages block destruction by the player through direct contact.
 */
public class DestroyHandler extends SimpleChannelInboundHandler<DestroyModel> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DestroyModel model) throws Exception {
        System.out.println("Server received: " + model);
        // TODO: 10/16/2024 Need validation
        WorldMapUtils.removeTile(model.getTileX(), model.getTileY(), model.getFloor());
        SimpleServerHandler.send(new DestroyModel(model.getTileX(), model.getTileY(), model.getFloor(), "HAND")); //brakuje uuid gracza
    }
}
