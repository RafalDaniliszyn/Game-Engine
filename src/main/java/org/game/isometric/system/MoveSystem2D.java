package org.game.isometric.system;

import org.game.GameData;
import org.game.Key;
import org.game.isometric.Camera2D;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.DirectionBlocked;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.MoveComponent2D.Direction;
import org.game.isometric.component.PositionComponent2D;
import org.game.system.BaseSystem;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class MoveSystem2D extends BaseSystem {

    public MoveSystem2D(GameData gameData) {
        super(gameData);
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

    private void animation(PositionComponent2D positionComponent, MoveComponent2D moveComponent, float dt) {
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

            if (direction == Direction.UP || direction == Direction.RIGHT) {
                position.y = position.y - (position.y % WorldSettings.TILE_SIZE);
                position.x = position.x - (position.x % WorldSettings.TILE_SIZE);
            } else if (direction == Direction.DOWN){
                position.y = position.y + (WorldSettings.TILE_SIZE - (position.y % WorldSettings.TILE_SIZE));
            } else if (direction == Direction.LEFT) {
                position.x = position.x + (WorldSettings.TILE_SIZE - (position.x % WorldSettings.TILE_SIZE));
            }

            positionComponent.setPosition(position);
            Camera2D.cameraPosition.x = position.x;
            Camera2D.cameraPosition.y = position.y;
        }
    }

    private void move(float dt) {
        getGameData().getEntities(PositionComponent2D.class, MoveComponent2D.class).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);

            AnimationTimer animationTimer = moveComponent.getAnimationTimer();
            if (animationTimer.isActive()) {
                animation(positionComponent, moveComponent, dt);
                return;
            }

            DirectionBlocked directionBlocked = moveComponent.getDirectionBlocked();
            if (Key.key == GLFW_KEY_W && Key.action != 0 && !directionBlocked.isUpBlocked()) {
                animationTimer.setActive(true);
                moveComponent.setDirection(Direction.UP);
            }
            if (Key.key == GLFW_KEY_S && Key.action != 0 && !directionBlocked.isDownBlocked()) {
                animationTimer.setActive(true);
                moveComponent.setDirection(Direction.DOWN);
            }
            if (Key.key == GLFW_KEY_A && Key.action != 0 && !directionBlocked.isLeftBlocked()) {
                animationTimer.setActive(true);
                moveComponent.setDirection(Direction.LEFT);
            }
            if (Key.key == GLFW_KEY_D && Key.action != 0 && !directionBlocked.isRightBlocked()) {
                animationTimer.setActive(true);
                moveComponent.setDirection(Direction.RIGHT);
            }
            if (Key.key == GLFW_KEY_DOWN && Key.action != 0 && GameState.getCurrentFloor() > 0) {
                GameState.setCurrentFloor(GameState.getCurrentFloor()-1);
            }
            if (Key.key == GLFW_KEY_UP && Key.action != 0 && GameState.getCurrentFloor() < WorldSettings.FLOORS-1) {
                GameState.setCurrentFloor(GameState.getCurrentFloor()+1);
            }
        });

    }
}
