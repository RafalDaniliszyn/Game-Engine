package org.game.isometric.entity;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.DragComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.joml.Vector2f;

public class ItemEntity2D extends Entity {

    public ItemEntity2D(EntityProperties entityProperties, EntityType entityType) {
        super(entityProperties, entityType);
        this.addComponent(new PositionComponent2D(new Vector2f(0, 0), 0));
    }

    public ItemEntity2D(EntityProperties entityProperties, EntityType entityType, Vector2f position, int floor) {
        super(entityProperties, entityType);
        this.addComponent(new PositionComponent2D(new Vector2f(position), floor));
    }
}
