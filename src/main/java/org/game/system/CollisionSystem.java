package org.game.system;

import org.game.GameData;
import org.game.component.CollisionComponent;
import org.game.component.MeshComponent;
import org.game.component.MoveComponent;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.joml.Vector3f;

import java.util.Map;

public class CollisionSystem extends BaseSystem {

    public CollisionSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        checkCollision();
        moveBack();
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {
        setCollisionShapes();
    }

    private void moveBack() {
        getGameData().getEntities(CollisionComponent.class, PositionComponent.class).forEach((id, entity) -> {
            if (entity.getComponent(CollisionComponent.class).isCollision()) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
                System.out.println(positionComponent.getPosition().x + "  " + positionComponent.getLastPosition().x);
                Vector3f position = positionComponent.getPosition();
                Vector3f lastPosition = positionComponent.getLastPosition();
                if (lastPosition.x < position.x) {
                    position.x += 1.0f;
                }else {
                    position.x -= 1.0f;
                }
                if (lastPosition.z < position.z) {
                    position.z -= 1.0f;
                }else {
                    position.z += 1.0f;
                }
                positionComponent.setPosition(position);
                entity.getComponent(CollisionComponent.class).setCollision(false);
            }
        });
    }

    private void checkCollision() {
        Map<Long, Entity> entities = getGameData().getEntities(CollisionComponent.class, PositionComponent.class, MoveComponent.class);
        Map<Long, Entity> entitiesToCheckCollision = getGameData().getEntities(CollisionComponent.class, PositionComponent.class);


        entities.forEach((id, entity) -> {
            Vector3f position = entity.getComponent(PositionComponent.class).getPosition();
            float[] length = entity.getComponent(CollisionComponent.class).getShapeLength();
            entitiesToCheckCollision.forEach((id1, entity1) -> {
                if (entity.getId() == entity1.getId()) {
                    return;
                }
                Vector3f position1 = entity1.getComponent(PositionComponent.class).getPosition();
                float[] length1 = entity1.getComponent(CollisionComponent.class).getShapeLength();

                float aMinX = position.x;
                float aMaxX = position.x + length[0]/2;
                float aMinY = position.y;
                float aMaxY = position.y + length[1]/2;
                float aMinZ = position.z;
                float aMaxZ = position.z + length[2]/2;

                float bMinX = position1.x;
                float bMaxX = position1.x + length1[0]/2;
                float bMinY = position1.y;
                float bMaxY = position1.y + length1[1]/2;
                float bMinZ = position1.z;
                float bMaxZ = position1.z + length1[2]/2;
                System.out.println("a: " + aMinX + " " + aMinY + " " + aMinZ + " b: " + bMaxX + " " + bMaxY + " " + bMaxZ);
                entity.getComponent(CollisionComponent.class).setCollision(
                              aMinX <= bMaxX  &&
                              aMaxX >= bMinX  &&
                              aMinY <= bMaxY  &&
                              aMaxY >= bMinY  &&
                              aMinZ <= bMaxZ  &&
                              aMaxZ >= bMinZ
                );
            });
        });
    }

    private void setCollisionShapes() {
        getGameData()
                .getEntities(CollisionComponent.class, PositionComponent.class, MeshComponent.class)
                .forEach((id, entity) ->{

                    float[] vertices = entity.getComponent(MeshComponent.class).getVertices();
                    float[] shapeLength = getShapeLength(vertices);
                    entity.getComponent(CollisionComponent.class).setShapeLength(shapeLength);
                });
    }

    private float[] getShapeLength(float[] vertices) {
        float minX = vertices[0];
        float minY = vertices[1];
        float minZ = vertices[2];
        float maxX = vertices[0];
        float maxY = vertices[1];
        float maxZ = vertices[2];
        for (int i = 0; i < vertices.length; i+=9) {
            //min
            if (vertices[i] < minX) {
                minX = vertices[i];
            }
            if (vertices[i+1] < minY) {
                minY = vertices[i+1];
            }
            if (vertices[i+2] < minZ) {
                minZ = vertices[i+2];
            }
            //max
            if (vertices[i] > maxX) {
                maxX = vertices[i];
            }
            if (vertices[i+1] > maxY) {
                maxY = vertices[i+1];
            }
            if (vertices[i+2] > maxZ) {
                maxZ = vertices[i+2];
            }
        }
        float xLength = maxX - minX;
        float yLength = maxY - minY;
        float zLength = maxZ - minZ;

        return new float[]{
                xLength, yLength, zLength
        };
    }
}











