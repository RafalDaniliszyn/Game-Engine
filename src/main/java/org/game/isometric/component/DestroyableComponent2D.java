package org.game.isometric.component;

import org.game.component.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The DestroyableComponent class is a component that indicates an entity can be destroyed.
 */
public class DestroyableComponent2D extends Component {
    private final int afterDestroyTextureId;
    private final String afterDestroyLabel;
    private final double destructionDifficulty;
    private final Map<String, Integer> lootMap;

    public DestroyableComponent2D(int afterDestroyTextureId, String afterDestroyLabel, double destructionDifficulty, Map<String, Integer> lootMap) {
        this.afterDestroyTextureId = afterDestroyTextureId;
        this.afterDestroyLabel = afterDestroyLabel;
        this.destructionDifficulty = destructionDifficulty;
        this.lootMap = Objects.requireNonNullElseGet(lootMap, HashMap::new);
    }

    public int getAfterDestroyTextureId() {
        return afterDestroyTextureId;
    }


    public String getAfterDestroyLabel() {
        return afterDestroyLabel;
    }


    public double getDestructionDifficulty() {
        return destructionDifficulty;
    }

    public Map<String, Integer> getLootMap() {
        return lootMap;
    }

    @Override
    public ComponentEnum getType() {
        return ComponentEnum.DestroyableComponent2D;
    }
}