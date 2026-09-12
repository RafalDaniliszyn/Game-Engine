package org.game.isometric.component;

import org.game.component.Component;

/**
 * The DestroyComponent class is used to mark objects that are intended to be destroyed.
 * Once the object is destroyed, DestroyComponent is removed from its list of components.
 */
public class DestroyComponent2D extends Component {
    private final double destructionDelay;
    private double elapsedTime;
    private double lastUpdateTime;
    private final boolean destroyNow;
    private int x;
    private int y;
    private int floor;

    public DestroyComponent2D(double destructionDifficulty, boolean destroyNow, int x, int y, int floor) {
        this.destructionDelay = destructionDifficulty;
        this.destroyNow = destroyNow;
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public DestroyComponent2D(double destructionDifficulty, boolean destroyNow) {
        this.destructionDelay = destructionDifficulty;
        this.destroyNow = destroyNow;
    }

    public DestroyComponent2D(double destructionDifficulty, boolean destroyNow, ComponentSource source, int x, int y, int floor) {
        super(source);
        this.destructionDelay = destructionDifficulty;
        this.destroyNow = destroyNow;
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public double getDestructionDelay() {
        return destructionDelay;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void increaseElapsedTime(double time) {
        this.elapsedTime += time;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(double lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isDestroyNow() {
        return destroyNow;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public ComponentEnum getType() {
        return ComponentEnum.DestroyComponent2D;
    }
}
