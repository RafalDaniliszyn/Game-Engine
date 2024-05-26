package org.game.system;

import org.game.GameData;
import org.game.isometric.component.ComponentEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseSystem {
    private final GameData gameData;
    private final List<ComponentEnum> requiredComponents;
    private final Set<Long> entitiesToProcess;

    public BaseSystem(GameData gameData) {
        this.gameData = gameData;
        this.requiredComponents = new ArrayList<>();
        this.entitiesToProcess = new HashSet<>();
    }

    public void addRequiredComponent(ComponentEnum... componentEnum) {
        requiredComponents.addAll(List.of(componentEnum));
    }

    public List<ComponentEnum> getRequiredComponents() {
        return requiredComponents;
    }

    public void addEntityToProcess(long entityId) {
        entitiesToProcess.add(entityId);
    }

    public void removeEntity(long entityId) {
        entitiesToProcess.remove(entityId);
    }

    public Set<Long> getEntitiesToProcess() {
        return entitiesToProcess;
    }

    public abstract void update(float deltaTime);
    public abstract void delete();
    public abstract void init();

    public GameData getGameData() {
        return gameData;
    }
}
