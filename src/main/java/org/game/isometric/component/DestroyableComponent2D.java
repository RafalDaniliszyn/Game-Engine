package org.game.isometric.component;

import org.game.component.Component;
import org.game.entity.Entity;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.EntityMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

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

    public List<Entity> afterDestroy() {
        Random random = new Random();
        List<Entity> entityResultList = new ArrayList<>();
        lootMap.forEach((label, probability) -> {
            int randomInteger = random.nextInt(100);
            if (randomInteger <= probability) {
                Entity entityBase = BlocksReader.getEntity(label);
                Entity newEntity = EntityMapper.getNewEntity(entityBase);
                entityResultList.add(newEntity);
            }
        });
        return entityResultList;
    }

    public int getAfterDestroyTextureId() {
        return afterDestroyTextureId;
    }

    public void setAfterDestroyTextureId(int afterDestroyTextureId) {
        this.afterDestroyTextureId = afterDestroyTextureId;
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