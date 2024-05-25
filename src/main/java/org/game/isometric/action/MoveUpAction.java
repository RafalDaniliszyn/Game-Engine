package org.game.isometric.action;

import org.game.entity.Entity;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.PositionComponent2D;

public class MoveUpAction extends Action {
    public MoveUpAction(boolean removeEntityAfter, Invoke invoke) {
        super(removeEntityAfter, true, invoke);
    }

    @Override
    public void processAction(Entity entity) {
        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        int floors = WorldSettings.FLOORS;
        int currentFloor = positionComponent.getFloor();
        if (currentFloor < floors - 1) {
            positionComponent.setFloor(currentFloor + 1);
            GameState.setCurrentFloor(currentFloor + 1);
        }
    }

    @Override
    public ComponentEnum getType() {
        return null;
    }
}
