package org.game.isometric.entity;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.CollisionComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PlayerComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.joml.Vector2f;

public class PlayerEntity2D extends Entity {

    public PlayerEntity2D(int textureID, int positionX, int positionY) {
        super(new EntityProperties.EntityPropertiesBuilder()
                .setCollidable(true)
                .setDraggable(false)
                .setLabel("player")
                .setStackable(false)
                .setQuantity(1)
                .setDepth(-1.0f)
                .build());

        int x = (int) (positionX * WorldSettings.TILE_SIZE);
        int y = (int) (positionY * WorldSettings.TILE_SIZE);

        Vector2f position = new Vector2f(x, y);

        PlayerComponent2D playerComponent2D = new PlayerComponent2D();
        PositionComponent2D positionComponent2D = new PositionComponent2D(position);
        MeshComponent2D meshComponent2D = new MeshComponent2D(textureID, new Vector2f(1.0f, 1.0f));
        MoveComponent2D moveComponent2D = new MoveComponent2D(350.0f);
        CollisionComponent2D collisionComponent2D = new CollisionComponent2D();
        addComponent(playerComponent2D);
        addComponent(positionComponent2D);
        addComponent(meshComponent2D);
        addComponent(moveComponent2D);
        addComponent(collisionComponent2D);
    }
}
