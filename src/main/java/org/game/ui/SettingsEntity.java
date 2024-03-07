package org.game.ui;

import org.game.GraphicsDisplay;
import org.game.component.mesh.MeshManager;
import org.game.component.ButtonComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.joml.Vector3f;

import java.util.List;

public class SettingsEntity extends Entity {

    public SettingsEntity(MeshManager meshManager) {
        super();
        PositionComponent positionComponent = new PositionComponent(new Vector3f(GraphicsDisplay.WIDTH/2.0f, 170.0f, -0.1f),
                0, 0, 0, new Vector3f(1.0f, 1.0f, 1.0f));
        addComponent(positionComponent);
        List<MeshComponent> meshComponent = meshManager.getMeshComponent("button");
        meshComponent.forEach(mesh -> {
            mesh.setSettings(true);
            mesh.setCullFace(false);
        });

        addComponent(meshComponent);
        addComponent(new ButtonComponent((x, y) -> {
            if (x < 100 && y < 100) {
               //Action
            }
        }));
    }
}
