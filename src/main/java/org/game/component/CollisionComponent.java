package org.game.component;

import org.game.helper.IdGenerator;
import org.joml.Vector3f;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollisionComponent extends Component {

    private final Map<Long, Float[]> shapes;
    private final Map<Long, Vector3f> shapeCollisions;
    private final Set<Long> entityCollisions;

    public CollisionComponent() {
        shapeCollisions = new HashMap<>();
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

    public void setShapeCollisions(Long id, Vector3f position) {
        shapeCollisions.put(id, position);
    }
    public void removeCollision(Set<Long> ids) {
        for (Long id : ids) {
            shapeCollisions.remove(id);
        }
    }

    public boolean anyCollision() {
        return !shapeCollisions.isEmpty();
    }

    public Map<Long, Vector3f> getShapeCollisions() {
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
