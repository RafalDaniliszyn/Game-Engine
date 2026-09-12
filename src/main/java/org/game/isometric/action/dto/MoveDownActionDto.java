package org.game.isometric.action.dto;

import org.game.isometric.action.Action;

public class MoveDownActionDto extends ActionDto {

    private Action.Invoke invoke;

    public Action.Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Action.Invoke invoke) {
        this.invoke = invoke;
    }

    @Override
    public String toString() {
        return "MoveDownActionDto{}";
    }
}
