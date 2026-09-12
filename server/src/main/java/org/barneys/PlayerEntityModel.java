package org.barneys;

import game.isometric.helper.IdGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
@Table(name = "player_entity")
public class PlayerEntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @OneToOne(mappedBy = "playerEntityModel")
    private User user;

    private String name;

    @Column(name = "position_x")
    private Integer positionX;

    @Column(name = "position_y")
    private Integer positionY;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "texture_label")
    private String textureLabel;

    @Transient
    private long movementSpeed;

    @Transient
    private int lastFloor;

    @Transient
    private boolean floorChanged;

    @Transient
    private long sessionEntityId;

    public PlayerEntityModel() {
        // TODO: 8/9/2024 get speed from db
        this.movementSpeed = 250;
        this.floor = 0;
        this.lastFloor = -1; //negative only when client need first chunk
        this.sessionEntityId = IdGenerator.getNextId();
    }

    public PlayerEntityModel(UUID uuid, User user, String name, Integer positionX, Integer positionY, Integer floor, String textureLabel) {
        this.uuid = uuid;
        this.user = user;
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.floor = floor;
        this.textureLabel = textureLabel;
        movementSpeed = 10;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        if (!this.floor.equals(floor)) {
            this.floorChanged = true;
        }
        this.floor = floor;
    }

    public String getTextureLabel() {
        return textureLabel;
    }

    public void setTextureLabel(String textureLabel) {
        this.textureLabel = textureLabel;
    }

    public long getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(long movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public int getLastFloor() {
        return lastFloor;
    }

    public void setLastFloor(int lastFloor) {
        this.lastFloor = lastFloor;
    }

    public boolean isFloorChanged() {
        if (floorChanged) {
            floorChanged = false;
            return true;
        } else {
            return false;
        }
    }

    public void setFloorChanged(boolean floorChanged) {
        this.floorChanged = floorChanged;
    }

    public long getSessionEntityId() {
        return sessionEntityId;
    }

    @Override
    public String toString() {
        return "PlayerEntityModel{" +
                "id=" + uuid +
                ", name='" + name + '\'' +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", textureLabel='" + textureLabel + '\'' +
                ", movementSpeed='" + movementSpeed + '\'' +
                ", lastFloor='" + lastFloor + '\'' +
                '}';
    }
}
