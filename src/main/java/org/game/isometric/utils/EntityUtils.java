package org.game.isometric.utils;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.component.ComponentEnum;
import java.util.HashSet;
import java.util.List;

public class EntityUtils {
    public static Boolean compareLabels(Entity e1, Entity e2) {
        if (e1 == null || e2 == null) {
            return Boolean.FALSE;
        }
        EntityProperties p1 = e1.getProperties();
        EntityProperties p2 = e2.getProperties();
        if (p1 == null || p2 == null) {
            return Boolean.FALSE;
        }
        return p1.getLabel().equals(p2.getLabel());
    }

    public static boolean containsComponents(Entity entity, ComponentEnum... componentClass) {
        return new HashSet<>(entity.getComponentEnumList()).containsAll(List.of(componentClass));
    }
}
