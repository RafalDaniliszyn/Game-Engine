package org.game.system;

import org.game.GameData;
import org.game.mouse.MouseInput;
import org.game.ui.component.UiComponent;

public class UiSystem extends BaseSystem {
    public UiSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        getGameData().getEntities(UiComponent.class).forEach((aLong, entity) -> {
            UiComponent component = entity.getComponent(UiComponent.class);
            component.update(MouseInput.x, MouseInput.y);
        });

    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
