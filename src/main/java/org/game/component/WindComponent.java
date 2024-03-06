package org.game.component;

public class WindComponent extends Component {
    private float lastWindSin;
    private boolean forwardDirection;

    public WindComponent() {
        this.lastWindSin = 0.1f;
        this.forwardDirection = true;
    }

    public float getLastWindSin() {
        return lastWindSin;
    }

    public void setLastWindSin(float lastWindSin) {
        this.lastWindSin = lastWindSin;
    }

    public boolean isForwardDirection() {
        return forwardDirection;
    }

    public void setForwardDirection(boolean forwardDirection) {
        this.forwardDirection = forwardDirection;
    }
}
