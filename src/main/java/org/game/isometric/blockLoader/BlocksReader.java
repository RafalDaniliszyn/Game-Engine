package org.game.isometric.blockLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.GameData;
import org.game.component.Component;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.GameLoadingState;
import org.game.isometric.action.*;
import org.game.isometric.action.dto.ActionDto;
import org.game.isometric.action.dto.ExplosionActionDto;
import org.game.isometric.component.*;
import org.game.isometric.entity.ItemEntity2D;
import org.game.isometric.entity.TerrainEntity2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.network.model.ChannelActiveModel;
import org.game.network.updater.FileUtils;

import java.io.IOException;
import java.util.*;

import static org.game.entity.Entity.State.DESTROYED;
import static org.game.entity.Entity.State.REPLACED_EDGE;

public class BlocksReader {
    private static final Map<String, Entity> loadedEntities;
    private static final Map<String, EntityDto> entityPrefabMap;

    static {
        loadedEntities = new HashMap<>();
        entityPrefabMap = new HashMap<>();
    }

    public static void readBlocks() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChannelActiveModel channelActiveModel = objectMapper.readValue(FileUtils.read("data/properties", "block.json"), ChannelActiveModel.class);
            List<EntityDto> entityDto = channelActiveModel.getEntityDto();
            for (EntityDto dto : entityDto) {
                entityPrefabMap.put(dto.getLabel(), dto);
                List<Entity> entityList = build(dto);
                entityList.forEach(entity -> {
                    loadedEntities.put(entity.getProperties().getLabel(), entity);
                    GameData.gameData.addEntity(entity);
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Entity getEntity(String label) {
        return loadedEntities.get(label);
    }

    public static Optional<Entity> getEntity(int textureId) {
        for (Entity entity : loadedEntities.values()) {
            if (entity.getComponent(MeshComponent2D.class).getTextureID() == textureId) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    // TODO: 10/30/2024 textures can be in Map<label, textureId>
    public static int getTextureIdByItemLabel(String label) {
        Entity entity = BlocksReader.getEntity(label);
        if (entity != null) {
            return entity.getComponent(MeshComponent2D.class).getTextureID();
        }
        return 0;
    }

    private static void addActions(EntityProperties properties, EntityDto entityDto) {
        List<ActionDto> actionList = entityDto.getActionList();
        if (actionList == null) {
            return;
        }
        for (ActionDto actionDto : actionList) {
            ActionEnum action = actionDto.getAction();
            List<Action> actionListToAdd = new ArrayList<>();
            switch (action) {
                case ExplosionAction -> {
                    ExplosionActionDto dto = (ExplosionActionDto) actionDto;
                    ExplosionAction explosionAction = new ExplosionAction(
                            dto.getExplosionRange(),
                            dto.isRemoveEntityAfter(),
                            dto.isRemoveActionAfter(),
                            dto.getInvoke(), dto.getDuration());
                    actionListToAdd.add(explosionAction);
                }
                case MoveUpAction -> {
                    MoveUpAction moveUpAction = new MoveUpAction(false, Action.Invoke.ON_ENTER);
                    actionListToAdd.add(moveUpAction);
                }
                case MoveDownAction -> {
                    MoveDownAction moveDownAction = new MoveDownAction(false, Action.Invoke.ON_ENTER);
                    actionListToAdd.add(moveDownAction);
                }
            }
            properties.setActionList(actionListToAdd);
        }
    }

    private static List<Entity> build(EntityDto entityDto) {
        Entity entity = null;
        List<Entity> entityList = new ArrayList<>();
        switch (entityDto.getEntityType()) {
            case "item" -> {
                entity = new ItemEntity2D(EntityMapper.toEntityProperties(entityDto), EntityType.LOCAL);
                entity.addComponents(getComponentList(entityDto));
                if (entityDto.getLabel().equals("TREE_2D")) {

                    //test only - animation

//                    UniqueAnimationAction action = new UniqueAnimationAction(false, false, Action.Invoke.ON_PUT);
//                    entity.getProperties().setActionList(List.of(action));
//                    Map<Integer, Integer> textureIdMap = new HashMap<>();
//                    Integer treeAnimation1 = TextureManager2D.getTextureIdByLabel("TREE_ANIMATION_1");
//                    Integer treeAnimation2 = TextureManager2D.getTextureIdByLabel("TREE_ANIMATION_2");
//                    Integer treeAnimation3 = TextureManager2D.getTextureIdByLabel("TREE_ANIMATION_3");
//                    Integer treeAnimation4 = TextureManager2D.getTextureIdByLabel("TREE_ANIMATION_4");
//                    textureIdMap.put(1, treeAnimation1);
//                    textureIdMap.put(2, treeAnimation2);
//                    textureIdMap.put(3, treeAnimation3);
//                    textureIdMap.put(4, treeAnimation4);
//
//                    Random random = new Random();
//                    AnimationComponent2D treeAnimation = new AnimationComponent2D(textureIdMap, 800 + random.nextFloat(500), "TREE_ANIMATION");
//                    treeAnimation.setLoop(true);
//                    treeAnimation.setActive(true);
                    //entity.addComponent(treeAnimation);

                    entity.getProperties().setCollidable(true);
                    boolean[][] collisionZone = new boolean[1][1];
                    collisionZone[0][0] = true;
//                    {
//                            {false, false, false, false},
//                            {false, false, false, false},
//                            {false, false, false, false},
//                            {false, false, true,  false}, //left down corner is 0,0
//                    };
                    //entity.getProperties().setCollisionZone(collisionZone);
                }
            }
            case "terrain" -> {
                entity = new TerrainEntity2D(EntityMapper.toEntityProperties(entityDto), EntityType.LOCAL);
                entity.addComponents(getComponentList(entityDto));

                Entity destroyedEntity = destroyableSettings(entityDto, entity);

                if (entityDto.hasReplaceableEdges()) {
                    List<Entity> replaceableEdgesEntityList = replaceableTerrainEdgesSettings(entityDto, entity);
                    if (replaceableEdgesEntityList.size() != 0) {
                        entityList.addAll(replaceableEdgesEntityList);
                    }
                    Map<Side, String> replaceableTexture = entityDto.getReplaceableTexture();
                    Map<Side, Integer> replaceableTextureIdMap = new HashMap<>();
                    final String label = entity.getProperties().getLabel();
                    replaceableTexture.forEach((side, path) -> {
                        Integer textureId = TextureManager2D.loadTexture(path, label);
                        replaceableTextureIdMap.put(side, textureId);
                    });
                    entity.getProperties().setReplaceableTextureIdMap(replaceableTextureIdMap);
                }

                System.out.println("BlockReader.build { " + entity + " }");
                if (destroyedEntity != null) {
                    entityList.add(destroyedEntity);
                    MeshComponent2D destroyedMeshComponent = destroyedEntity.getComponent(MeshComponent2D.class);
                }
                System.out.println("BlockReader.buildDestroyedEntity { " + destroyedEntity + " }");
            }
        }
        if (entity != null) {
            EntityProperties properties = entity.getProperties();
            addActions(properties, entityDto);
            entityList.add(entity);
        }
        return entityList;
    }

    public static void setRotatedChildren(Entity entity) {

    }

    private static List<Entity> replaceableTerrainEdgesSettings(EntityDto entityDto, Entity entity) {
        if (!"terrain".equals(entity.getProperties().getType())) {
            return Collections.emptyList();
        }
        List<Entity> edgeEntityList = new ArrayList<>();
        Map<Side, String> replaceableTexture = entityDto.getReplaceableTexture();
        Map<Side, Long> replaceableEntityIdMap = new HashMap<>();
        replaceableTexture.forEach((side, path) -> {
            //Build new terrainEntity in REPLACED_EDGE state
            TerrainEntity2D replacedEdgeEntity = new TerrainEntity2D(EntityMapper.toEntityProperties(entityDto), EntityType.LOCAL);
            replacedEdgeEntity.setState(REPLACED_EDGE);
            int edgeTextureId = TextureManager2D.loadTexture(path, entity.getProperties().getLabel());
            MeshComponent2D meshComponent = new MeshComponent2D(edgeTextureId);
            replacedEdgeEntity.addComponent(meshComponent);

            replacedEdgeEntity.getProperties().setLabel(entityDto.getLabel() + "_" + side);
            replaceableEntityIdMap.put(side, replacedEdgeEntity.getId());
            edgeEntityList.add(replacedEdgeEntity);
        });
        entity.getProperties().setReplaceableEdgeEntityIdMap(replaceableEntityIdMap);

        replaceableEntityIdMap.forEach(((side, id) -> {
            edgeEntityList.stream().filter(edgeEntity -> edgeEntity.getId() == id).findAny().ifPresent(replacedEdgeEntity -> setParentEntity(entity, replacedEdgeEntity));
        }));
        return edgeEntityList;
    }

    private static void setParentEntity(Entity parent, Entity child) {
        Map<Entity.State, Long> entityByState = parent.getEntityByState();
        child.setEntityByState(entityByState);
        DestroyableComponent2D destroyableComponent = parent.getComponent(DestroyableComponent2D.class);
        if (destroyableComponent != null) {
            child.addComponent(destroyableComponent);
        }
        EntityProperties childProperties = child.getProperties();
        EntityProperties parentProperties = parent.getProperties();
        childProperties.setReplaceableEdges(true);
        childProperties.setReplaceableEdgeEntityIdMap(parentProperties.getReplaceableEdgeEntityIdMap());
        childProperties.setReplaceableTextureIdMap(parentProperties.getReplaceableTextureIdMap());
    }

    private static Entity destroyableSettings(EntityDto entityDto, Entity entity) {
        if (!entityDto.isDestroyable()) {
            return null;
        }
        //Build new terrainEntity in DESTROYED state
        TerrainEntity2D destroyedTerrainEntity = new TerrainEntity2D(EntityMapper.toDestroyedEntityProperties(entityDto), EntityType.LOCAL);
        destroyedTerrainEntity.setState(DESTROYED);
        int afterDestroyTextureId = TextureManager2D.loadTexture(entityDto.getAfterDestroyTexturePath(), entity.getProperties().getLabel());
        MeshComponent2D meshComponent = new MeshComponent2D(afterDestroyTextureId);
        destroyedTerrainEntity.addComponent(meshComponent);

        //Add destroyableComponent to terrainEntity in ACTIVE state
        Map<String, Integer> dropMap = entityDto.getDrop();
        DestroyableComponent2D destroyableComponent =
                new DestroyableComponent2D(
                        afterDestroyTextureId,
                        entityDto.getAfterDestroyLabel(),
                        entityDto.getDestructionDifficulty(),
                        dropMap);
        entity.addComponent(destroyableComponent);
        //Add to terrainEntity in ACTIVE state reference destroyedTerrainEntity
        entity.addToEntityByState(destroyedTerrainEntity.getId(), DESTROYED);
        return destroyedTerrainEntity;
    }

    private static List<Component> getComponentList(EntityDto entityDto) {
        List<Component> componentList = new ArrayList<>();
        List<String> components = entityDto.getComponents();
        components.forEach(component -> {
            switch (component) {
                // TODO: 5/24/2024 Change to ComponentEnum
                case "CollisionComponent2D" -> componentList.add(new CollisionComponent2D());
                case "DragComponent2D" -> componentList.add(new DragComponent2D());
                case "MeshComponent2D" -> {
                    int[] width = new int[1];
                    int[] height = new int[1];
                    int textureId = TextureManager2D.loadTexture(entityDto.getTexturePath(), width, height);
                    if (entityDto.getLabel().equals("TREE_2D")) {
                        componentList.add(new MeshComponent2D(textureId, width[0], height[0]));
                    } else {
                        componentList.add(new MeshComponent2D(textureId));
                    }

                }
            }
        });
        return componentList;
    }
}
