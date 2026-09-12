package org.game.isometric.action;

import org.game.entity.Entity;
import org.game.isometric.GameState;
import org.game.isometric.component.PositionComponent2D;

public class MoveDownAction extends Action {
    public MoveDownAction(boolean removeEntityAfter, Invoke invoke) {
        super(removeEntityAfter, true, invoke);
    }

    @Override
    public ActionEnum getActionType() {
        return ActionEnum.MoveDownAction;
    }

    @Override
    public void processAction(Entity entity) {
        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        int currentFloor = positionComponent.getFloor();
        if (currentFloor > 0) {
            positionComponent.setFloor(currentFloor - 1);
            GameState.setCurrentFloor(currentFloor - 1);
        }
    }

}
