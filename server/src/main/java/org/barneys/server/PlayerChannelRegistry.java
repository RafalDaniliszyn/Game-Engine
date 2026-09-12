package org.barneys.server;

import io.netty.channel.Channel;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerChannelRegistry {
    private static final ConcurrentHashMap<UUID, Channel> playerToChannel = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Channel, UUID> channelToPlayer = new ConcurrentHashMap<>();

    public static void register(UUID uuid, Channel channel) {
        playerToChannel.put(uuid, channel);
        channelToPlayer.put(channel, uuid);
    }

    public static void unregister(Channel channel) {
        UUID id = channelToPlayer.remove(channel);
        if (id != null) {
            playerToChannel.remove(id);
        }
    }

    public static Channel getChannel(UUID playerUuid) {
        return playerToChannel.get(playerUuid);
    }

    public static Optional<UUID> getPlayerUuid(Channel channel) {
        return Optional.of(channelToPlayer.get(channel));
    }
}
