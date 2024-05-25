package org.game.isometric.system;

import org.game.GameData;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.system.BaseSystem;
import org.joml.Vector2f;

public class GameStateSystem extends BaseSystem {

    public GameStateSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        getGameData().getEntities(ComponentEnum.PlayerComponent2D).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            Vector2f position = positionComponent.getPosition();
            MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);
            float tileSize = WorldSettings.TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH;
            int chunkSize = WorldSettings.CHUNK_SIZE;
            int chunkX = (int) ((position.x / tileSize) / chunkSize);
            int chunkY = (int) ((position.y / tileSize) / chunkSize);
            GameState.setCurrentChunkX(chunkX);
            GameState.setCurrentChunkY(chunkY);
            TilePosition playerTilePosition = PositionUtils.getTilePosition((int) (position.x / tileSize), (int) (position.y / tileSize));
            GameState.setPlayerPosition(playerTilePosition);
            GameState.setPlayerDirection(moveComponent.getDirection());
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
