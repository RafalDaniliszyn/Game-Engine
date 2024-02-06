package org.game.entity;

import org.game.MeshManager;
import org.game.component.CollisionComponent;
import org.game.component.PositionComponent;
import org.joml.Vector3f;

public class StaticObjectEntity extends Entity {

    public StaticObjectEntity(MeshManager meshManager, long meshId, Vector3f position, Vector3f rotation, Vector3f scale) {
        super();
        addComponent(new PositionComponent(position, rotation.x, rotation.y, rotation.z, scale));
        addComponent(meshManager.getMeshComponent(meshId));
        addComponent(new CollisionComponent());
    }
}
