package org.game.network;

public enum DataType {
    DEFAULT("Default"),
    PLAYER_STATE_MODEL("PlayerStateModel"),
    SHUTDOWN_MODEL("ShutdownModel"),
    GAME_STATE_MODEL("GameStateModel"),
    CHANNEL_ACTIVE_MODEL("ChannelActiveModel"),
    WORLD_MAP_MODEL("WorldMapModel"),
    INIT_MODEL("InitModel"),
    INPUT_MODEL("InputModel"),
    USE_ITEM("UseItem"),
    PUT_ITEM_MODEL("PutItemModel"),

    PLAYER_FUTURE_STATE("PlayerFutureState");

    private final String type;
    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
