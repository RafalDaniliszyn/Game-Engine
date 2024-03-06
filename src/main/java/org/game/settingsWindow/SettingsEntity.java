package org.game.settingsWindow;

import org.game.MeshManager;
import org.game.component.ButtonComponent;
import org.game.component.MeshComponent;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.joml.Vector3f;

import java.util.List;

public class SettingsEntity extends Entity {

    public SettingsEntity(MeshManager meshManager) {
        super();
        PositionComponent positionComponent = new PositionComponent(new Vector3f(-10.0f, 9.0f, -1.0f),
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
