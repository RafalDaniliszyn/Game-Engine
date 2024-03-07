package org.game.entity;

import org.game.component.mesh.MeshManager;
import org.game.component.CollisionComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.MoveComponent;
import org.game.component.PositionComponent;
import org.joml.Vector3f;

import java.util.List;

public class PlayerEntity extends Entity {

    boolean attachedCamera;

    public PlayerEntity(MeshManager meshManager, boolean lines) {
        super();
        PositionComponent positionComponent = new PositionComponent(
                new Vector3f(4.0f, 0.0f, 4.0f), 0.0f, 0.0f, 0.0f, new Vector3f(1.0f, 1.0f, 1.0f));
        addComponent(positionComponent);
        List<MeshComponent> meshComponent = meshManager.getMeshComponent("player");
        addComponent(new MoveComponent(new Vector3f(0.0f, 0.0f, 0.0f), 5.0f));

        if (lines) {
            meshComponent.forEach(mesh -> {
                addComponent(meshManager.getCollisionLines(mesh.getVertices(), positionComponent));
            });
        }
        //addComponent(meshComponent.get(0));
        addComponent(new CollisionComponent());
        this.attachedCamera = true;
    }
}
