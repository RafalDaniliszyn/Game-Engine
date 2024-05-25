package org.game.entity;

import org.game.component.LightComponent;
import org.game.component.PositionComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.mesh.MeshManager;
import org.joml.Vector3f;
import java.util.List;

public class LightSourceEntity extends Entity {

    public LightSourceEntity(MeshManager meshManager, String meshName, Vector3f position,
                              Vector3f rotation, Vector3f scale, boolean lines, Vector3f lightColor) {
        super();
        PositionComponent positionComponent = new PositionComponent(position, rotation.x, rotation.y, rotation.z, scale);
        List<MeshComponent> meshComponent = meshManager.getMeshComponent(meshName, rotation.y);
        addComponent(positionComponent);
        if (lines) {
            meshComponent.forEach(mesh -> {
                addComponent(meshManager.getCollisionLines(mesh.getVertices(), positionComponent));
            });
        }
        addComponents(meshComponent);
        LightComponent lightComponent = new LightComponent(new Vector3f(1.0f, 1.0f, 1.0f));
        addComponent(lightComponent);
    }
}
