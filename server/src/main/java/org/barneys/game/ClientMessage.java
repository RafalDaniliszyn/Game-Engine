package org.barneys.game;

import org.barneys.model.BaseModel;

public class ClientMessage extends BaseModel {
    private MessageType type;
    private long playerId;

    public ClientMessage() {
    }

    public MessageType getType() {
        return type;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
