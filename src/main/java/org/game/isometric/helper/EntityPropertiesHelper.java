package org.game.isometric.helper;

import org.game.entity.Entity;

public class EntityPropertiesHelper {

    public static void setQuantity(Entity entity, Integer quantity) {
        if (entity == null) {
            return;
        }
        entity.getProperties().setQuantity(quantity);
    }

    public static Integer getStackTextureId(Entity entity, Integer quantity) {
        if (entity != null && entity.getProperties() != null && entity.getProperties().getStack() != null) {
            return entity.getProperties().getStack().getTextureId(quantity);
        }
        return null;
    }

}
