package org.game.isometric.component;

public class DirectionBlocked {
    private boolean rightBlocked;
    private boolean leftBlocked;
    private boolean upBlocked;
    private boolean downBlocked;

    public boolean isRightBlocked() {
        return rightBlocked;
    }

    public void setRightBlocked(boolean rightBlocked) {
        this.rightBlocked = rightBlocked;
    }

    public boolean isLeftBlocked() {
        return leftBlocked;
    }

    public void setLeftBlocked(boolean leftBlocked) {
        this.leftBlocked = leftBlocked;
    }

    public boolean isUpBlocked() {
        return upBlocked;
    }

    public void setUpBlocked(boolean upBlocked) {
        this.upBlocked = upBlocked;
    }

    public boolean isDownBlocked() {
        return downBlocked;
    }

    public void setDownBlocked(boolean downBlocked) {
        this.downBlocked = downBlocked;
    }
}
