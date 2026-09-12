package org.barneys.game;

public class GameEventMapper {

    public static GameEvent map(ClientMessage msg) {

        return switch (msg) {
            case PickupItemMessage m -> new PickupItemEvent(m.getX(), m.getY());
            case ToolUseRequestMessage m -> new ToolUseEvent(m.getPlayerId(), m.getUserUuid(), m.getToolId());
            default -> throw new IllegalStateException("Unexpected value: " + msg);
        };
    }
}