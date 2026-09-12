package org.game.isometric.utils;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.ComponentEnum;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
        //return p1.getLabel().equals(p2.getLabel());
        Map<Side, Long> edgeMapP1 = p1.getReplaceableEdgeEntityIdMap();
        Map<Side, Long> edgeMapP2 = p2.getReplaceableEdgeEntityIdMap();
        if (Entity.State.REPLACED_EDGE.equals(e1.getState()) && Entity.State.REPLACED_EDGE.equals(e2.getState())) {
            return edgeMapP1.containsValue(e2.getId()) || edgeMapP2.containsValue(e1.getId());
        }

        return (p1.getLabel().contains(p2.getLabel()) || p2.getLabel().contains(p1.getLabel()))
                && p1.getLabel().contains("DESTROYED") == p2.getLabel().contains("DESTROYED");
    }

    public static boolean containsComponents(Entity entity, ComponentEnum... componentClass) {
        return new HashSet<>(entity.getComponentEnumSet()).containsAll(List.of(componentClass));
    }
}
