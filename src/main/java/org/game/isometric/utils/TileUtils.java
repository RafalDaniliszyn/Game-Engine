package org.game.isometric.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class TileUtils {

    private TileUtils() {}

    /**
     * This method removes the specified entity from the tile by its ID.
     *
     * @param id Entity id to remove
     * @param entitiesQueue Deque with entity IDs on tiles.
     */
    public static void removeEntityFromTile(Long id, Deque<Long> entitiesQueue) {
        entitiesQueue.remove(id);
    }

    /**
     * This method adds an entity at the top.
     *
     * @param entityId Entity id to add
     * @param entitiesQueue Deque with entity IDs on tiles.
     */
    public static void addEntity(Long entityId, Deque<Long> entitiesQueue) {
        entitiesQueue.offerLast(entityId);
    }

    /**
     * This method adds an entity at the bottom without removing existing ones.
     *
     * @param entityId Entity id to add
     * @param entitiesQueue Deque with entity IDs on tiles.
     */
    public static void addEntityOnBottom(Long entityId, Deque<Long> entitiesQueue) {
        entitiesQueue.offerFirst(entityId);
    }

    /**
     * This method replaces the entity at the bottom and returns the ID of the removed entity.
     *
     * @param id new entity id
     * @param entitiesQueue Deque with entity IDs on tiles.
     * @return removed entity id
     */
    public static Long replaceEntityOnBottom(Long id, Deque<Long> entitiesQueue) {
        Long removed = entitiesQueue.pollFirst();
        entitiesQueue.offerFirst(id);
        return removed;
    }

    /**
     * This method retrieves a list of entities id from a specified tile.
     *
     * @param entitiesQueue The 2D array of Deques with entity IDs representing entities on tiles.
     * @param tileX         The X-coordinate of the tile.
     * @param tileY         The Y-coordinate of the tile.
     * @return A Deque<Long> containing the entities id on the specified tile or empty ArrayDeque.
     */
    public static Deque<Long> getEntitiesOnTile(Deque<Long>[][] entitiesQueue, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        if (tilePosition.x() >= 0 && tilePosition.x() <= entitiesQueue.length && tilePosition.y() >= 0 && tilePosition.y() <= entitiesQueue.length) {
            return entitiesQueue[tilePosition.x()][tilePosition.y()];
        }
        return new ArrayDeque<>();
    }

    /**
     * This method retrieves a list of IDs of adjacent tiles based on the given tileX, tileY and array of entities queue.
     *
     * @param entitiesQueue  The 2D array of Deques with entity IDs representing entities on tiles.
     * @param tileX          The X-coordinate of the tile.
     * @param tileY          The Y-coordinate of the tile.
     * @return A List<Long> containing the entities id on the adjacent tile or empty List.
     */
    public static List<Long> getAdjacentTiles(Deque<Long>[][] entitiesQueue, int tileX, int tileY) {
        TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
        List<Long> result = new ArrayList<>();
        if (tilePosition.x() > 0) {
            Deque<Long> left = entitiesQueue[tilePosition.x() - 1][tilePosition.y()];
            if (left != null) {
                result.add(left.peekFirst());
            }
        }
        if (tilePosition.x() < entitiesQueue.length - 1) {
            Deque<Long> right = entitiesQueue[tilePosition.x() + 1][tilePosition.y()];
            if (right != null) {
                result.add(right.peekFirst());
            }
        }
        if (tilePosition.y() < entitiesQueue.length - 1) {
            Deque<Long> up = entitiesQueue[tilePosition.x()][tilePosition.y() + 1];
            if (up != null) {
                result.add(up.peekFirst());
            }
        }
        if (tilePosition.y() > 0) {
            Deque<Long> down = entitiesQueue[tilePosition.x()][tilePosition.y() - 1];
            if (down != null) {
                result.add(down.peekFirst());
            }
        }

        if (tilePosition.x() > 0 && tilePosition.y() > 0) {
            Deque<Long> downLeft = entitiesQueue[tilePosition.x() - 1][tilePosition.y() - 1];
            if (downLeft != null) {
                result.add(downLeft.peekFirst());
            }
        }
        if (tilePosition.x() < entitiesQueue.length - 1 && tilePosition.y() > 0) {
            Deque<Long> downRight = entitiesQueue[tilePosition.x() + 1][tilePosition.y() - 1];
            if (downRight != null) {
                result.add(downRight.peekFirst());
            }
        }
        if (tilePosition.x() > 0 && tilePosition.y() < entitiesQueue.length - 1) {
            Deque<Long> upLeft = entitiesQueue[tilePosition.x() - 1][tilePosition.y() + 1];
            if (upLeft != null) {
                result.add(upLeft.peekFirst());
            }
        }
        if (tilePosition.x() < entitiesQueue.length - 1 && tilePosition.y() < entitiesQueue.length - 1) {
            Deque<Long> upRight = entitiesQueue[tilePosition.x() + 1][tilePosition.y() + 1];
            if (upRight != null) {
                result.add(upRight.peekFirst());
            }
        }
        return result;
    }
}
