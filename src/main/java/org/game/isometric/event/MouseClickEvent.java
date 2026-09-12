package org.game.isometric.event;

public class MouseClickEvent extends Event {
    private final double x;
    private final double y;
    private final int floor;

    public MouseClickEvent(double x, double y, int floor) {
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getFloor() {
        return floor;
    }
}
