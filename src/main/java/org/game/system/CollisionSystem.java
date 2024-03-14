package org.game.system;

import org.game.GameData;
import org.game.component.mesh.MeshManager;
import org.game.component.CollisionComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.MoveComponent;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.joml.Vector3f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class CollisionSystem extends BaseSystem {

    public CollisionSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        Map<Long, Entity> entities = getGameData().getEntities(CollisionComponent.class, PositionComponent.class, MoveComponent.class);
        Map<Long, Entity> entitiesToCheckCollision = getGameData().getEntities(CollisionComponent.class, PositionComponent.class);
        checkCollisions(entities, entitiesToCheckCollision);
        moveBack(deltaTime);
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {
        setCollisionShapes();
    }

    private void moveBack(float dt) {
        getGameData().getEntities(CollisionComponent.class, PositionComponent.class, MoveComponent.class).forEach((id, entity) -> {
            CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
            if (collisionComponent.anyCollision()) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
                Vector3f position = positionComponent.getPosition();
                Vector3f lastMoveVector = positionComponent.getLastMoveVector().negate();
                MoveComponent moveComponent = entity.getComponent(MoveComponent.class);

                Set<Long> collisionSet = collisionComponent.getEntityCollisions();
                Map<Long, Vector3f> shapeCollisions = collisionComponent.getShapeCollisions();
                Entity entity1 = getGameData().getEntity(collisionSet.stream().findFirst().orElse(null));
                if (entity1 == null) {
                    return;
                }
                CollisionComponent entity1CollisionComponent = entity1.getComponent(CollisionComponent.class);
                Map<Float[], Vector3f> shapes = new HashMap<>();
                entity1CollisionComponent.getShapes().forEach((shapeId, shape) -> {
                    if (shapeCollisions.containsKey(shapeId)) {
                        shapes.put(shape, shapeCollisions.get(shapeId));
                    }
                });

                Float[] shape = shapes.entrySet().stream().findFirst().get().getKey();
                Vector3f position1 = shapes.entrySet().stream().findFirst().get().getValue();
                float aMinX = position1.x + shape[0];
                float aMaxX = position1.x + shape[3];

                float rotationY = positionComponent.getRotationY();
                if (position.x > aMinX && position.x < aMaxX) {
                    rotationY = 180 - (2*rotationY);
                    lastMoveVector.rotateY((float) Math.toRadians(rotationY));
                }
                if (position.x < aMinX || position.x > aMaxX) {
                    rotationY = 180 + (2*rotationY);
                    lastMoveVector.rotateY((float) Math.toRadians(rotationY));
                }

                Vector3f positionTemp = new Vector3f(position);
                positionTemp.x -= lastMoveVector.x * moveComponent.getSpeed() * dt;
                positionTemp.z -= lastMoveVector.z * moveComponent.getSpeed() * dt;
                if (checkCollision(entity, positionTemp, entity1, position1)) {
                    lastMoveVector.rotateY(-(float) Math.toRadians(rotationY));
                }
                position.x -= lastMoveVector.x * moveComponent.getSpeed() * dt;
                position.z -= lastMoveVector.z * moveComponent.getSpeed() * dt;
                positionComponent.setPosition(position);
            }
        });
    }

    private boolean checkCollision(Entity entity, Vector3f position, Entity entityToCheckCollision, Vector3f position1) {
            Map<Long, Float[]> shapes = entity.getComponent(CollisionComponent.class).getShapes();
            AtomicBoolean anyCollision = new AtomicBoolean(false);
                if (entity.getId() != entityToCheckCollision.getId()) {
                    Map<Long, Float[]> shapes1 = entityToCheckCollision.getComponent(CollisionComponent.class).getShapes();
                    shapes.forEach((id, shape) -> {
                        shapes1.forEach((id1, shape1) -> {
                            float aMinX = position.x + shape[0];
                            float aMinY = position.y + shape[1];
                            float aMinZ = position.z + shape[2];
                            float aMaxX = position.x + shape[3];
                            float aMaxY = position.y + shape[4];
                            float aMaxZ = position.z + shape[5];

                            float bMinX = position1.x + shape1[0];
                            float bMinY = position1.y + shape1[1];
                            float bMinZ = position1.z + shape1[2];
                            float bMaxX = position1.x + shape1[3];
                            float bMaxY = position1.y + shape1[4];
                            float bMaxZ = position1.z + shape1[5];

                            if (aMinX <= bMaxX  &&
                                aMaxX >= bMinX  &&
                                aMinY <= bMaxY  &&
                                aMaxY >= bMinY  &&
                                aMinZ <= bMaxZ  &&
                                aMaxZ >= bMinZ) {
                                anyCollision.set(true);
                            }
                        });
                    });
                }
        return anyCollision.get();
    }

    private void checkCollisions(Map<Long, Entity> entities, Map<Long, Entity> entitiesToCheckCollision) {
        entities.forEach((id, entity) -> {
            Vector3f position = entity.getComponent(PositionComponent.class).getPosition();
            CollisionComponent collisionComponent = entity.getComponent(CollisionComponent.class);
            Map<Long, Float[]> shapes = collisionComponent.getShapes();
            entitiesToCheckCollision.forEach((id1, entity1) -> {
                if (entity.getId() != entity1.getId()) {
                    List<PositionComponent> positionComponentList = entity1.getComponents(PositionComponent.class);
                    CollisionComponent collisionComponent1 = entity1.getComponent(CollisionComponent.class);
                    Map<Long, Float[]> shapes1 = collisionComponent1.getShapes();
                    AtomicBoolean anyCollision = new AtomicBoolean(false);
                    shapes.forEach((shapeId, shape) -> {
                        shapes1.forEach((shape1Id, shape1) -> {
                            float aMinX = position.x + shape[0];
                            float aMinY = position.y + shape[1];
                            float aMinZ = position.z + shape[2];
                            float aMaxX = position.x + shape[3];
                            float aMaxY = position.y + shape[4];
                            float aMaxZ = position.z + shape[5];

                            for (int i = 0; i < positionComponentList.size(); i++) {
                                Vector3f position1 = positionComponentList.get(i).getPosition();
                                float bMinX = position1.x + shape1[0];
                                float bMinY = position1.y + shape1[1];
                                float bMinZ = position1.z + shape1[2];
                                float bMaxX = position1.x + shape1[3];
                                float bMaxY = position1.y + shape1[4];
                                float bMaxZ = position1.z + shape1[5];

                                if (aMinX <= bMaxX  &&
                                        aMaxX >= bMinX  &&
                                        aMinY <= bMaxY  &&
                                        aMaxY >= bMinY  &&
                                        aMinZ <= bMaxZ  &&
                                        aMaxZ >= bMinZ) {
                                    collisionComponent.setShapeCollisions(shape1Id, position1);
                                    collisionComponent.setEntityCollision(entity1.getId());
                                    anyCollision.set(true);
                                }
                            }
                        });
                    });
                    if (!anyCollision.get()) {
                        Set<Long> shapesId = collisionComponent1.getShapesId();
                        collisionComponent.removeCollision(shapesId);
                        collisionComponent.removeEntityCollision(entity1.getId());
                    }
                }
            });
        });
    }

    private void setCollisionShapes() {
        getGameData()
                .getEntities(CollisionComponent.class, PositionComponent.class, MeshComponent.class)
                .forEach((id, entity) ->{
                    List<MeshComponent> components = entity.getComponents(MeshComponent.class);
                    components.forEach(component -> {
                        float[] vertices = component.getVertices();
                        float[] shape = MeshManager.getShapeLength(vertices);
                        entity.getComponent(CollisionComponent.class).addShape(shape);
                    });
                });
    }
}











