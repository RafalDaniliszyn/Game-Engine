package org.game.isometric.event;

public class LoadTextureEvent extends Event {
    private final String path;
    private final String entityLabel;
    private final Long rotation;


    public LoadTextureEvent(String path, String entityLabel, Long rotation) {
        this.path = path;
        this.entityLabel = entityLabel;
        this.rotation = rotation;
    }

    public String getPath() {
        return path;
    }

    public String getEntityLabel() {
        return entityLabel;
    }

    public Long getRotation() {
        return rotation;
    }
}
