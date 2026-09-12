package org.game.network.client.model;

import org.game.network.model.BaseModel;

public class ClientMessage extends BaseModel {
    private long playerId;


    public ClientMessage() {
    }

    public ClientMessage(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
