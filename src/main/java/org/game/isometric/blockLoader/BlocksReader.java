package org.game.isometric.blockLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.component.Component;
import org.game.editWindow.ComboModel;
import org.game.editWindow.Frame;
import org.game.entity.Entity;
import org.game.isometric.component.CollisionComponent2D;
import org.game.isometric.component.DestroyableComponent2D;
import org.game.isometric.component.DragComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.entity.ItemEntity2D;
import org.game.isometric.entity.TerrainEntity2D;
import org.game.isometric.texture2D.TextureManager2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlocksReader {

    // TODO: 5/9/2024 Change test path
    private static final String path = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\blocks\\block.json";
    private static Map<String, Entity> loadedEntities;
    static {
        loadedEntities = new HashMap<>();
    }

    public static void readBlocks() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EntityDto[] entityDto = objectMapper.readValue(new File(path), EntityDto[].class);
            for (EntityDto dto : entityDto) {
                Entity entity = build(dto);
                loadedEntities.put(dto.getTextureLabel(), entity);
                System.out.println(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Entity getEntity(String label) {
        return loadedEntities.get(label);
    }

    private static Entity build(EntityDto entityDto) {
        Entity entity = null;
        switch (entityDto.getType()) {
            case "item" -> {
                entity = new ItemEntity2D(EntityMapper.toEntityProperties(entityDto));
                entity.addComponents(getComponentList(entityDto));
                MeshComponent2D meshComponent2D = entity.getComponent(MeshComponent2D.class);
                Frame.addTextureToComboBox(new ComboModel(entityDto.getTextureLabel(), meshComponent2D.getTextureID(), entityDto.getTexturePath()));
            }
            case "terrain" -> {
                entity = new TerrainEntity2D(EntityMapper.toEntityProperties(entityDto));
                entity.addComponents(getComponentList(entityDto));
                MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);

                if (entityDto.hasReplaceableEdges()) {
                    Map<Side, String> replaceableTexture = entityDto.getReplaceableTexture();
                    Map<Side, Integer> replaceableTextureIdMap = new HashMap<>();
                    replaceableTexture.forEach((side, path) -> {
                        Integer textureId = TextureManager2D.loadTexture(path);
                        replaceableTextureIdMap.put(side, textureId);
                    });
                    entity.getProperties().setReplaceableTextureIdMap(replaceableTextureIdMap);
                }
                if (entityDto.isDestroyable()) {
                    Integer afterDestroyTextureId = TextureManager2D.loadTexture(entityDto.getAfterDestroyTexturePath());
                    Map<String, Integer> dropMap = entityDto.getDrop();
                    DestroyableComponent2D destroyableComponent =
                            new DestroyableComponent2D(
                                    afterDestroyTextureId,
                                    entityDto.getAfterDestroyLabel(),
                                    entityDto.getDestructionDifficulty(),
                                    dropMap);
                    entity.addComponent(destroyableComponent);
                }

                System.out.println(meshComponent.getTextureID());
                Frame.addTextureToComboBox(new ComboModel(entityDto.getTextureLabel(), meshComponent.getTextureID(), entityDto.getTexturePath()));
            }
        }
        return entity;
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
                    int textureId = TextureManager2D.loadTexture(entityDto.getTexturePath());
                    componentList.add(
                            new MeshComponent2D(textureId));
                }
            }
        });
        return componentList;
    }
}
