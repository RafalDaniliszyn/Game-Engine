package org.game.event;

public class EquipmentEvent extends Event {

    private final long itemId;

    public EquipmentEvent(EventEnum eventEnum, long itemId) {
        super(eventEnum);
        this.itemId = itemId;
    }

    public long getItemId() {
        return itemId;
    }
}
