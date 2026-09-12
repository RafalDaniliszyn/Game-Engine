package org.game.network.model;

import org.game.network.DataType;

import java.util.Arrays;
import java.util.Deque;

public class WorldMapModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.WORLD_MAP_MODEL;
    private int floor;
    private int chunkX;
    private int chunkY;
    private Deque<EntityIdLabelDto>[][] entitiesQueue;
    private boolean isComplete;

    public WorldMapModel() {
        super(DATA_TYPE.getType());
    }

    public WorldMapModel(int floor, int chunkX, int chunkY, Deque<EntityIdLabelDto>[][] entitiesQueue, boolean isComplete) {
        super(DATA_TYPE.getType());
        this.floor = floor;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.entitiesQueue = entitiesQueue;
        this.isComplete = isComplete;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public void setChunkY(int chunkY) {
        this.chunkY = chunkY;
    }

    public Deque<EntityIdLabelDto>[][] getEntitiesQueue() {
        return entitiesQueue;
    }

    public void setEntitiesQueue(Deque<EntityIdLabelDto>[][] entitiesQueue) {
        this.entitiesQueue = entitiesQueue;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "WorldMapModel{" +
                "floor=" + floor +
                ", chunkX=" + chunkX +
                ", chunkY=" + chunkY +
                ", entitiesQueue=" + Arrays.toString(entitiesQueue) +
                "} " + super.toString();
    }
}
