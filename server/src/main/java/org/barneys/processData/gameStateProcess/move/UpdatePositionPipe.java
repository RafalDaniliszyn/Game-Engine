package org.barneys.processData.gameStateProcess.move;

import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.WorldState;
import org.barneys.model.GameStateModel;

public class UpdatePositionPipe implements Pipe<GameStateModel, GameStateModel> {

    @Override
    public GameStateModel process(GameStateModel gameStateModel) {
        if (gameStateModel == null) {
            return null;
        }
        User user = WorldState.getByUuid(gameStateModel.getUserUuid());
        if (user == null) {
            return null;
        }
        PlayerEntityModel playerEntityModel = user.getPlayerEntityModel();
        int distX = Math.abs(playerEntityModel.getPositionX() - gameStateModel.getTileX());
        int distY = Math.abs(playerEntityModel.getPositionY() - gameStateModel.getTileY());
        if (distX >= 0 && distX < 2 && (distY >= 0 && distY < 2) ) {
            playerEntityModel.setPositionX(gameStateModel.getTileX());
            playerEntityModel.setPositionY(gameStateModel.getTileY());
        } else {
           gameStateModel.setTileX(playerEntityModel.getPositionX());
           gameStateModel.setTileY(playerEntityModel.getPositionY());
        }
        return gameStateModel;
    }

}
