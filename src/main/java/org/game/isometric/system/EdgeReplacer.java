package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.isometric.utils.TileUtils;
import org.game.isometric.worldMap.WorldMapData;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
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
            Vector2f centerEntityPosition = centerEntityPositionComponent.getPosition();

            TilePosition tilePosition = PositionUtils.getTilePosition(tileX, tileY);
            List<Long> adjacentTiles = TileUtils.getAdjacentTiles(entitiesIdByChunk.get(), tileX, tileY);
            List<Entity> differentEntityTypeList = this.getDifferentEntityTypeList(entityProperties.getLabel(), adjacentTiles);

            differentEntityTypeList.forEach(differentEntity -> {
                if (!differentEntity.getProperties().hasReplaceableEdges()) {
                    return;
                }
                PositionComponent2D differentEntityPositionComponent = differentEntity.getComponent(PositionComponent2D.class);
                if (differentEntityPositionComponent == null) {
                    return;
                }
                Vector2f position = differentEntityPositionComponent.getPosition();
                if (position.x < centerEntityPosition.x) {
                    Integer textureId = differentEntity.getProperties().getReplaceableTextureIdMap().get(Side.RIGHT);
                    differentEntity.getComponent(MeshComponent2D.class).setTextureID(textureId);
                }
                if (position.x > centerEntityPosition.x) {
                    Integer textureId = differentEntity.getProperties().getReplaceableTextureIdMap().get(Side.LEFT);
                    differentEntity.getComponent(MeshComponent2D.class).setTextureID(textureId);
                }
                if (position.y < centerEntityPosition.y) {
                    Integer textureId = differentEntity.getProperties().getReplaceableTextureIdMap().get(Side.UP);
                    differentEntity.getComponent(MeshComponent2D.class).setTextureID(textureId);
                }
                if (position.y > centerEntityPosition.y) {
                    Integer textureId = differentEntity.getProperties().getReplaceableTextureIdMap().get(Side.DOWN);
                    differentEntity.getComponent(MeshComponent2D.class).setTextureID(textureId);
                }
            });
        }
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
