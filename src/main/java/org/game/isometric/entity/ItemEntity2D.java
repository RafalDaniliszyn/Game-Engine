package org.game.isometric.entity;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.component.DragComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.joml.Vector2f;

public class ItemEntity2D extends Entity {

    public ItemEntity2D(EntityProperties entityProperties) {
        super(entityProperties);
    }

    public ItemEntity2D(Vector2f position, int floor, String label, int textureId, boolean draggable) {
        super(new EntityProperties.EntityPropertiesBuilder()
                .setCollidable(false)
                .setDraggable(draggable)
                .setLabel(label)
                .setStackable(false)
                .setQuantity(1)
                .setDepth(-1.9f)
                .build());

        // TODO: 5/9/2024 Add scale settings and remove this test block
        Vector2f scale;
        if (label.equals("TREE_2D")) {
            scale = new Vector2f(2.0f, 2.0f);
        } else {
            scale = new Vector2f(1.0f, 1.0f);
        }

        PositionComponent2D positionComponent2D = new PositionComponent2D(position, floor);
        MeshComponent2D meshComponent2D = new MeshComponent2D(textureId, scale);
        if (draggable) {
            DragComponent2D dragComponent2D = new DragComponent2D();
            addComponent(dragComponent2D);
        }
        addComponent(positionComponent2D);
        addComponent(meshComponent2D);
    }
}
