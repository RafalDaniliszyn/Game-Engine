package org.game.isometric.action;

import org.game.GameData;
import org.game.component.Component;
import org.game.entity.Entity;

public abstract class Action extends Component {
    private boolean removeEntityAfter;
    private boolean removeActionAfter;
    private final Invoke invoke;

    protected Action(boolean removeEntityAfter, boolean removeActionAfter, Invoke invoke) {
        this.removeEntityAfter = removeEntityAfter;
        this.removeActionAfter = removeActionAfter;
        this.invoke = invoke;
    }

    public abstract void processAction(Entity entity);

    public void processActions(Entity entity, GameData gameData) {}

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
