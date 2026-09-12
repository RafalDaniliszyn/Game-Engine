package org.game.network.processData;

public class ChunkLoaded {
    private final int x;
    private final int y;
    private final int floor;

    public ChunkLoaded(int x, int y, int floor) {
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFloor() {
        return floor;
    }
}
