package org.game.isometric.utils;

import java.util.Objects;

public record TilePosition(int x, int y, int chunkX, int chunkY) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TilePosition that = (TilePosition) o;
        return x == that.x && y == that.y && chunkX == that.chunkX && chunkY == that.chunkY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, chunkX, chunkY);
    }
}
