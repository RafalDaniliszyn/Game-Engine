package org.game.isometric;

public class WorldSettings {
    public static final float TILE_SIZE = 100.0f;
    public static final float TILE_OVERLAP_LENGTH;

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
        TILE_OVERLAP_LENGTH = 0.0f;
        CHUNK_SIZE = 40;
        WORLD_SIZE = 4;
        FLOORS = 2;
    }

    public static float getTileSize() {
        return TILE_SIZE;
    }
}
