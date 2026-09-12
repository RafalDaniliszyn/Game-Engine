package org.game.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.game.GraphicsDisplay;
import org.game.isometric.GameState;
import org.game.network.DataType;
import org.game.network.client.handler.EntityModelDecoder;
import org.game.network.client.handler.EntityModelEncoder;
import org.game.network.client.handler.GameClientHandler;
import org.game.network.client.incomingDataHandler.*;
import org.game.network.client.incomingDataHandler.fileTransfer.FileTransferHandler;
import org.game.network.client.incomingDataHandler.update.JsonFileHandler;
import org.game.network.client.incomingDataHandler.update.VersionInfoHandler;
import org.game.network.model.BaseModel;
import java.net.InetSocketAddress;

public class GameClient {
    private static volatile GameClient instance;
    private final String host;
    private final int port;
    private Channel channel;

    private GameClient(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        start();
    }

    public static GameClient getInstance() {
        if (instance == null) {
            synchronized (GameClient.class) {
                if (instance == null) {
                    try {
                        String host = "localhost";
                        int port = 8080;
                        if (GraphicsDisplay.port != 0 && GraphicsDisplay.host != null) {
                            port = GraphicsDisplay.port;
                            host = GraphicsDisplay.host;
                        }
                        System.out.println(host + ": " + port);
                        instance = new GameClient(host, port);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

    public Channel getChannel() {
        return channel;
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new EntityModelEncoder());
                            pipeline.addLast(new EntityModelDecoder());
                            pipeline.addLast(new VersionInfoHandler());
                            pipeline.addLast(new PlayerInitDataHandler());
                            pipeline.addLast(new PutItemHandler());
                            pipeline.addLast(new ServerMessageHandler());
                            pipeline.addLast(new DestroyHandler());
                            pipeline.addLast(new DropModelHandler());
                            pipeline.addLast(new FileTransferHandler());
                            pipeline.addLast(new JsonFileHandler());
                            pipeline.addLast(new GameClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
            channel = channelFuture.channel();
            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                eventLoopGroup.shutdownGracefully();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(BaseModel model) {
        System.out.println("send: " + model);
        System.out.println("send: " + model.getClass());
        channel.writeAndFlush(model);
    }

    public void shutdown() {
        channel.writeAndFlush(new ShutdownModel());
        GameState.setOffline();
        channel.close();
    }

    public static class ShutdownModel extends BaseModel {
        private static final DataType DATA_TYPE = DataType.SHUTDOWN_MODEL;

        public ShutdownModel() {
            super(DATA_TYPE.getType());
        }

        @Override
        public String toString() {
            return DATA_TYPE.getType();
        }
    }
}

