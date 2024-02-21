package org.game.component;

import org.game.IdGenerator;

import java.util.*;

public class CollisionComponent extends Component {

    private Map<Long, Float[]> shapes;
    private Set<Long> shapeCollisions;
    private Set<Long> entityCollisions;

    public CollisionComponent() {
        shapeCollisions = new HashSet<>();
        shapes = new HashMap<>();
        entityCollisions = new HashSet<>();
    }

    public void addShape(float[] shape) {
        Float[] floatShape = new Float[shape.length];
        for (int i = 0; i < shape.length; i++) {
            floatShape[i] = shape[i];
        }
        shapes.put(IdGenerator.getNextId(), floatShape);
    }

    public void setEntityCollision(Long entityId) {
        entityCollisions.add(entityId);
    }

    public void removeEntityCollision(Long entityId) {
        entityCollisions.remove(entityId);
    }

    public void setShapeCollisions(Long id) {
        shapeCollisions.add(id);
    }
    public void removeCollision(Set<Long> ids) {
        shapeCollisions.removeAll(ids);
    }

    public boolean anyCollision() {
        return !shapeCollisions.isEmpty();
    }

    public Set<Long> getShapeCollisions() {
        return shapeCollisions;
    }

    public Set<Long> getEntityCollisions() {
        return entityCollisions;
    }

    public Map<Long, Float[]> getShapes() {
        return shapes;
    }

    public Set<Long> getShapesId() {
        return shapes.keySet();
    }
}
