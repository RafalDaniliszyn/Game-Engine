package org.barneys.model;

import org.barneys.DataType;
import org.barneys.Input;
import org.barneys.State;
import java.util.UUID;

public class GameStateModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.GAME_STATE_MODEL;
    private int tileX;
    private int tileY;
    private int floor;
    private State state;
    private String label;
    private Direction direction;
    private Input input;

    public GameStateModel() {
        super(DATA_TYPE.getType());
    }

    public GameStateModel(int tileX, int tileY, int floor, Direction direction, State state, UUID userUuid, String label, Input input) {
        super(userUuid, DATA_TYPE.getType());
        this.tileX = tileX;
        this.tileY = tileY;
        this.floor = floor;
        this.direction = direction;
        this.state = state;
        this.label = label;
        this.input = input;
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "GameStateModel{" +
                "tileX=" + tileX +
                ", tileY=" + tileY +
                ", floor=" + floor +
                ", state=" + state +
                ", label='" + label + '\'' +
                ", direction=" + direction +
                ", input=" + input +
                "} " + super.toString();
    }
}
