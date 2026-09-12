package org.barneys.worldMap;

import game.isometric.entity.Entity;
import game.isometric.entity.EntityProperties;
import game.isometric.helper.EntityPropertiesHelper;
import java.util.List;

public class StackUpdater {
    private final WorldMap worldMap;

    public StackUpdater(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void updateStack(int floor, int tileX, int tileY) {
        List<EntityProperties> entityPropertiesList = worldMap.getEntityPropertiesList(floor, tileX, tileY);
        if (entityPropertiesList.size() < 2) {
            return;
        }
        EntityProperties entityProperties1 = entityPropertiesList.get(entityPropertiesList.size() - 1);
        EntityProperties entityProperties2 = entityPropertiesList.get(entityPropertiesList.size() - 2);

        Long topEntityId = worldMap.getTopEntityIdFromTile(floor, tileX, tileY);
        if (topEntityId == null) {
            return;
        }

        Entity entity = worldMap.getEntity(topEntityId);
        if (entityProperties1.isStackable()
                && entityProperties2.isStackable()
                && entityProperties1.getLabel().equals(entityProperties2.getLabel())) {

            int quantitySum = entityProperties1.getQuantity() + entityProperties2.getQuantity();
            if (quantitySum > entityProperties1.getStack().getMaxOnStack()) {
                return;
            }

            worldMap.removeEntityFromTile(floor, tileX, tileY, topEntityId);
            worldMap.removeEntity(topEntityId);

            Long topEntityStacked = worldMap.getTopEntityIdFromTile(floor, tileX, tileY);
            if (topEntityStacked == null) {
                return;
            }
            entity = worldMap.getEntity(topEntityStacked);
            EntityPropertiesHelper.setQuantity(entity, quantitySum);
        }
        //Stack has depth range for example if terrain level is on -2.0f depth level then stack of gold can be from -1.9f to -1.0f
        //Every stack begin from -1.9f and next layer subtract 0.1f
        float toAdd = entityPropertiesList.size() * 0.1f;
        entity.getProperties().setDepth(-1.9f + toAdd);
        System.out.println(-1.9f + toAdd);
    }
}
