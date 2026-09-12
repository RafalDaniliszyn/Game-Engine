package org.game.isometric.blockLoader;

import org.game.component.Component;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.action.*;
import org.game.isometric.component.DestroyableComponent2D;
import org.game.isometric.component.DragComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.entity.ItemEntity2D;
import org.game.isometric.entity.TerrainEntity2D;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

public class EntityMapper {
    private static final String TERRAIN = "terrain";
    private static final String ITEM = "item";

    public static Entity getNewEntity(Entity entity, EntityType entityType, Vector2f position, int floor) {
        String type = entity.getProperties().getType();
        switch (type) {
            case TERRAIN -> {
                TerrainEntity2D terrainEntity = new TerrainEntity2D(toEntityProperties(entity.getProperties()), entityType);
                terrainEntity.addComponent(new PositionComponent2D(new Vector2f(0, 0), 0));
                terrainEntity.addComponents(toComponentList(entity));
                return terrainEntity;
            }
            case ITEM -> {
                ItemEntity2D itemEntity = new ItemEntity2D(new EntityProperties(entity.getProperties()), entityType, position, floor);
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
        if (TERRAIN.equals(entityDto.getEntityType())) {
            return new EntityProperties.EntityPropertiesBuilder()
                    .setDraggable(false)
                    .setCollidable(components.contains("CollisionComponent2D"))
                    .setLabel(entityDto.getLabel())
                    .setStackable(false)
                    .setQuantity(1)
                    .setStack(null)
                    .setType(TERRAIN)
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
                .setType(entityDto.getEntityType())
                .setDepth(entityDto.getDepth())
                .setReplaceableEdges(false)
                .build();
    }


    public static EntityProperties toDestroyedEntityProperties(EntityDto entityDto) {
        if (TERRAIN.equals(entityDto.getEntityType())) {
            return new EntityProperties.EntityPropertiesBuilder()
                    .setDraggable(false)
                    .setCollidable(false)
                    .setLabel(entityDto.getAfterDestroyLabel())
                    .setStackable(false)
                    .setQuantity(1)
                    .setStack(null)
                    .setType(TERRAIN)
                    .setDepth(entityDto.getDepth())
                    .setReplaceableEdges(false)
                    .build();
        }
        return null;
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
                .setActionList(toActionList(properties.getActionList()))
                .setActionListToDo(toActionList(properties.getActionListToDo()))
                .build();
    }

    public static List<Action> toActionList(List<Action> actionList) {
        List<Action> result = new ArrayList<>();
        for (Action action : actionList) {
            ActionEnum actionType = action.getActionType();
            switch (actionType) {
                case ExplosionAction -> {
                    ExplosionAction explosion = (ExplosionAction) action;
                    ExplosionAction newExplosionAction = new ExplosionAction(
                            explosion.getExplosionRange(),
                            explosion.isRemoveEntityAfter(),
                            explosion.isRemoveActionAfter(),
                            explosion.getInvoke(),
                            explosion.getDuration());
                    result.add(newExplosionAction);
                }
                case MoveUpAction -> {
                    MoveUpAction moveUpAction = new MoveUpAction(false, Action.Invoke.ON_ENTER);
                    result.add(moveUpAction);
                }
                case MoveDownAction -> {
                    MoveDownAction moveDownAction = new MoveDownAction(false, Action.Invoke.ON_ENTER);
                    result.add(moveDownAction);
                }
            }
        }
        return result;
    }

    public static List<Component> toComponentList(Entity entity) {
        if (entity == null) {
            return new ArrayList<>();
        }
        MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        DestroyableComponent2D destroyableComponent = entity.getComponent(DestroyableComponent2D.class);
        DragComponent2D dragComponent2D = entity.getComponent(DragComponent2D.class);
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
        if (dragComponent2D != null) {
            newComponentList.add(dragComponent2D);
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
