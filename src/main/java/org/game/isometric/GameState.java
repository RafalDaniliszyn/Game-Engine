package org.game.isometric;

public class GameState {
    private static int currentChunkX;
    private static int currentChunkY;
    private static int currentFloor;

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
}
