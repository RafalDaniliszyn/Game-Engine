package org.game.isometric.blockLoader;

import org.game.component.Component;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.component.DestroyableComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.entity.ItemEntity2D;
import org.game.isometric.entity.TerrainEntity2D;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class EntityMapper {

    public static Entity getNewEntity(Entity entity) {
        String type = entity.getProperties().getType();
        switch (type) {
            case "terrain" -> {
                TerrainEntity2D terrainEntity = new TerrainEntity2D(toEntityProperties(entity.getProperties()));
                terrainEntity.addComponents(toComponentList(entity));
                return terrainEntity;
            }
            case "item" -> {
                ItemEntity2D itemEntity = new ItemEntity2D(toEntityProperties(entity.getProperties()));
                itemEntity.addComponent(new PositionComponent2D(new Vector2f(0, 0), 0));
                itemEntity.addComponents(toComponentList(entity));
                return itemEntity;
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


    private static EntityProperties toEntityProperties(EntityProperties properties) {
        return new EntityProperties.EntityPropertiesBuilder()
                .setCollidable(properties.isCollidable())
                .setDraggable(properties.isDraggable())
                .setStackable(properties.isStackable())
                .setLabel(properties.getLabel())
                .setQuantity(properties.getQuantity())
                .setStack(properties.getStack())
                .setType(properties.getType())
                .setDepth(properties.getDepth())
                .setReplaceableEdges(properties.hasReplaceableEdges())
                .setReplaceableTextureIdMap(properties.getReplaceableTextureIdMap())
                .build();
    }

    private static List<Component> toComponentList(Entity entity) {
        if (entity == null) {
            return new ArrayList<>();
        }
        MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        DestroyableComponent2D destroyableComponent = entity.getComponent(DestroyableComponent2D.class);
        List<Component> newComponentList = new ArrayList<>();
        if (meshComponent != null) {
            newComponentList.add(toMeshComponent2D(meshComponent));
        }
        if (positionComponent != null) {
            newComponentList.add(toPositionComponent2D(positionComponent));
        }
        if (destroyableComponent != null) {
            newComponentList.add(toDestroyableComponent2D(destroyableComponent));
        }
        return newComponentList;
    }

    private static MeshComponent2D toMeshComponent2D(MeshComponent2D meshComponent) {
        return new MeshComponent2D(meshComponent.getTextureID(), new Vector2f(meshComponent.getScale()));
    }
    private static PositionComponent2D toPositionComponent2D(PositionComponent2D positionComponent) {
        return new PositionComponent2D(new Vector2f(positionComponent.getPosition()), positionComponent.getFloor());
    }
    private static DestroyableComponent2D toDestroyableComponent2D(DestroyableComponent2D destroyableComponent) {
        return new DestroyableComponent2D(
                destroyableComponent.getAfterDestroyTextureId(),
                destroyableComponent.getAfterDestroyLabel(),
                destroyableComponent.getDestructionDifficulty(),
                destroyableComponent.getLootMap());
    }
}
