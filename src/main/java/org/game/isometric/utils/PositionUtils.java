package org.game.isometric.utils;

import org.game.isometric.WorldSettings;

public final class PositionUtils {

    private PositionUtils() {}

    /**
     * This method is responsible for computing chunkX and chunkY based on the given tileX and tileY,
     * as well as adjusting tileX and tileY to fit the chunk size.
     * If either tileX or tileY exceeds the chunk size, chunkX or chunkY is incremented,
     * and tileX or tileY is adjusted by subtracting the number of tiles in the chunk multiplied by the number of additional chunks.
     *
     * @param tileX the X coordinate of the tile
     * @param tileY the Y coordinate of the tile
     * @return TilePosition object with computed values.
     */
    public static TilePosition getTilePosition(int tileX, int tileY) {
        int chunkX = tileX / WorldSettings.CHUNK_SIZE;
        int chunkY = tileY / WorldSettings.CHUNK_SIZE;
        tileX = tileX % WorldSettings.CHUNK_SIZE;
        tileY = tileY % WorldSettings.CHUNK_SIZE;
        return new TilePosition(tileX, tileY, chunkX, chunkY);
    }

}

