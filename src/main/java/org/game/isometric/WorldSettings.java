package org.game.isometric;

public class WorldSettings {
    public static final float TILE_SIZE = 100.0f;

    /**
     * Chunk must be square.
     * CHUNK_SIZE is number of tiles on one side.
     */
    public static final Integer CHUNK_SIZE;

    /**
     * World must be square.
     * WORLD_SIZE is number of chunks on one side.
     */
    public static final Integer WORLD_SIZE;
    public static final Integer FLOORS;

    static {
        CHUNK_SIZE = 40;
        WORLD_SIZE = 3;
        FLOORS = 2;
    }

    public static float getTileSize() {
        return TILE_SIZE;
    }
}
