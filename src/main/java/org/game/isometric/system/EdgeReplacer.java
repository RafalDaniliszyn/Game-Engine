package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.utils.EntityUtils;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TileUtils;
import org.game.isometric.worldMap.WorldMapData;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EdgeReplacer {

    private final GameData gameData;
    private final WorldMapData worldMapData;

    public EdgeReplacer(GameData gameData, WorldMapData worldMapData) {
        this.gameData = gameData;
        this.worldMapData = worldMapData;
    }

    public void replaceEdges(int floor, int tileX, int tileY) {
        Long entityOnBottom = worldMapData.getBottomEntityIdFromTile(floor, tileX, tileY);
        if (entityOnBottom == null) {
            return;
        }

        Entity entity = gameData.getEntity(entityOnBottom);
        if (entity != null) {
            EntityProperties entityProperties = entity.getProperties();
            Optional<Deque<Long>[][]> entitiesIdByChunk = worldMapData.getEntityIDsOnChunk(floor, tileX, tileY);
            if (entitiesIdByChunk.isEmpty()) {
                return;
            }
            PositionComponent2D centerEntityPositionComponent = entity.getComponent(PositionComponent2D.class);
            if (centerEntityPositionComponent == null) {
                return;
            }
            Vector2f centerEntityPosition = centerEntityPositionComponent.getPosition();
            List<Long> adjacentTiles = TileUtils.getAdjacentTiles(entitiesIdByChunk.get(), tileX, tileY);
            List<Entity> differentEntityTypeList = this.getDifferentEntityTypeList(entityProperties.getLabel(), adjacentTiles);

            this.replaceTextures(centerEntityPosition, differentEntityTypeList);
        }
    }

    private void replaceTextures(Vector2f centerEntityPosition, List<Entity> differentEntityTypeList) {
        differentEntityTypeList.forEach(differentEntity -> {
            if (!differentEntity.getProperties().hasReplaceableEdges()) {
                return;
            }
            PositionComponent2D differentEntityPositionComponent = differentEntity.getComponent(PositionComponent2D.class);
            if (differentEntityPositionComponent == null) {
                return;
            }
            Vector2f position = differentEntityPositionComponent.getPosition();
            Map<Side, Integer> replaceableTextureIdMap = differentEntity.getProperties().getReplaceableTextureIdMap();
            MeshComponent2D meshComponent = differentEntity.getComponent(MeshComponent2D.class);

            float tileSize = WorldSettings.TILE_SIZE;
            Long bottomEntityIdFromTileLeft = worldMapData.getBottomEntityIdFromTile(GameState.getCurrentFloor(), (int) (position.x/tileSize) - 1, (int) (position.y/tileSize));
            Long bottomEntityIdFromTileRight = worldMapData.getBottomEntityIdFromTile(GameState.getCurrentFloor(), (int) (position.x/tileSize) + 1, (int) (position.y/tileSize));
            Long bottomEntityIdFromTileUp = worldMapData.getBottomEntityIdFromTile(GameState.getCurrentFloor(), (int) (position.x/tileSize), (int) (position.y/tileSize) + 1);
            Long bottomEntityIdFromTileDown = worldMapData.getBottomEntityIdFromTile(GameState.getCurrentFloor(), (int) (position.x/tileSize), (int) (position.y/tileSize) - 1);
            Entity entityLeft = gameData.getEntity(bottomEntityIdFromTileLeft);
            Entity entityRight = gameData.getEntity(bottomEntityIdFromTileRight);
            Entity entityUp = gameData.getEntity(bottomEntityIdFromTileUp);
            Entity entityDown = gameData.getEntity(bottomEntityIdFromTileDown);

            switch (PositionUtils.getRelativePosition(position, centerEntityPosition)) {
                case UP -> {
                    if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityLeft, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.DOWN_LEFT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityRight, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.DOWN_RIGHT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else {
                        Integer textureId = replaceableTextureIdMap.get(Side.DOWN);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    }
                }
                case DOWN -> {
                    if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityLeft, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.UP_LEFT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityRight, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.UP_RIGHT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else {
                        Integer textureId = replaceableTextureIdMap.get(Side.UP);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    }
                    if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityLeft, differentEntity)) && Boolean.FALSE.equals(EntityUtils.compareLabels(entityRight, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.CENTER);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    }
                }
                case LEFT -> {
                    if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityUp, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.UP_RIGHT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityDown, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.DOWN_RIGHT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else {
                        Integer textureId = replaceableTextureIdMap.get(Side.RIGHT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    }
                }
                case RIGHT -> {
                    if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityUp, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.UP_LEFT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else if (Boolean.FALSE.equals(EntityUtils.compareLabels(entityDown, differentEntity))) {
                        Integer textureId = replaceableTextureIdMap.get(Side.DOWN_LEFT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    } else {
                        Integer textureId = replaceableTextureIdMap.get(Side.LEFT);
                        if (textureId != null) {
                            meshComponent.setTextureID(textureId);
                        }
                    }
                }
            }
        });
    }

    private List<Entity> getDifferentEntityTypeList(String tileLabel, List<Long> adjacentTiles) {
        List<Entity> differentEntityList = new ArrayList<>();
        adjacentTiles.forEach(tileId -> {
            Entity entity = gameData.getEntity(tileId);
            if (entity != null && !tileLabel.equals(entity.getProperties().getLabel())) {
                differentEntityList.add(entity);
            }
        });
        return differentEntityList;
    }
}
