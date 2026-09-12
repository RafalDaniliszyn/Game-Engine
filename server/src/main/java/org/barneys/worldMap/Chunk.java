package org.barneys.worldMap;

import java.util.ArrayDeque;
import java.util.Deque;

import static game.isometric.WorldSettings.CHUNK_SIZE;

public class Chunk {
    private final Deque<Long>[][] entitiesQueue;
    private final int x;
    private final int y;
    private final int floor;

    public Chunk(Integer x, Integer y, Integer floor) {
        this.x = x;
        this.y = y;
        this.floor = floor;

        this.entitiesQueue = new ArrayDeque[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                this.entitiesQueue[i][j] = new ArrayDeque<>();
            }
        }
    }

    public Deque<Long>[][] getEntitiesQueue() {
        return entitiesQueue;
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
