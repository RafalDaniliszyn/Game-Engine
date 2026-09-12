package org.barneys.model;

public class EntityIdLabelDto {
    private String label;
    private long id;

    public EntityIdLabelDto() {
    }

    public EntityIdLabelDto(String label, long id) {
        this.label = label;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public long getId() {
        return id;
    }
}
