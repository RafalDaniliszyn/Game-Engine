package org.barneys.processData.gameStateProcess.move;

import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.model.GameStateModel;

public class CollisionPipe implements Pipe<GameStateModel, GameStateModel> {

    private final CollisionChecker collisionChecker;

    public CollisionPipe() {
        this.collisionChecker = new CollisionChecker();
    }

    @Override
    public GameStateModel process(GameStateModel gameStateModel) {
        if (gameStateModel == null || checkCollision(gameStateModel)) {
            return null;
        }
        return gameStateModel;
    }



    public boolean checkCollision(GameStateModel gameStateModel) {
        int x = gameStateModel.getTileX();
        int y = gameStateModel.getTileY();
        int floor = gameStateModel.getFloor();
        return collisionChecker.checkCollision(x, y, floor);
    }
}
