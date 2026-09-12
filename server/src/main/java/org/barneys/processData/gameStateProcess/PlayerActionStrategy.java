package org.barneys.processData.gameStateProcess;

import org.barneys.model.GameStateModel;

public interface PlayerActionStrategy {
    void execute(GameStateModel model);
}
