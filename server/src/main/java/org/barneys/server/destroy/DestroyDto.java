package org.barneys.server.destroy;

import org.barneys.server.modelHandler.DestroyModel;
import org.barneys.server.modelHandler.DropModel;
import org.barneys.server.modelHandler.PutModel;

import java.util.*;

public class DestroyDto {
    private final UUID userUuid;
    private int tileX;
    private int tileY;
    private int floor;
    private String label;
    private String item;
    private int range;
    private long duration;

    private final List<DestroyModel> destroyed;
    private final List<DestroyedEntityDto> destroyedEntityDtoList;
    private final List<DropModel> drop;

    public DestroyDto(UUID userUuid, int tileX, int tileY, int floor, String label, String item, long duration) {
        this.userUuid = userUuid;
        this.tileX = tileX;
        this.tileY = tileY;
        this.floor = floor;
        this.label = label;
        this.item = item;
        this.duration = duration;
        this.destroyed = new ArrayList<>();
        this.drop = new ArrayList<>();
        this.destroyedEntityDtoList = new ArrayList<>();
    }

    public UUID getUserUuid() {
        return userUuid;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public List<DestroyModel> getDestroyed() {
        return destroyed;
    }

    public void addDestroyed(DestroyModel destroyed) {
        this.destroyed.add(destroyed);
    }

    public List<DropModel> getDrop() {
        return drop;
    }

    public void addDropItem(int tileX, int tileY, int floor, String label) {
        drop.add(new DropModel(tileX, tileY, floor, label));
    }

    public List<DestroyedEntityDto> getDestroyedEntityDtoList() {
        return destroyedEntityDtoList;
    }

    public void addDestroyedEntity(DestroyedEntityDto destroyedEntityDto) {
        destroyedEntityDtoList.add(destroyedEntityDto);
    }
}
