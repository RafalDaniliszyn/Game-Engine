package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.WorldSettings;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.entity.TerrainEntity2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.isometric.utils.TileUtils;
import org.joml.Vector2f;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.game.isometric.WorldSettings.CHUNK_SIZE;
import static org.game.isometric.texture2D.TextureEnum2D.DIRT2D;
import static org.game.isometric.texture2D.TextureEnum2D.GRASS2D;

public class Chunk {
    private final Deque<Long>[][] entitiesQueue;
    private final GameData gameData;
    private final int chunkX;
    private final int chunkY;

    public Chunk(GameData gameData, Integer chunkX, Integer chunkY) {
        this.gameData = gameData;
        this.chunkX = chunkX;
        this.chunkY = chunkY;

        this.entitiesQueue = new ArrayDeque[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                this.entitiesQueue[i][j] = new ArrayDeque<>();
            }
        }
    }

    public List<Long> fillChunk(Integer floor) {
        List<Long> entityIdList = new ArrayList<>();
        Integer textureIdGrass = TextureManager2D.getTextureId(GRASS2D);
        Integer textureIdDirt = TextureManager2D.getTextureId(DIRT2D);
        Integer currentTextureId = textureIdGrass;
        boolean collidable = false;
        float tileSize = WorldSettings.TILE_SIZE;
        if (floor < WorldSettings.FLOORS - 1) {
            currentTextureId = textureIdDirt;
            collidable = true;
        }
        int xOffset = chunkX * CHUNK_SIZE;
        int yOffset = chunkY * CHUNK_SIZE;
        boolean replaceableEdges;
        for (int i = 0; i < entitiesQueue.length; i++) {
            for (int j = 0; j < entitiesQueue.length; j++) {
                Map<Side, Integer> replaceableTextureIdMap = new HashMap<>();
                if (Objects.equals(currentTextureId, textureIdDirt)) {
                    Entity dirt = BlocksReader.getEntity("DIRT_2D"); // TODO: 5/8/2024 Change this line
                    replaceableTextureIdMap = dirt.getProperties().getReplaceableTextureIdMap();
                    replaceableEdges = true;
                } else {
                    replaceableEdges = false;
                }
                TerrainEntity2D terrainEntity2D = new TerrainEntity2D(currentTextureId, new Vector2f((i+xOffset) * tileSize, (j+yOffset) * tileSize), floor,
                        new EntityProperties.EntityPropertiesBuilder()
                                .setCollidable(collidable)
                                .setDraggable(false)
                                .setLabel(TextureManager2D.getTextureById(currentTextureId).getLabel())
                                .setStackable(false)
                                .setQuantity(1)
                                .setDepth(-2.0f)
                                .setReplaceableEdges(replaceableEdges)
                                .setReplaceableTextureIdMap(replaceableTextureIdMap)
                                .build());
                TileUtils.addEntityOnBottom(terrainEntity2D.getId() ,entitiesQueue[i][j]);
                gameData.addEntity(terrainEntity2D);
                entityIdList.add(terrainEntity2D.getId());
            }
        }
        return entityIdList;
    }

    public Deque<Long>[][] getEntitiesQueue() {
        return entitiesQueue;
    }
}
