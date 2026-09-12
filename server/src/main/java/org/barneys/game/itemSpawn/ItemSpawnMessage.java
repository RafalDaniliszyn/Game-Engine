package org.barneys.game.itemSpawn;

import org.barneys.game.ServerMessage;

public class ItemSpawnMessage extends ServerMessage {
    private String prefab;
    private long id;
    private int tileX;
    private int tileY;
    private int floor;

    public ItemSpawnMessage() {
    }

    public ItemSpawnMessage(String prefab, long id, int tileX, int tileY, int floor) {
        this.prefab = prefab;
        this.id = id;
        this.tileX = tileX;
        this.tileY = tileY;
        this.floor = floor;
    }

    public String getPrefab() {
        return prefab;
    }

    public void setPrefab(String prefab) {
        this.prefab = prefab;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ItemSpawnMessage{" +
                "prefab='" + prefab + '\'' +
                ", id=" + id +
                ", tileX=" + tileX +
                ", tileY=" + tileY +
                ", floor=" + floor +
                "} " + super.toString();
    }
}
