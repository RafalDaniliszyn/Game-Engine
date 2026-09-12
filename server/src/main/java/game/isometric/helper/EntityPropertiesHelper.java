package game.isometric.helper;

import game.isometric.entity.Entity;

public class EntityPropertiesHelper {

    public static void setQuantity(Entity entity, Integer quantity) {
        if (entity == null) {
            return;
        }
        entity.getProperties().setQuantity(quantity);
    }
}
