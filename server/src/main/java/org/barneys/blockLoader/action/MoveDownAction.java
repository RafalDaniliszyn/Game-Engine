package org.barneys.blockLoader.action;

import org.barneys.PlayerEntityModel;

public class MoveDownAction extends Action {
    public MoveDownAction(boolean removeEntityAfter, Invoke invoke) {
        super(removeEntityAfter, true, invoke);
    }

    @Override
    public ActionEnum getActionType() {
        return ActionEnum.MoveDownAction;
    }

    @Override
    public void processAction(PlayerEntityModel playerEntityModel) {
        int currentFloor = playerEntityModel.getFloor();
        if (currentFloor > 0) {
            playerEntityModel.setFloor(currentFloor - 1);
            System.out.println("MoveDownAction setFloor { " + playerEntityModel + " }");
        }
    }

}
