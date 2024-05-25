package org.game.component;

import org.game.helper.PositionHelper;
import org.game.isometric.component.ComponentEnum;
import org.joml.Vector3f;

public class PositionComponent extends Component {
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private Vector3f scale;
    private Vector3f lastMoveVector;

    public PositionComponent(Vector3f position, float rotationX, float rotationY, float rotationZ, Vector3f scale) {
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.lastMoveVector = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = scale;
    }

    public PositionComponent(float[] mapVert, Vector3f position, float rotationX, float rotationY, float rotationZ, Vector3f scale) {
        position.y =  PositionHelper.getPositionY(mapVert, position.x, position.z);
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.lastMoveVector = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = scale;
    }

    public void rotateY(float offsetY) {
        if (rotationY + offsetY > 360) {
            offsetY = (rotationY + offsetY) - 360;
            rotationY = 0;
        } else if (rotationY + offsetY < 0) {
            offsetY = rotationY + offsetY;
            rotationY = 360;
        }
        rotationY += offsetY;
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

    public Vector3f getLastMoveVector() {
        return lastMoveVector;
    }

    public void setLastMoveVector(Vector3f lastMoveVector) {
        this.lastMoveVector = lastMoveVector;
    }

    @Override
    public ComponentEnum getType() {
        return null;
    }
}
