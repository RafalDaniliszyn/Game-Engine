package game.isometric.entity;

import java.util.UUID;

public class ServerEntity extends Entity {
    private UUID uuid;
    public ServerEntity(EntityProperties properties) {
        super(properties);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
