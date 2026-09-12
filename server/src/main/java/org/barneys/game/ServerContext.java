package org.barneys.game;

import org.barneys.worldMap.WorldMap;

public class ServerContext {
    private final GameLoop gameLoop;
    private final ChannelRegistry channelRegistry;

    public ServerContext(WorldMap worldMap) {
        this.gameLoop = new GameLoop(worldMap);
        this.channelRegistry = new ChannelRegistry();
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public ChannelRegistry getChannelRegistry() {
        return channelRegistry;
    }
}
