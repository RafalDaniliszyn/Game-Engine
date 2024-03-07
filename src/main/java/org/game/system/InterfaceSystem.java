package org.game.system;

import org.game.GameData;
import org.game.mouse.MouseInput;
import org.game.component.ButtonComponent;

public class InterfaceSystem extends BaseSystem {
    public InterfaceSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        getGameData().getEntities(ButtonComponent.class).forEach((aLong, entity) -> {
            ButtonComponent button = entity.getComponent(ButtonComponent.class);
            button.getClickable().update(MouseInput.x, MouseInput.y);
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
