package org.game.network.client.model;

import org.game.network.model.BaseModel;

public class PutModel extends BaseModel {
    private int tileX;
    private int tileY;
    private int floor;
    private String label;

    public PutModel() {
    }

    public PutModel(int tileX, int tileY, int floor, String label) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.floor = floor;
        this.label = label;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
