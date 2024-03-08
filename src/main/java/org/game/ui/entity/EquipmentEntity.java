package org.game.ui.entity;

import org.game.component.mesh.MeshManager;
import org.game.ui.component.UiComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.game.event.EquipmentEvent;
import org.game.event.EquipmentEventManager;
import org.game.event.EventEnum;
import org.game.event.EventManager;
import org.joml.Vector3f;
import java.util.List;

public class EquipmentEntity extends Entity {

    private EquipmentEventManager eventManager;

    public EquipmentEntity(MeshManager meshManager, long itemId, float x, float y) {
        super();
        PositionComponent positionComponent = new PositionComponent(new Vector3f(x, y, -0.1f),
                0, 0, 0, new Vector3f(1.0f, 1.0f, 1.0f));
        addComponent(positionComponent);
        List<MeshComponent> meshComponent = meshManager.getMeshComponent("button");
        meshComponent.forEach(mesh -> {
            mesh.setSettings(true);
            mesh.setCullFace(false);
        });
        addComponent(meshComponent);

        eventManager = new EquipmentEventManager();
        UiComponent box = new UiComponent(
                positionComponent.getPosition().x,
                positionComponent.getPosition().y,
                150, 150, new EquipmentEvent(EventEnum.USE, itemId), eventManager);
        addComponent(box);
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
