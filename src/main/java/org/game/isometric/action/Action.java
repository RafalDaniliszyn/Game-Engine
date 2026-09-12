package org.game.isometric.action;

import org.game.GameData;
import org.game.entity.Entity;

public abstract class Action {

    private final boolean removeEntityAfter;
    private boolean remove;
    private boolean removeActionAfter;
    private final Invoke invoke;

    protected Action(boolean removeEntityAfter, boolean removeActionAfter, Invoke invoke) {
        this.removeEntityAfter = removeEntityAfter;
        this.removeActionAfter = removeActionAfter;
        this.invoke = invoke;
        this.remove = false;
    }

    public abstract ActionEnum getActionType();

    public abstract void processAction(Entity entity);

    public void processActions(Entity entity, GameData gameData) {}

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        if (this.removeEntityAfter) {
            this.remove = remove;
        }
    }

    public boolean isRemoveActionAfter() {
        return removeActionAfter;
    }

    public void setRemoveActionAfter(boolean removeActionAfter) {
        this.removeActionAfter = removeActionAfter;
    }

    public boolean isRemoveEntityAfter() {
        return removeEntityAfter;
    }

    public Invoke getInvoke() {
        return invoke;
    }

    public enum Invoke {
        ON_ENTER, ON_PUT
    }
}
