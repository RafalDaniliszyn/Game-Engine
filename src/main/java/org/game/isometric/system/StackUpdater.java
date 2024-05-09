package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.helper.EntityPropertiesHelper;
import org.game.isometric.worldMap.WorldMapData;
import java.util.List;

public class StackUpdater {
    private final GameData gameData;
    private final WorldMapData worldMapData;

    public StackUpdater(GameData gameData, WorldMapData worldMapData) {
        this.worldMapData = worldMapData;
        this.gameData = gameData;
    }

    public void updateStack(int floor, int tileX, int tileY) {
        List<EntityProperties> entityPropertiesList = worldMapData.getEntityPropertiesList(floor, tileX, tileY);
        if (entityPropertiesList.size() < 2) {
            return;
        }
        EntityProperties entityProperties1 = entityPropertiesList.get(entityPropertiesList.size() - 1);
        EntityProperties entityProperties2 = entityPropertiesList.get(entityPropertiesList.size() - 2);

        Long topEntityId = worldMapData.getTopEntityIdFromTile(floor, tileX, tileY);
        if (topEntityId == null) {
            return;
        }

        Entity entity = gameData.getEntity(topEntityId);
        if (entityProperties1.isStackable()
                && entityProperties2.isStackable()
                && entityProperties1.getLabel().equals(entityProperties2.getLabel())) {

            int quantitySum = entityProperties1.getQuantity() + entityProperties2.getQuantity();
            if (quantitySum > entityProperties1.getStack().getMaxOnStack()) {
                return;
            }

            worldMapData.removeEntityFromTile(floor, tileX, tileY, topEntityId);
            gameData.removeEntity(topEntityId);

            Long topEntityStacked = worldMapData.getTopEntityIdFromTile(floor, tileX, tileY);
            entity = gameData.getEntity(topEntityStacked);
            EntityPropertiesHelper.setQuantity(entity, quantitySum);

            MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
            Integer textureId = EntityPropertiesHelper.getStackTextureId(entity, quantitySum);
            meshComponent.setTextureID(textureId);

        }
        //Stack has depth range for example if terrain level is on -2.0f depth level then stack of gold can be from -1.9f to -1.0f
        //Every stack begin from -1.9f and next layer subtract 0.1f
        MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
        float toAdd = entityPropertiesList.size() * 0.1f;
        entity.getProperties().setDepth(-1.9f + toAdd);
        System.out.println(-1.9f + toAdd);
    }
}
