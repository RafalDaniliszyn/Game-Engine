package org.barneys.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.BlockingQueue;

public class NettyEventHandler extends SimpleChannelInboundHandler<ClientMessage> {

    private final BlockingQueue<GameEvent> eventQueue;

    public NettyEventHandler(BlockingQueue<GameEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        eventQueue.offer(GameEventMapper.map(msg));
    }
}
