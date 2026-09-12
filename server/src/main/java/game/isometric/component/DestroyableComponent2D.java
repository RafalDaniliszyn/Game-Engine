package game.isometric.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The DestroyableComponent class is a component that indicates an entity can be destroyed.
 */
public class DestroyableComponent2D extends Component {
    private int afterDestroyTextureId;
    private String afterDestroyLabel;
    private double destructionDifficulty;
    private final Map<String, Integer> lootMap;

    public DestroyableComponent2D(int afterDestroyTextureId, String afterDestroyLabel, double destructionDifficulty, Map<String, Integer> lootMap) {
        this.afterDestroyTextureId = afterDestroyTextureId;
        this.afterDestroyLabel = afterDestroyLabel;
        this.destructionDifficulty = destructionDifficulty;
        this.lootMap = Objects.requireNonNullElseGet(lootMap, HashMap::new);
    }

    public DestroyableComponent2D() {
        lootMap = new HashMap<>();
    }

    public String getAfterDestroyLabel() {
        return afterDestroyLabel;
    }

    public void setAfterDestroyLabel(String afterDestroyLabel) {
        this.afterDestroyLabel = afterDestroyLabel;
    }

    public double getDestructionDifficulty() {
        return destructionDifficulty;
    }

    public void setDestructionDifficulty(double destructionDifficulty) {
        this.destructionDifficulty = destructionDifficulty;
    }

    public Map<String, Integer> getLootMap() {
        return lootMap;
    }

    @Override
    public ComponentEnum getType() {
        return ComponentEnum.DestroyableComponent2D;
    }
}