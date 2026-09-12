package org.barneys;

public enum DataType {
    DEFAULT("Default"),
    SHUTDOWN_MODEL("ShutdownModel"),
    PLAYER_STATE_MODEL("PlayerStateModel"),
    GAME_STATE_MODEL("GameStateModel"),
    CHANNEL_ACTIVE_MODEL("ChannelActiveModel"),
    WORLD_MAP_MODEL("WorldMapModel"),
    USE_ITEM("UseItem"),
    INIT_MODEL("InitModel"),
    INPUT_MODEL("InputModel"),
    PUT_ITEM_MODEL("PutItemModel"),
    MOVE_ITEM_MODEL("MoveItemModel");

    private final String type;
    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
