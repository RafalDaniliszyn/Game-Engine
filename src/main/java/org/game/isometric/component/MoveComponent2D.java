package org.game.isometric.component;

import org.game.component.Component;
import org.game.isometric.WorldSettings;
import org.game.isometric.system.AnimationTimer;

public class MoveComponent2D extends Component {
    private float speed;
    private Direction direction;
    private boolean blocked;
    private AnimationTimer animationTimer;
    private final DirectionBlocked directionBlocked;

    public MoveComponent2D(float speed) {
        this.speed = speed;
        this.direction = Direction.UP;
        this.animationTimer = new AnimationTimer(WorldSettings.TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH);
        this.directionBlocked = new DirectionBlocked();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public AnimationTimer getAnimationTimer() {
        return animationTimer;
    }

    public void setAnimationTimer(AnimationTimer animationTimer) {
        this.animationTimer = animationTimer;
    }

    public DirectionBlocked getDirectionBlocked() {
        return directionBlocked;
    }

    public void setDirectionBlocked(Direction direction, boolean isBlocked) {
        switch (direction) {
            case LEFT -> directionBlocked.setLeftBlocked(isBlocked);
            case RIGHT -> directionBlocked.setRightBlocked(isBlocked);
            case DOWN -> directionBlocked.setDownBlocked(isBlocked);
            case UP -> directionBlocked.setUpBlocked(isBlocked);
        }
    }

    @Override
    public ComponentEnum getType() {
        return ComponentEnum.MoveComponent2D;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN;
    }
}
