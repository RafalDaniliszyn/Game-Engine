package org.barneys.processData.gameStateProcess.move;

import game.isometric.entity.EntityProperties;
import org.barneys.blockLoader.action.Action;
import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.WorldState;
import org.barneys.model.GameStateModel;
import java.util.List;

public class EnterActionPipe implements Pipe<GameStateModel, GameStateModel> {
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
        int x = playerEntityModel.getPositionX();
        int y = playerEntityModel.getPositionY();
        int floor = playerEntityModel.getFloor();
        List<EntityProperties> entityProperties = WorldState.getWorldMap().getEntityPropertiesList(floor, x, y);
        for (EntityProperties property : entityProperties) {
            for (Action action : property.getActionList()) {
                if (Action.Invoke.ON_ENTER.equals(action.getInvoke())) {
                    action.processAction(playerEntityModel);
                }
            }
        }
        return gameStateModel;
    }
}
