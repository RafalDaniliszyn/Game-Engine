package org.game.component;

import org.joml.Vector3f;

public class PositionComponent extends Component {
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private Vector3f scale;
    private Vector3f lastPosition;

    public PositionComponent(Vector3f position, float rotationX, float rotationY, float rotationZ, Vector3f scale) {
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
    }

    public void increaseRotationY() {
        this.rotationY += 1;
        if (this.rotationY > 360) {
            this.rotationY = 0;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Vector3f lastPosition) {
        this.lastPosition = lastPosition;
    }
}
