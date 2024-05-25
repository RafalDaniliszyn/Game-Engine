package org.game.isometric;

import org.game.isometric.component.MoveComponent2D.Direction;
import org.game.isometric.utils.TilePosition;

public class GameState {
    private static int currentChunkX;
    private static int currentChunkY;
    private static int currentFloor;
    private static TilePosition playerPosition;
    private static Direction playerDirection;


    public static int getCurrentChunkX() {
        return currentChunkX;
    }

    public static void setCurrentChunkX(int currentChunkX) {
        GameState.currentChunkX = currentChunkX;
    }

    public static int getCurrentChunkY() {
        return currentChunkY;
    }

    public static void setCurrentChunkY(int currentChunkY) {
        GameState.currentChunkY = currentChunkY;
    }

    public static int getCurrentFloor() {
        return currentFloor;
    }

    public static void setCurrentFloor(int currentFloor) {
        GameState.currentFloor = currentFloor;
    }


    public static TilePosition getPlayerPosition() {
        return playerPosition;
    }

    public static void setPlayerPosition(TilePosition playerPosition) {
        GameState.playerPosition = playerPosition;
    }

    public static Direction getPlayerDirection() {
        return playerDirection;
    }

    public static void setPlayerDirection(Direction playerDirection) {
        GameState.playerDirection = playerDirection;
    }
}
