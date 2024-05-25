package org.game.isometric.component;

import org.game.component.Component;
import org.joml.Vector2f;

public class PositionComponent2D extends Component {
    private Vector2f position;
    private int floor;

    public PositionComponent2D(Vector2f position) {
        this.position = position;
        this.floor = 0;
    }

    public PositionComponent2D(Vector2f position, int floor) {
        this.position = position;
        this.floor = floor;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public ComponentEnum getType() {
        return ComponentEnum.PositionComponent2D;
    }
}
