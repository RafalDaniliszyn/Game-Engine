package org.game.component;

public class CollisionComponent extends Component {

    private float[] shapeLength;
    private boolean collision;

    public CollisionComponent() {
    }

    public CollisionComponent(float[] shapeLength) {
        this.shapeLength = shapeLength;
    }

    public float[] getShapeLength() {
        return shapeLength;
    }

    public void setShapeLength(float[] shapeLength) {
        this.shapeLength = shapeLength;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
