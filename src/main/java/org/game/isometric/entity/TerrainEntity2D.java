package org.game.isometric.entity;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.joml.Vector2f;
import java.util.UUID;

public class TerrainEntity2D extends Entity {

    private UUID uuid;

    public TerrainEntity2D(EntityProperties entityProperties, EntityType entityType) {
        super(entityProperties, entityType);
    }

    public TerrainEntity2D(int textureID, Vector2f position, int floor, EntityProperties properties, EntityType entityType) {
        super(properties, entityType);
        PositionComponent2D positionComponent2D = new PositionComponent2D(position, floor);
        MeshComponent2D meshComponent2D = new MeshComponent2D(textureID, new Vector2f(1.0f, 1.0f));
        addComponent(positionComponent2D);
        addComponent(meshComponent2D);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
