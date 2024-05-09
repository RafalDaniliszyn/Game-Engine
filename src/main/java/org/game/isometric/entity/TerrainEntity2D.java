package org.game.isometric.entity;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.joml.Vector2f;

public class TerrainEntity2D extends Entity {

    public TerrainEntity2D(EntityProperties entityProperties) {
        super(entityProperties);
    }

    public TerrainEntity2D(int textureID, Vector2f position, int floor, EntityProperties properties) {
        super(properties);
        PositionComponent2D positionComponent2D = new PositionComponent2D(position, floor);
        MeshComponent2D meshComponent2D = new MeshComponent2D(textureID, new Vector2f(1.0f, 1.0f));
        addComponent(positionComponent2D);
        addComponent(meshComponent2D);
    }
}
