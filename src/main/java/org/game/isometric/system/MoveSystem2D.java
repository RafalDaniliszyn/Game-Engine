package org.game.isometric.system;

import org.game.GameData;
import org.game.Key;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.Camera2D;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.action.Action;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.DirectionBlocked;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.MoveComponent2D.Direction;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.utils.AnimationUtils;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.game.isometric.action.Action.Invoke.ON_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class MoveSystem2D extends BaseSystem {

    Set<Long> entitiesToProcess;

    public MoveSystem2D(GameData gameData) {
        super(gameData);
        entitiesToProcess = new HashSet<>();
        addRequiredComponent(ComponentEnum.PositionComponent2D, ComponentEnum.MoveComponent2D);
    }

    @Override
    public void update(float deltaTime) {
        move(deltaTime);
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    private void animation(Entity entity, PositionComponent2D positionComponent, MoveComponent2D moveComponent, float dt) {
        float speed = moveComponent.getSpeed();
        AnimationTimer animationTimer = moveComponent.getAnimationTimer();
        Vector2f position = new Vector2f(positionComponent.getPosition());
        Direction direction = moveComponent.getDirection();
        switch (direction) {
            case UP -> position.y += speed * dt;
            case DOWN -> position.y -= speed * dt;
            case RIGHT -> position.x += speed * dt;
            case LEFT -> position.x -= speed * dt;
        }

        positionComponent.setPosition(position);
        Camera2D.cameraPosition.x = position.x;
        Camera2D.cameraPosition.y = position.y;
        animationTimer.increment(speed * dt);
        if (animationTimer.getAnimationTimer() >= animationTimer.getDuration()) {
            animationTimer.setActive(false);
            animationTimer.setAnimationTimer(0.0f);
            float tileSize = WorldSettings.TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH;

            if (direction == Direction.UP || direction == Direction.RIGHT) {
                position.y = position.y - (position.y % tileSize);
                position.x = position.x - (position.x % tileSize);
            } else if (direction == Direction.DOWN){
                position.y = position.y + (tileSize - (position.y % tileSize));
            } else if (direction == Direction.LEFT) {
                position.x = position.x + (tileSize - (position.x % tileSize));
            }

            positionComponent.setPosition(position);
            Camera2D.cameraPosition.x = position.x;
            Camera2D.cameraPosition.y = position.y;

            int currentFloor = GameState.getCurrentFloor();
            GameData gameData = getGameData();
            Optional<Deque<Long>> entitiesOnTile = gameData.getWorldMapData().getEntitiesOnTile(currentFloor, (int) (position.x / tileSize), (int) (position.y / tileSize));
            entitiesOnTile.ifPresent(ids -> ids.forEach(id -> {
                Entity tileEntity = gameData.getEntity(id);
                if (tileEntity != null) {
                    EntityProperties properties = tileEntity.getProperties();
                    List<Action> actionList = properties.getActionList();
                    if (actionList != null) {
                        EntityProperties entityProperties = entity.getProperties();
                        for (Action action : actionList) {
                            if (ON_ENTER.equals(action.getInvoke())) {
                                entityProperties.getActionListToDo().add(action);
                            }
                        }
                    }
                }
            }));

        }
    }

    private void move(float dt) {
        GameData gameData = getGameData();
        getEntitiesToProcess().forEach(id -> {
            Entity entity = gameData.getEntity(id);
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);
            MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
            List<AnimationComponent2D> animationComponents = entity.getComponents(AnimationComponent2D.class);
            EntityProperties properties = entity.getProperties();
            Map<Side, Integer> replaceableTextureIdMap = properties.getReplaceableTextureIdMap();

            AnimationTimer animationTimer = moveComponent.getAnimationTimer();
            if (animationTimer.isActive()) {
                animation(entity, positionComponent, moveComponent, dt);
                return;
            }

            boolean isAnyAnimationActive = AnimationUtils.isAnyAnimationActive(animationComponents);
            DirectionBlocked directionBlocked = moveComponent.getDirectionBlocked();
            if (Key.isPressed(GLFW_KEY_W)) {
                if (!directionBlocked.isUpBlocked()) {
                    animationTimer.setActive(true);
                }
                if (!isAnyAnimationActive) {
                    meshComponent.setTextureID(replaceableTextureIdMap.get(Side.UP));
                }
                moveComponent.setDirection(Direction.UP);
            }
            if (Key.isPressed(GLFW_KEY_S)) {
                if (!directionBlocked.isDownBlocked()) {
                    animationTimer.setActive(true);
                }
                if (!isAnyAnimationActive) {
                    meshComponent.setTextureID(replaceableTextureIdMap.get(Side.DOWN));

                }
                moveComponent.setDirection(Direction.DOWN);
            }
            if (Key.isPressed(GLFW_KEY_A)) {
                if (!directionBlocked.isLeftBlocked()) {
                    animationTimer.setActive(true);
                }
                if (!isAnyAnimationActive) {
                    meshComponent.setTextureID(replaceableTextureIdMap.get(Side.LEFT));
                }
                moveComponent.setDirection(Direction.LEFT);
            }
            if (Key.isPressed(GLFW_KEY_D)) {
                if (!directionBlocked.isRightBlocked()) {
                    animationTimer.setActive(true);
                }
                if (!isAnyAnimationActive) {
                    meshComponent.setTextureID(replaceableTextureIdMap.get(Side.RIGHT));
                }
                moveComponent.setDirection(Direction.RIGHT);
            }

            int currentFloor = GameState.getCurrentFloor();
            if (Key.getKey() == GLFW_KEY_DOWN && Key.getAction() != 0 && currentFloor > 0) {
                GameState.setCurrentFloor(currentFloor -1);
            }
            if (Key.getKey() == GLFW_KEY_UP && Key.getAction() != 0 && currentFloor < WorldSettings.FLOORS-1) {
                GameState.setCurrentFloor(currentFloor +1);
            }
        });
    }
}
