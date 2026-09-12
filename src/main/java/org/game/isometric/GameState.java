package org.game.isometric;

import org.game.isometric.component.MoveComponent2D.Direction;
import org.game.isometric.utils.TilePosition;
import org.game.network.model.PlayerStateModel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameState {
    public static UUID userUuid;
    private static long playerId;
    private static int currentChunkX;
    private static int currentChunkY;
    private static int currentFloor;
    private static TilePosition playerPosition;
    private static Direction playerDirection;
    private static final Map<String, PlayerStateModel> playersStateMap;
    private static final Map<UUID, Long> entityIdByUserUuid;
    public static Mode mode;

    static {
        playersStateMap = new HashMap<>();
        entityIdByUserUuid = new HashMap<>();
        playerPosition = new TilePosition(38, 3, 0, 0);
    }

    public static void addToEntityIdByUserUuidMap(UUID userUuid, long entityId) {
        entityIdByUserUuid.put(userUuid, entityId);
    }

    public static Long getPlayerEntityId(UUID userUuid) {
        return entityIdByUserUuid.get(userUuid);
    }

    public static UUID getUserUuid() {
        return userUuid;
    }

    public static void setUserUuid(UUID userUuid) {
        GameState.userUuid = userUuid;
    }

    public static long getPlayerId() {
        return playerId;
    }

    public static void setPlayerId(long playerId) {
        GameState.playerId = playerId;
    }

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
        if (playerPosition == null) {

        }
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

    public static Map<String, PlayerStateModel> getPlayersStateMap() {
        return playersStateMap;
    }

    public static void addPlayerState(PlayerStateModel playerState) {
        GameState.playersStateMap.put(playerState.getPlayerName(), playerState);
    }

    public static void setOnline() {
        mode = Mode.ONLINE;
    }

    public static void setOffline() {
        mode = Mode.OFFLINE;
    }
}
