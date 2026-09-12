package org.barneys.blockLoader.action;

import org.barneys.PlayerEntityModel;

public abstract class Action {
    private boolean removeEntityAfter;
    private boolean removeActionAfter;
    private final Invoke invoke;

    protected Action(boolean removeEntityAfter, boolean removeActionAfter, Invoke invoke) {
        this.removeEntityAfter = removeEntityAfter;
        this.removeActionAfter = removeActionAfter;
        this.invoke = invoke;
    }

    public abstract ActionEnum getActionType();

    public abstract void processAction(PlayerEntityModel playerEntityModel);

    public boolean isRemoveEntityAfter() {
        return removeEntityAfter;
    }

    public void setRemoveEntityAfter(boolean removeEntityAfter) {
        this.removeEntityAfter = removeEntityAfter;
    }

    public boolean isRemoveActionAfter() {
        return removeActionAfter;
    }

    public void setRemoveActionAfter(boolean removeActionAfter) {
        this.removeActionAfter = removeActionAfter;
    }

    public Invoke getInvoke() {
        return invoke;
    }

    public enum Invoke {
        ON_ENTER, ON_PUT
    }
}
