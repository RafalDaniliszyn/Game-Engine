package org.barneys.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.barneys.WorldState;
import org.barneys.game.GameLoop;
import org.barneys.game.NettyEventHandler;
import org.barneys.game.ServerContext;
import org.barneys.server.modelHandler.VersionUpdateHandler;
import org.barneys.server.modelHandler.DestroyHandler;
import org.barneys.server.modelHandler.PutModelHandler;
import org.barneys.server.handler.ModelDecoder;
import org.barneys.server.handler.ModelEncoder;
import org.barneys.server.handler.SimpleServerHandler;
import org.barneys.server.modelHandler.fileTransfer.FileTransferRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
    private final int port;
    private static final ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NettyServer(int port) {
        this.port = port;
    }

    public void start(ServerContext serverContext) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ModelDecoder());
                            pipeline.addLast(new ModelEncoder());
                            pipeline.addLast(new PutModelHandler());
                            pipeline.addLast(new DestroyHandler());
                            pipeline.addLast(new FileTransferRequestHandler());
                            pipeline.addLast(new VersionUpdateHandler());
                            pipeline.addLast(new NettyEventHandler(serverContext.getGameLoop().getEventQueue()));
                            pipeline.addLast(new SimpleServerHandler(allChannels));
                            socketChannel.closeFuture().addListener(new DisconnectEventProcessor());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("Server started, listening on " + port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static String JDBC_HOST;
    public static String JDBC_PORT;
    public static String HIBERNATE_USERNAME;
    public static String HIBERNATE_PASSWORD;
    public static String INIT_MAP;

    public static void main(String[] args) throws InterruptedException {
        ConfigManager configManager = new ConfigManager();
        JDBC_HOST = ConfigManager.config.JDBC_HOST;
        JDBC_PORT = String.valueOf(ConfigManager.config.JDBC_PORT);
        HIBERNATE_USERNAME = ConfigManager.config.HIBERNATE_USERNAME;
        HIBERNATE_PASSWORD = ConfigManager.config.HIBERNATE_PASSWORD;
        for (String arg : args) {
            String[] split = arg.split("=");
            switch (split[0]) {
                case "JDBC_HOST" -> JDBC_HOST = split[1];
                case "JDBC_PORT" -> JDBC_PORT = split[1];
                case "HIBERNATE_USERNAME" -> HIBERNATE_USERNAME = split[1];
                case "HIBERNATE_PASSWORD" -> HIBERNATE_PASSWORD = split[1];
                case "INIT_MAP" -> {
                    INIT_MAP = split[1];
                   // log.info("run profile: INIT_MAP {}", WorldState.getWorldMap().hashCode());
                }
            }
        }
        ServerContext serverContext = new ServerContext(WorldState.getWorldMap());
        if ("true".equals(NettyServer.INIT_MAP)) {
            log.info("run profile: INIT_MAP {}", WorldState.getWorldMap().hashCode());
        }
        Thread thread = new Thread(serverContext.getGameLoop());
        thread.start();
        new NettyServer(8080).start(serverContext);
    }

}

