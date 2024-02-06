package org.game.system;

import org.game.GameData;

public abstract class BaseSystem {
    private GameData gameData;

    public BaseSystem(GameData gameData) {
        this.gameData = gameData;
    }

    public abstract void update(float deltaTime);
    public abstract void delete();
    public abstract void init();

    public GameData getGameData() {
        return gameData;
    }
}
