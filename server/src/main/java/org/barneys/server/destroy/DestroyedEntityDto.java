package org.barneys.server.destroy;

import java.util.Map;

public class DestroyedEntityDto {
    private int x;
    private int y;
    private int floor;
    private Map<String, Integer> drop;

    public DestroyedEntityDto(int x, int y, int floor, Map<String, Integer> drop) {
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.drop = drop;
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

    public Map<String, Integer> getDrop() {
        return drop;
    }
}
