package org.barneys;

import game.isometric.entity.Entity;
import io.netty.channel.ChannelId;
import org.barneys.blockLoader.BlocksReader;
import org.barneys.blockLoader.EntityDto;
import org.barneys.model.ChannelActiveModel;
import org.barneys.worldMap.WorldMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WorldState {
    public static final Map<UUID, User> users;
    public static final Map<String, EntityDto> entityDtoMap;
    public static final Map<String, Entity> entityMap;
    public static final Map<Long, Entity> entityMapById;
    public static Map<ChannelId, Optional<UUID>> channelIdByUserUuidMap;
    public static final ChannelActiveModel channelActiveModel;
    private static final WorldMap worldMap;

    static {
        users = new HashMap<>();
        entityDtoMap = new HashMap<>();
        entityMap = new HashMap<>();
        channelActiveModel = BlocksReader.readBlocks();
        entityMapById = new HashMap<>();
        entityMap.forEach((label, entity) -> {
            entityMapById.put(entity.getId(), entity);
        });
        worldMap = new WorldMap();
        channelIdByUserUuidMap = new HashMap<>();
    }

    public static void updateEntityMapById() {
        entityMap.forEach((label, entity) -> {
            if (!entityMapById.containsKey(entity.getId())) {
                entityMapById.put(entity.getId(), entity);
            }
        });
    }

    public static User getByUuid(UUID userUuid) {
        return users.get(userUuid);
    }

    public static WorldMap getWorldMap() {
        return worldMap;
    }

    public static void registerUser(ChannelId channelId, UUID userUuid) {
        channelIdByUserUuidMap.putIfAbsent(channelId, Optional.of(userUuid));
    }
}
