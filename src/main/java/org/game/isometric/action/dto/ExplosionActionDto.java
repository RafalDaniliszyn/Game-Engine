package org.game.isometric.action.dto;

import org.game.isometric.action.Action;

public class ExplosionActionDto extends ActionDto {
    private int explosionRange;
    private boolean removeEntityAfter;
    private boolean removeActionAfter;
    private long duration;
    private Action.Invoke invoke;

    public int getExplosionRange() {
        return explosionRange;
    }

    public void setExplosionRange(int explosionRange) {
        this.explosionRange = explosionRange;
    }

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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Action.Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Action.Invoke invoke) {
        this.invoke = invoke;
    }

    @Override
    public String toString() {
        return "ExplosionActionDto{" +
                "explosionRange=" + explosionRange +
                ", removeEntityAfter=" + removeEntityAfter +
                ", removeActionAfter=" + removeActionAfter +
                ", invoke=" + invoke;
    }
}
