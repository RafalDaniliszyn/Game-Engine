package org.game.isometric.system;

import org.game.GameData;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.StateChangedComponent2D;
import org.game.system.BaseSystem;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StateChangedSystem2D class is responsible for updating systems and their entitiesToProcess lists to reflect the current state of entities.
 * that have been marked with the {@link StateChangedComponent2D}.
 * Using the {@link org.game.entity.Entity#removeStateChangedComponent()} method to prevent infinite loops and StackOverflowException.
 * Responsibilities include:
 * - Removing entities from that no longer contain the required components
 * - Removing entities from the processing set if they no longer exist
 * - Adding entities that have required components by system
 */
public class StateChangedSystem2D extends BaseSystem {

    public StateChangedSystem2D(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        GameData gameData = getGameData();
        gameData.getEntities(ComponentEnum.StateChangedComponent2D).forEach((id, entity) -> {
            gameData.getSystems().forEach((systemName, system) -> {
                List<ComponentEnum> requiredComponents = system.getRequiredComponents();
                    Set<Long> entitiesToProcess = system.getEntitiesToProcess();
                    List<ComponentEnum> componentEnumList = entity.getComponentEnumList();
                    if (new HashSet<>(componentEnumList).containsAll(requiredComponents)) {
                        system.addEntityToProcess(id);
                    } else {
                        entitiesToProcess.remove(id);
                    }
            });
            entity.removeStateChangedComponent();
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
