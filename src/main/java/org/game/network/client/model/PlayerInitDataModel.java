package org.game.network.client.model;

import org.game.network.model.BaseModel;

public class PlayerInitDataModel extends BaseModel {

    private int x;
    private int y;
    private int floor;
    private long sessionEntityId;

    public PlayerInitDataModel(int x, int y, int floor, long sessionEntityId) {
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.sessionEntityId = sessionEntityId;
    }

    public PlayerInitDataModel() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public long getSessionEntityId() {
        return sessionEntityId;
    }

    public void setSessionEntityId(long sessionEntityId) {
        this.sessionEntityId = sessionEntityId;
    }

    @Override
    public String toString() {
        return "PlayerInitDataModel{" +
                "x=" + x +
                ", y=" + y +
                ", floor=" + floor +
                ", sessionEntityId=" + sessionEntityId +
                "} " + super.toString();
    }
}
