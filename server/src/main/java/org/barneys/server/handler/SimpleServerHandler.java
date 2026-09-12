package org.barneys.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.barneys.model.BaseModel;
import org.barneys.processData.ChannelActiveHelper;
import org.barneys.processData.ProcessDataStrategy;
import org.barneys.server.modelHandler.VersionUpdateFinishedEvent;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SimpleServerHandler extends SimpleChannelInboundHandler<BaseModel> {

    private static ChannelGroup allChannels;

    public SimpleServerHandler(ChannelGroup allChannels) {
        SimpleServerHandler.allChannels = allChannels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        if (socketAddress != null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            System.out.println(inetSocketAddress);
        }
        System.out.println("channel id: " + channel.id());
        allChannels.add(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseModel msg) {
        ProcessDataStrategy.getStrategy(msg).onChannelReadProcess(ctx.channel(), allChannels, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof VersionUpdateFinishedEvent) {
            ChannelActiveHelper.sendOnActiveData(ctx.channel());
        }
    }

    public static void send(BaseModel baseModel) {
        allChannels.writeAndFlush(baseModel);
    }
}
