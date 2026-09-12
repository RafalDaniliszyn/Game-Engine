package org.barneys.blockLoader;

import game.isometric.entity.Entity;
import game.isometric.entity.EntityProperties;
import game.isometric.entity.ServerEntity;
import org.barneys.blockLoader.action.Action;
import org.barneys.blockLoader.action.ExplosionAction;
import org.barneys.blockLoader.action.MoveDownAction;
import org.barneys.blockLoader.action.MoveUpAction;
import org.barneys.blockLoader.action.dto.ActionDto;
import org.barneys.blockLoader.action.dto.ExplosionActionDto;
import org.barneys.blockLoader.action.dto.MoveDownActionDto;
import org.barneys.blockLoader.action.dto.MoveUpActionDto;

import java.util.*;

public class EntityMapper {
    private static final String ITEM = "item";
    private static final String TERRAIN = "terrain";


    public static Optional<Entity> newItemEntity(EntityDto entityDto) {
        if (ITEM.equals(entityDto.getEntityType())) {
            List<String> components = entityDto.getComponents();
            if (components == null) {
                components = new ArrayList<>();
            }
            Entity entity = new ServerEntity(new EntityProperties.EntityPropertiesBuilder()
                    .setCollidable(components.contains("CollisionComponent2D"))
                    .setDraggable(true)
                    .setStackable(entityDto.isStackable())
                    .setDestroyable(entityDto.isDestroyable())
                    .setAfterDestroyLabel(entityDto.getAfterDestroyLabel())
                    .setDestructionDifficulty(entityDto.getDestructionDifficulty())
                    .setLabel(entityDto.getLabel())
                    .setQuantity(entityDto.getQuantity())
                    .setType(ITEM)
                    .setDepth(entityDto.getDepth())
                    .setReplaceableEdges(entityDto.hasReplaceableEdges())
                    .setReplaceableTextureIdMap(new HashMap<>())
                    .setActionList(toActionList(entityDto.getActionList()))
                    .build());
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public static List<Entity> toEntityList(EntityDto entityDto) {
        List<Entity> entityList = new ArrayList<>();
        if (entityDto == null) {
            return Collections.emptyList();
        }
        switch (entityDto.getEntityType()) {
            case ITEM -> {
                List<String> components = entityDto.getComponents();
                if (components == null) {
                    components = new ArrayList<>();
                }
                Entity entity = new ServerEntity(new EntityProperties.EntityPropertiesBuilder()
                        .setCollidable(components.contains("CollisionComponent2D"))
                        .setDraggable(true)
                        .setStackable(entityDto.isStackable())
                        .setDestroyable(entityDto.isDestroyable())
                        .setAfterDestroyLabel(entityDto.getAfterDestroyLabel())
                        .setDestructionDifficulty(entityDto.getDestructionDifficulty())
                        .setLabel(entityDto.getLabel())
                        .setQuantity(entityDto.getQuantity())
                        .setType(ITEM)
                        .setDepth(entityDto.getDepth())
                        .setReplaceableEdges(entityDto.hasReplaceableEdges())
                        .setReplaceableTextureIdMap(new HashMap<>())
                        .setActionList(toActionList(entityDto.getActionList()))
                        .build());
                entityList.add(entity);
            }
            case TERRAIN -> {
                List<String> components = entityDto.getComponents();
                if (components == null) {
                    components = new ArrayList<>();
                }
                Entity entity = new ServerEntity(new EntityProperties.EntityPropertiesBuilder()
                        .setCollidable(components.contains("CollisionComponent2D"))
                        .setDraggable(false)
                        .setStackable(false)
                        .setDestroyable(entityDto.isDestroyable())
                        .setAfterDestroyLabel(entityDto.getAfterDestroyLabel())
                        .setDestructionDifficulty(entityDto.getDestructionDifficulty())
                        .setLabel(entityDto.getLabel())
                        .setQuantity(1)
                        .setType(TERRAIN)
                        .setDepth(entityDto.getDepth())
                        .setReplaceableEdges(entityDto.hasReplaceableEdges())
                        .setReplaceableTextureIdMap(new HashMap<>())
                        .setActionList(toActionList(entityDto.getActionList()))
                        .setDropMap(entityDto.getDrop())
                        .build());
                entityList.add(entity);
                if (entityDto.hasReplaceableEdges()) {
                    List<Entity> sideList = replaceableTerrainEdgesSettings(entityDto, entity);
                    entityList.addAll(sideList);
                }

                if (entityDto.getAfterDestroyLabel() != null) {
                    Entity destroyed = new ServerEntity(new EntityProperties.EntityPropertiesBuilder()
                            .setCollidable(false)
                            .setDraggable(false)
                            .setStackable(false)
                            .setDestroyable(false)
                            .setLabel(entityDto.getAfterDestroyLabel())
                            .setQuantity(1)
                            .setStackable(false)
                            .setType(TERRAIN)
                            .setDepth(entityDto.getDepth())
                            .setReplaceableEdges(false)
                            .setReplaceableTextureIdMap(new HashMap<>())
                            .setActionList(new ArrayList<>())
                            .build());
                    entityList.add(destroyed);
                }
            }
        }
        return entityList;
    }

    private static List<Entity> replaceableTerrainEdgesSettings(EntityDto entityDto, Entity entity) {
        if (!"terrain".equals(entity.getProperties().getType())) {
            return Collections.emptyList();
        }
        List<Entity> edgeEntityList = new ArrayList<>();
        Map<Side, String> replaceableTexture = entityDto.getReplaceableTexture();
        replaceableTexture.forEach((side, path) -> {
            //Build new terrainEntity in REPLACED_EDGE state
            ServerEntity replacedEdgeEntity = new ServerEntity(
                    new EntityProperties.EntityPropertiesBuilder()
                            .setCollidable(entity.getProperties().isCollidable())
                            .setDraggable(false)
                            .setStackable(false)
                            .setDestroyable(entityDto.isDestroyable())
                            .setAfterDestroyLabel(entityDto.getAfterDestroyLabel())
                            .setDestructionDifficulty(entityDto.getDestructionDifficulty())
                            .setQuantity(1)
                            .setType(TERRAIN)
                            .setDepth(entityDto.getDepth())
                            .setReplaceableEdges(entityDto.hasReplaceableEdges())
                            .setReplaceableTextureIdMap(new HashMap<>())
                            .setActionList(toActionList(entityDto.getActionList()))
                            .setDropMap(entityDto.getDrop())
                            .build());


            replacedEdgeEntity.getProperties().setLabel(entityDto.getLabel() + "_" + side);
            edgeEntityList.add(replacedEdgeEntity);
        });
        return edgeEntityList;
    }

    private static List<Action> toActionList(List<ActionDto> actionDtoList) {
        if (actionDtoList == null) {
            return new ArrayList<>();
        }
        List<Action> actionList = new ArrayList<>();
        actionDtoList.forEach(actionDto -> {
            switch (actionDto.getAction()) {
                case ExplosionAction -> {
                    ExplosionActionDto explosionActionDto = (ExplosionActionDto) actionDto;
                    actionList.add(new ExplosionAction(
                            explosionActionDto.isRemoveEntityAfter(),
                            explosionActionDto.isRemoveActionAfter(),
                            explosionActionDto.getDuration(),
                            explosionActionDto.getInvoke()));
                }
                case MoveDownAction -> {
                    MoveDownActionDto moveDownActionDto = (MoveDownActionDto) actionDto;
                    actionList.add(new MoveDownAction(false, moveDownActionDto.getInvoke()));
                }
                case MoveUpAction -> {
                    MoveUpActionDto moveUpActionDto = (MoveUpActionDto) actionDto;
                    actionList.add(new MoveUpAction(false, moveUpActionDto.getInvoke()));
                }
            }
        });
        return actionList;
    }
}
