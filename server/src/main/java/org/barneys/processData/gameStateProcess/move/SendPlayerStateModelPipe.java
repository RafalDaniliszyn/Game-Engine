package org.barneys.processData.gameStateProcess.move;

import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.WorldState;
import org.barneys.model.Direction;
import org.barneys.model.GameStateModel;
import org.barneys.model.PlayerStateModel;
import org.barneys.server.handler.SimpleServerHandler;

public class SendPlayerStateModelPipe implements Pipe<GameStateModel, GameStateModel> {

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
        PlayerStateModel playerStateModel = new PlayerStateModel(
                user.getUuid(),
                user.getName(),
                playerEntityModel.getPositionX(),
                playerEntityModel.getPositionY(),
                playerEntityModel.getFloor(),
                playerEntityModel.getTextureLabel(), gameStateModel.getDirection());
        System.out.println("SendPlayerStateModelPipe { " + playerStateModel + " }");
        SimpleServerHandler.send(playerStateModel);
        return gameStateModel;
    }

//    private void badMoveRequest() {
//        PlayerEntityModel playerEntityModel = user.getPlayerEntityModel();
//        PlayerStateModel playerStateModel = new PlayerStateModel(
//                user.getUuid(),
//                user.getName(),
//                playerEntityModel.getPositionX(),
//                playerEntityModel.getPositionY(),
//                playerEntityModel.getFloor(),
//                playerEntityModel.getTextureLabel(), gameStateModel.getDirection());
//    }

    private Direction getDirection(Direction direction) {
        Direction result = null;
        switch (direction) {
            case LEFT -> result = Direction.LEFT;
            case RIGHT -> result = Direction.RIGHT;
            case UP -> result = Direction.UP;
            case DOWN -> result = Direction.DOWN;
        }
        return result;
    }
}
