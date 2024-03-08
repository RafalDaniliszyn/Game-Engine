package org.game.component;

import org.joml.Vector3f;

public class MoveComponent extends Component{
    private Vector3f moveVector;
    private float speed;
    private boolean jump;

    public MoveComponent() {
        this.moveVector = new Vector3f(0.0f, 0.0f, 0.0f);
        this.speed = 0.0f;
    }

    public MoveComponent(Vector3f moveVector, float speed) {
        this.moveVector = moveVector;
        this.speed = speed;
    }

    public void setMoveVector(Vector3f moveVector) {
        this.moveVector = moveVector;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector3f getMoveVector() {
        return moveVector;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
