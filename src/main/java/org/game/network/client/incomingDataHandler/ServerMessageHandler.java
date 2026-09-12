package org.game.network.client.incomingDataHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.isometric.utils.MapEditorUtils;

public class ServerMessageHandler extends SimpleChannelInboundHandler<ServerMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerMessage serverMessage) throws Exception {
        switch (serverMessage) {
            case ItemSpawnMessage message -> HandlerAction.add(() ->
                    MapEditorUtils.itemSpawnMessageHandler(message));
            default -> throw new IllegalStateException("Unexpected value: " + serverMessage);
        }
        System.out.println(serverMessage);
    }
}
