package org.game.ui.entity;

import org.game.GraphicsDisplay;
import org.game.entity.EntityProperties;
import org.game.system.shader.ShaderEnum;
import org.game.ui.component.RawUiModel;
import org.game.ui.component.UiComponent;
import org.game.entity.Entity;
import org.game.event.EquipmentEvent;
import org.game.event.EquipmentEventManager;
import org.game.event.EventEnum;
import org.game.event.EventManager;

public class UiEntity extends Entity {

    private final EquipmentEventManager eventManager;
    private final RawUiModel rawUiModel;

    public UiEntity(long itemId, RawUiModel rawUiModel) {
        super(new EntityProperties(ShaderEnum.UI));
        this.rawUiModel = rawUiModel;
        this.eventManager = new EquipmentEventManager();

        double width = rawUiModel.getScale().x * GraphicsDisplay.WIDTH;
        double height = rawUiModel.getScale().y * GraphicsDisplay.HEIGHT;
        UiComponent box = new UiComponent(
                rawUiModel.getPosition().x,
                rawUiModel.getPosition().y,
                width, height, new EquipmentEvent(EventEnum.USE, itemId), eventManager, rawUiModel);
        addComponent(box);
    }

    public RawUiModel getRawUiModel() {
        return rawUiModel;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
