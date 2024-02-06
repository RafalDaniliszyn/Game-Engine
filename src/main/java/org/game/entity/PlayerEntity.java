package org.game.entity;

import org.game.MeshManager;
import org.game.component.CollisionComponent;
import org.game.component.MeshComponent;
import org.game.component.MoveComponent;
import org.game.component.PositionComponent;
import org.joml.Vector3f;

public class PlayerEntity extends Entity {

    boolean attachedCamera;

    public PlayerEntity(MeshManager meshManager) {
        super();
        addComponent(new PositionComponent(
                new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, 0.0f, 0.0f, new Vector3f(5.0f, 5.0f, 5.0f)));
        MeshComponent player = meshManager.getMeshComponent(7L);
        addComponent(player);
        addComponent(new MoveComponent(new Vector3f(0.0f, 0.0f, 0.0f), 10.0f));
        addComponent(new CollisionComponent());
        this.attachedCamera = true;
    }
}
