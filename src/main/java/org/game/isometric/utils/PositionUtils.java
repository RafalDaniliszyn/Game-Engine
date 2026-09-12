package org.game.isometric.utils;

import org.game.isometric.WorldSettings;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.MoveComponent2D;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Objects;

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
        int x = Math.round(position.x / tileSize);
        int y = Math.round(position.y / tileSize);
        return new AbsoluteTilePosition(x, y);
    }

    public static AbsoluteTilePosition getAbsoluteTilePositionFromWorldSpace(Vector2f position, MoveComponent2D.Direction direction) {
        float tileSize = WorldSettings.TILE_SIZE;
        int x = Math.round(position.x / tileSize);;
        int y = Math.round(position.y / tileSize);;
        switch (direction) {
            case LEFT -> {
                x = (int) Math.floor(position.x / tileSize);
            }
            case RIGHT -> {
                x = (int) Math.ceil(position.x / tileSize);
            }
            case UP -> {
                y = (int) Math.ceil(position.y / tileSize);
            }
            case DOWN -> {
                y = (int) Math.floor(position.y / tileSize);
            }
        }
        return new AbsoluteTilePosition(x, y);
    }

    public static Vector2f getScreenPosition(AbsoluteTilePosition tilePosition) {
        float tileSize = WorldSettings.TILE_SIZE;
        return new Vector2f(tilePosition.x * tileSize, tilePosition.y * tileSize);
    }

    public static Vector2i getDistance(AbsoluteTilePosition p1, AbsoluteTilePosition p2) {
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

    public static void setDestinationTile(MoveComponent2D moveComponent, AbsoluteTilePosition absoluteTilePosition, MoveComponent2D.Direction direction) {
        if (direction == null) {
            AbsoluteTilePosition destinationTile =
                    new AbsoluteTilePosition(absoluteTilePosition.x(), absoluteTilePosition.y());
            Vector2f screenPosition = PositionUtils.getScreenPosition(destinationTile);
            moveComponent.setDestination(screenPosition);
            return;
        }
        switch (direction) {
            case LEFT -> {
                AbsoluteTilePosition destinationTile =
                        new AbsoluteTilePosition(absoluteTilePosition.x() - 1, absoluteTilePosition.y());
                Vector2f screenPosition = PositionUtils.getScreenPosition(destinationTile);
                moveComponent.setDestination(screenPosition);
            }
            case RIGHT -> {
                AbsoluteTilePosition destinationTile =
                        new AbsoluteTilePosition(absoluteTilePosition.x() + 1, absoluteTilePosition.y());
                Vector2f screenPosition = PositionUtils.getScreenPosition(destinationTile);
                moveComponent.setDestination(screenPosition);
            }
            case UP -> {
                AbsoluteTilePosition destinationTile =
                        new AbsoluteTilePosition(absoluteTilePosition.x(), absoluteTilePosition.y() + 1);
                Vector2f screenPosition = PositionUtils.getScreenPosition(destinationTile);
                moveComponent.setDestination(screenPosition);
            }
            case DOWN -> {
                AbsoluteTilePosition destinationTile =
                        new AbsoluteTilePosition(absoluteTilePosition.x(), absoluteTilePosition.y() - 1);
                Vector2f screenPosition = PositionUtils.getScreenPosition(destinationTile);
                moveComponent.setDestination(screenPosition);
            }
        }
    }

    public record AbsoluteTilePosition(int x, int y) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbsoluteTilePosition that = (AbsoluteTilePosition) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this);
        }
    }

}

