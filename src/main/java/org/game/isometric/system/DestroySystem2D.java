package org.game.isometric.system;

import org.game.GameData;
import org.game.Key;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.DestroyComponent2D;
import org.game.isometric.component.DestroyableComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.utils.EntityUtils;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.List;
import java.util.Map;

import static org.game.entity.Entity.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class DestroySystem2D extends BaseSystem {

    private final EdgeReplacer edgeReplacer;
    public DestroySystem2D(GameData gameData) {
        super(gameData);
        this.edgeReplacer = new EdgeReplacer(gameData, gameData.getWorldMapData());
    }

    @Override
    public void update(float deltaTime) {
        getDestroyInput();
        updateEntitiesToDestroy();
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    public void getDestroyInput() {
        if (Key.isPressed(GLFW_KEY_SPACE)) {
            int floor = GameState.getCurrentFloor();

            TilePosition playerPosition = GameState.getPlayerPosition();
            PositionUtils.AbsoluteTilePosition absoluteTilePosition = PositionUtils.getAbsoluteTilePosition(playerPosition);
            int x = absoluteTilePosition.x();
            int y = absoluteTilePosition.y();

            MoveComponent2D.Direction playerDirection = GameState.getPlayerDirection();
            switch (playerDirection) {
                case LEFT -> x -= 1;
                case RIGHT -> x += 1;
                case UP -> y += 1;
                case DOWN -> y -=1;
            }

            GameData gameData = getGameData();
            Long bottomEntityIdFromTile = gameData.getWorldMapData().getBottomEntityIdFromTile(floor, x, y);
            Entity entity = gameData.getEntity(bottomEntityIdFromTile);
            if (entity == null) {
                return;
            }

            DestroyableComponent2D destroyableComponent = entity.getComponent(DestroyableComponent2D.class);
            if (destroyableComponent != null && !EntityUtils.containsComponents(entity, ComponentEnum.DestroyComponent2D)) {
                entity.addComponent(new DestroyComponent2D(destroyableComponent.getDestructionDifficulty(), false));
            }

            Map<Long, Entity> playerEntityMap = gameData.getEntities(ComponentEnum.PlayerComponent2D);
            playerEntityMap.forEach((id, player) -> {
                List<AnimationComponent2D> animationComponent = player.getComponents(AnimationComponent2D.class);
                for (AnimationComponent2D animation : animationComponent) {
                    if (animation.getLabel().equals(playerDirection.name())) {
                        animation.setActive(true);
                    }
                }

            });
        }
    }

    private void updateEntitiesToDestroy() {
        GameData gameData = getGameData();
        gameData.getEntities(ComponentEnum.DestroyableComponent2D, ComponentEnum.DestroyComponent2D, ComponentEnum.MeshComponent2D, ComponentEnum.PositionComponent2D).forEach((id, entity) -> {
            DestroyComponent2D destroyComponent = entity.getComponent(DestroyComponent2D.class);
            if (destroyComponent.isDestroyNow()) {
                destroy(gameData, entity);
            }
            if (!Key.isPressed(GLFW_KEY_SPACE) || State.DESTROYED.equals(entity.getState())) {
                entity.removeComponent(DestroyComponent2D.class);
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (destroyComponent.getLastUpdateTime() == 0) {
                destroyComponent.setLastUpdateTime(currentTime);
            }
            double elapsed = currentTime - destroyComponent.getLastUpdateTime();
            destroyComponent.setLastUpdateTime(currentTime);
            destroyComponent.increaseElapsedTime(elapsed);

            if (destroyComponent.getElapsedTime() < destroyComponent.getDestructionDelay()) {
                return;
            }

            destroy(gameData, entity);
        });
    }

    private void destroy(GameData gameData, Entity entity) {
        MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
        DestroyableComponent2D destroyableComponent = entity.getComponent(DestroyableComponent2D.class);
        meshComponent.setTextureID(destroyableComponent.getAfterDestroyTextureId());
        entity.removeComponent(DestroyComponent2D.class);
        this.afterDestroySettings(entity, destroyableComponent);

        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        Vector2f position = positionComponent.getPosition();
        float tileSize = WorldSettings.TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH;

        List<Entity> entityListToAdd = destroyableComponent.afterDestroy();
        int currentFloor = GameState.getCurrentFloor();
        entityListToAdd.forEach(newEntity -> {
            PositionComponent2D newPositionComponent = newEntity.getComponent(PositionComponent2D.class);
            newPositionComponent.setPosition(new Vector2f(position.x, position.y));
            newPositionComponent.setFloor(positionComponent.getFloor());

            gameData.addEntity(newEntity);
            gameData.getWorldMapData().addEntityToTile(
                    currentFloor, (int) (position.x / tileSize),
                    (int) (position.y / tileSize), newEntity.getId(),
                    false);
        });
        edgeReplacer.replaceEdges(currentFloor, (int) (position.x / tileSize),  (int) (position.y / tileSize));
    }

    private void afterDestroySettings(Entity entity, DestroyableComponent2D destroyableComponent) {
        entity.setState(State.DESTROYED);
        EntityProperties properties = entity.getProperties();
        properties.setLabel(destroyableComponent.getAfterDestroyLabel());
        properties.setCollidable(false);
        properties.setReplaceableEdges(false);
        properties.setDepth(-2.0f);
    }
}
