package org.game.entity;

import org.game.component.mesh.MeshManager;
import org.game.component.CollisionComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.PositionComponent;
import org.joml.Vector3f;
import java.util.List;

public class StaticObjectEntity extends Entity {
    public StaticObjectEntity(MeshManager meshManager, String meshName, Vector3f position,
                              Vector3f rotation, Vector3f scale, boolean lines) {
        super();
        PositionComponent positionComponent = new PositionComponent(position, rotation.x, rotation.y, rotation.z, scale);
        CollisionComponent collisionComponent = new CollisionComponent();
        List<MeshComponent> meshComponent = meshManager.getMeshComponent(meshName, rotation.y);
        addComponent(positionComponent);
        if (lines) {
            meshComponent.forEach(mesh -> {
                addComponent(meshManager.getCollisionLines(mesh.getVertices(), positionComponent));
            });
        }
        addComponents(meshComponent);
        addComponent(collisionComponent);
    }

    public StaticObjectEntity(MeshManager meshManager, String meshName, Vector3f position,
                              Vector3f rotation, Vector3f scale, boolean lines, EntityProperties properties) {
        super(properties);
        PositionComponent positionComponent = new PositionComponent(position, rotation.x, rotation.y, rotation.z, scale);
        CollisionComponent collisionComponent = new CollisionComponent();
        List<MeshComponent> meshComponent = meshManager.getMeshComponent(meshName, rotation.y);
        addComponent(positionComponent);
        if (lines) {
            meshComponent.forEach(mesh -> {
                addComponent(meshManager.getCollisionLines(mesh.getVertices(), positionComponent));
            });
        }
        addComponents(meshComponent);
        addComponent(collisionComponent);
    }

    public StaticObjectEntity(float[] mapVertices, MeshManager meshManager, String meshName, Vector3f position,
                              Vector3f rotation, Vector3f scale, boolean lines) {
        super();
        PositionComponent positionComponent = new PositionComponent(mapVertices, position, rotation.x, rotation.y, rotation.z, scale);
        CollisionComponent collisionComponent = new CollisionComponent();
        List<MeshComponent> meshComponent = meshManager.getMeshComponent(meshName, rotation.y);
        addComponent(positionComponent);
        if (lines) {
            meshComponent.forEach(mesh -> {
                addComponent(meshManager.getCollisionLines(mesh.getVertices(), positionComponent));
            });
        }
        addComponents(meshComponent);
        addComponent(collisionComponent);
    }
}
