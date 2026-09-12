package org.game.isometric.action;

import org.game.entity.Entity;
import org.game.isometric.component.AnimationComponent2D;

import java.util.Random;

public class UniqueAnimationAction extends Action {
    public UniqueAnimationAction(boolean removeEntityAfter, boolean removeActionAfter, Invoke invoke) {
        super(removeEntityAfter, removeActionAfter, invoke);
    }

    @Override
    public ActionEnum getActionType() {
        return ActionEnum.UniqueAnimationAction;
    }

    @Override
    public void processAction(Entity entity) {
        AnimationComponent2D component = entity.getComponent(AnimationComponent2D.class);
        Random random = new Random();
        component.setFrameDuration(component.getFrameDuration() + random.nextDouble(200.0));
    }
}
