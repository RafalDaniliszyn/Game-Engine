package org.game.isometric.blockLoader;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.entity.ItemEntity2D;
import org.game.isometric.entity.TerrainEntity2D;
import java.util.List;

public class EntityMapper {

    public static Entity getNewEntity(Entity entity) {
        String type = entity.getProperties().getType();
        switch (type) {
            case "terrain" -> {
                return new TerrainEntity2D(entity.getProperties());
            }
            case "item" -> {
                return new ItemEntity2D(entity.getProperties());
            }
            default -> {
                return null;
            }
        }
    }

    public static EntityProperties toEntityProperties(EntityDto entityDto) {
        List<String> components = entityDto.getComponents();
        if ("terrain".equals(entityDto.getType())) {
            return new EntityProperties.EntityPropertiesBuilder()
                    .setDraggable(false)
                    .setCollidable(components.contains("CollisionComponent2D"))
                    .setLabel(entityDto.getLabel())
                    .setStackable(false)
                    .setQuantity(1)
                    .setStack(null)
                    .setType("terrain")
                    .setDepth(entityDto.getDepth())
                    .setReplaceableEdges(entityDto.hasReplaceableEdges())
                    .build();
        }
        return new EntityProperties.EntityPropertiesBuilder()
                .setDraggable(components.contains("DragComponent2D"))
                .setCollidable(components.contains("CollisionComponent2D"))
                .setLabel(entityDto.getLabel())
                .setStackable(entityDto.isStackable())
                .setQuantity(entityDto.getQuantity())
                .setStack(null) // TODO: 4/22/2024 add Stack
                .setType(entityDto.getType())
                .setDepth(entityDto.getDepth())
                .setReplaceableEdges(false)
                .build();
    }
}
