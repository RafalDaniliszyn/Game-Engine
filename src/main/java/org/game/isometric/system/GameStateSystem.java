package org.game.isometric.system;

import org.game.GameData;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.PlayerComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.system.BaseSystem;
import org.joml.Vector2f;

public class GameStateSystem extends BaseSystem {

    public GameStateSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        getGameData().getEntities(PlayerComponent2D.class).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            Vector2f position = positionComponent.getPosition();
            int chunkX = (int) ((position.x / WorldSettings.TILE_SIZE) / WorldSettings.CHUNK_SIZE);
            int chunkY = (int) ((position.y / WorldSettings.TILE_SIZE) / WorldSettings.CHUNK_SIZE);
            GameState.setCurrentChunkX(chunkX);
            GameState.setCurrentChunkY(chunkY);
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
