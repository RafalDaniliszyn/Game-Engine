package org.barneys.game;

import org.barneys.worldMap.WorldMap;

import java.util.UUID;

public class ToolUseEvent implements GameEvent {
    private final long playerId;
    private final UUID clientUuid;
    private final int toolId;

    public ToolUseEvent(long playerId, UUID clientUuid, int toolId) {
        this.playerId = playerId;
        this.clientUuid = clientUuid;
        this.toolId = toolId;
    }

    @Override
    public void execute(WorldMap worldMap) {
        System.out.println("----------------ToolUseEvent--------------");
        System.out.println("playerId:" + playerId);
        System.out.println("clientUuid:" + clientUuid);
        System.out.println("toolId:" + toolId);
        System.out.println("----------------ToolUseEvent--------------");
    }

    public long getPlayerId() {
        return playerId;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public int getToolId() {
        return toolId;
    }
}
