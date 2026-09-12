package org.barneys.blockLoader.action.dto;

import org.barneys.blockLoader.action.Action;

public class MoveUpActionDto extends ActionDto {
    private Action.Invoke invoke;

    public Action.Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Action.Invoke invoke) {
        this.invoke = invoke;
    }
}
