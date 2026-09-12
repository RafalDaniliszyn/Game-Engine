package org.game.isometric.system.destroySystem;

import org.game.GameData;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.ComponentSource;
import org.game.isometric.component.DestroyComponent2D;
import org.game.isometric.component.DestroyableComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.system.SwapEdgeHelper;
import org.game.isometric.utils.TileUtils;
import org.game.key.Key;
import org.game.entity.Entity;
import org.game.isometric.GameState;
import org.game.isometric.utils.EntityUtils;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.network.client.DataSync;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.game.entity.Entity.State;
import static org.game.isometric.component.ComponentSource.CLIENT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class DestroySystem2D extends BaseSystem {

    public DestroySystem2D(GameData gameData) {
        super(gameData);
        addRequiredComponent(ComponentEnum.DestroyableComponent2D, ComponentEnum.DestroyComponent2D, ComponentEnum.MeshComponent2D);
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
                entity.addComponent(new DestroyComponent2D(destroyableComponent.getDestructionDifficulty(), false, x, y, floor));
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
        getEntitiesToProcess().forEach(id -> {
            Entity entity = gameData.getEntity(id);
            DestroyComponent2D destroyComponent = entity.getComponent(DestroyComponent2D.class);
            if (destroyComponent != null && destroyComponent.isDestroyNow()) {
                destroy(gameData, entity, destroyComponent.getSource());
            }
            if (!Key.isPressed(GLFW_KEY_SPACE) || State.DESTROYED.equals(entity.getState())) {
                entity.removeComponent(DestroyComponent2D.class);
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (destroyComponent != null) {
                if (destroyComponent.getLastUpdateTime() == 0) {
                    destroyComponent.setLastUpdateTime(currentTime);
                }
                double elapsed = currentTime - destroyComponent.getLastUpdateTime();
                destroyComponent.setLastUpdateTime(currentTime);
                destroyComponent.increaseElapsedTime(elapsed);

                if (destroyComponent.getElapsedTime() < destroyComponent.getDestructionDelay()) {
                    return;
                }
                destroy(gameData, entity, destroyComponent.getSource());
            }
        });
    }

    private void destroy(GameData gameData, Entity entity, ComponentSource source) {
        List<DestroyComponent2D> destroyComponents = entity.getComponents(DestroyComponent2D.class);

        destroyComponents.forEach(destroyComponent ->  {
            Optional<Deque<Long>> entitiesOnTileOptional = gameData.getWorldMapData().getEntitiesOnTile(destroyComponent.getFloor(), destroyComponent.getX(), destroyComponent.getY());
            if (entitiesOnTileOptional.isPresent()) {
                Deque<Long> idQueue = entitiesOnTileOptional.get();
                Long entityIdByState = entity.getEntityIdByState(State.DESTROYED);
                if (entityIdByState != null) {
                    TileUtils.replaceEntityOnBottom(entityIdByState, idQueue);
                    //edgeReplacer.replaceEdges(destroyComponent.getFloor(), destroyComponent.getX(), destroyComponent.getY());
                    SwapEdgeHelper.changeAround(destroyComponent.getFloor(), destroyComponent.getX(), destroyComponent.getY());
                }
                entity.removeComponents(DestroyComponent2D.class);
            }

            if (CLIENT.equals(source)) {
                Vector2f screenPosition = PositionUtils.getScreenPosition(new PositionUtils.AbsoluteTilePosition(destroyComponent.getX(), destroyComponent.getY()));
                //DataSync.sendDestroy(screenPosition, beforeDestroyLabel);
                DataSync.sendDestroy(screenPosition);
                //If the Component originates from the CLIENT, then the loot is provided by the SERVER
            }
        });
        entity.removeComponents(DestroyComponent2D.class);
    }
}
