package org.game.network.client;

import org.game.entity.Entity;
import org.game.isometric.GameState;
import org.game.isometric.Input;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.network.client.model.ClientMessage;
import org.game.network.client.model.DownloadUpdateModel;
import org.game.network.client.model.DestroyModel;
import org.game.network.client.model.PutModel;
import org.game.network.model.GameStateModel;
import org.joml.Vector2f;

import static org.game.isometric.utils.PositionUtils.AbsoluteTilePosition;
import static org.game.isometric.utils.PositionUtils.getAbsoluteTilePositionFromWorldSpace;

/**
 * This class provides methods for communication with the server.
 */
public class DataSync {
    private static final GameClient gameClient = GameClient.getInstance();

    /**
     * Block destruction by the player through direct contact.
     */
    public static void sendDestroy(Vector2f position) {
        AbsoluteTilePosition absTilePosition = getAbsoluteTilePositionFromWorldSpace(position);
        DestroyModel destroyModel = new DestroyModel(
                absTilePosition.x(),
                absTilePosition.y(),
                GameState.getCurrentFloor(),
                "HAND"
        );
        gameClient.send(destroyModel);
    }

    public static void sendPutModel(int x, int y, String label) {
        PutModel putModel = new PutModel(x, y, GameState.getCurrentFloor(), label);
        gameClient.send(putModel);
    }

    public static GameStateModel sendLocalGameState(GameStateModel lastGameStateModel) {
        TilePosition playerPosition = GameState.getPlayerPosition();
        AbsoluteTilePosition absoluteTilePosition = PositionUtils.getAbsoluteTilePosition(playerPosition);
        int floor = GameState.getCurrentFloor();
        GameStateModel currentGameStateModel =
                new GameStateModel(absoluteTilePosition.x(), absoluteTilePosition.y(), floor, GameState.getPlayerDirection(), Entity.State.ACTIVE, "", Input.MOVE_FORWARD);
        if (!currentGameStateModel.equals(lastGameStateModel)) {
            System.out.println("sendLocalPlayerPosition: " + currentGameStateModel);
            gameClient.send(currentGameStateModel);
        }
        return currentGameStateModel;
    }

    public static void sendVersionUpdateRequest(DownloadUpdateModel model) {
        gameClient.send(model);
    }

    public static void sendClientMessage(ClientMessage model) {
        gameClient.send(model);
    }
}
