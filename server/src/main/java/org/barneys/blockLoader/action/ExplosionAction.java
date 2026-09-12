package org.barneys.blockLoader.action;

import org.barneys.PlayerEntityModel;

public class ExplosionAction extends Action {

    private final long duration;

    public ExplosionAction(boolean removeEntityAfter, boolean removeActionAfter, long duration, Invoke invoke) {
        super(removeEntityAfter, removeActionAfter, invoke);
        this.duration = duration;
    }

    @Override
    public ActionEnum getActionType() {
        return ActionEnum.ExplosionAction;
    }

    @Override
    public void processAction(PlayerEntityModel playerEntityModel) {

    }

    public long getDuration() {
        return duration;
    }
}
