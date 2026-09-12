package org.game.network.model;

import org.game.network.DataType;

import static org.game.isometric.component.MoveComponent2D.Direction;

public class PlayerStateModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.PLAYER_STATE_MODEL;
    private String playerName;
    private int tileX;
    private int tileY;
    private int floor;
    private String label;
    private Direction direction;

    public PlayerStateModel() {
        super(DATA_TYPE.getType());
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "PlayerStateModel{" +
                "playerName='" + playerName + '\'' +
                ", tileX=" + tileX +
                ", tileY=" + tileY +
                ", floor=" + floor +
                ", label='" + label + '\'' +
                ", direction=" + direction +
                '}';
    }
}
