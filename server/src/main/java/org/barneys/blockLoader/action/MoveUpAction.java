package org.barneys.blockLoader.action;

import game.isometric.WorldSettings;
import org.barneys.PlayerEntityModel;

public class MoveUpAction extends Action {
    public MoveUpAction(boolean removeEntityAfter, Invoke invoke) {
        super(removeEntityAfter, true, invoke);
    }

    @Override
    public ActionEnum getActionType() {
        return ActionEnum.MoveUpAction;
    }

    @Override
    public void processAction(PlayerEntityModel playerEntityModel) {
        int currentFloor = playerEntityModel.getFloor();
        int floors = WorldSettings.FLOORS;
        if (currentFloor < floors - 1) {
            playerEntityModel.setFloor(currentFloor + 1);
            System.out.println("MoveUpAction setFloor { " + playerEntityModel + " }");
        }
    }

}
