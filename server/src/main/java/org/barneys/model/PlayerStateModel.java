package org.barneys.model;

import org.barneys.DataType;
import java.util.UUID;



public class PlayerStateModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.PLAYER_STATE_MODEL;
    private String playerName;
    private int tileX;
    private int tileY;
    private int floor;
    private String label;
    private Direction direction;

    public PlayerStateModel(UUID userUuid) {
        super(userUuid, DATA_TYPE.getType());
    }

    public PlayerStateModel(UUID userUuid, String playerName, int tileX, int tileY, int floor, String label, Direction direction) {
        super(userUuid, DATA_TYPE.getType());
        this.playerName = playerName;
        this.tileX = tileX;
        this.tileY = tileY;
        this.floor = floor;
        this.label = label;
        this.direction = direction;
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
                "} " + super.toString();
    }
}
