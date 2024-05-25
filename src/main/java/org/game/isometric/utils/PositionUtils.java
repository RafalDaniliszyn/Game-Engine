package org.game.isometric.utils;

import org.game.isometric.WorldSettings;
import org.game.isometric.blockLoader.Side;
import org.joml.Vector2f;
import org.joml.Vector2i;

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

    public static AbsoluteTilePosition getAbsoluteTilePosition(TilePosition tilePosition) {
        Integer chunkSize = WorldSettings.CHUNK_SIZE;
        int x = tilePosition.x() + (tilePosition.chunkX() * chunkSize);
        int y = tilePosition.y() + (tilePosition.chunkY() * chunkSize);
        return new AbsoluteTilePosition(x, y);
    }

    public static AbsoluteTilePosition getAbsoluteTilePositionFromWorldSpace(Vector2f position) {
        float tileSize = WorldSettings.TILE_SIZE;
        int x = (int) (position.x / tileSize);
        int y = (int) (position.y / tileSize);
        return new AbsoluteTilePosition(x, y);
    }


    public static Vector2i getDistance(TilePosition p1, TilePosition p2) {
        int x = Math.abs(p1.x() - p2.x());
        int y = Math.abs(p1.y() - p2.y());
        return new Vector2i(Math.abs(x), Math.abs(y));
    }

    public static Side getRelativePosition(Vector2f v1, Vector2f v2) {
        if (v1.x < v2.x && v1.y == v2.y) {
            return Side.LEFT;
        }
        if (v1.x > v2.x && v1.y == v2.y) {
            return Side.RIGHT;
        }
        if (v1.y > v2.y && v1.x == v2.x) {
            return Side.UP;
        }
        if (v1.y < v2.y && v1.x == v2.x) {
            return Side.DOWN;
        }
        return Side.CENTER;
    }

    public record AbsoluteTilePosition(int x, int y) {
    }

}

