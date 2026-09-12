package org.barneys.game;

import io.netty.channel.Channel;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelRegistry {
    private final ConcurrentHashMap<UUID, Channel> playerToChannel = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Channel, UUID> channelToPlayer = new ConcurrentHashMap<>();

    public void register(UUID uuid, Channel channel) {
        playerToChannel.put(uuid, channel);
        channelToPlayer.put(channel, uuid);
    }

    public void unregister(Channel channel) {
        UUID id = channelToPlayer.remove(channel);
        if (id != null) {
            playerToChannel.remove(id);
        }
    }

    public Channel getChannel(UUID playerUuid) {
        return playerToChannel.get(playerUuid);
    }

    public Optional<UUID> getPlayerUuid(Channel channel) {
        return Optional.of(channelToPlayer.get(channel));
    }
}
