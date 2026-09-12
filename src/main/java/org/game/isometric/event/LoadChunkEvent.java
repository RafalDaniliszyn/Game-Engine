package org.game.isometric.event;

import org.game.network.model.WorldMapModel;

public class LoadChunkEvent extends Event {
    private final WorldMapModel worldMapModel;

    public LoadChunkEvent(WorldMapModel worldMapModel) {
        this.worldMapModel = worldMapModel;
    }

    public WorldMapModel getWorldMapModel() {
        return worldMapModel;
    }
}
