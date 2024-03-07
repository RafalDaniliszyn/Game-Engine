package org.game.system;

import org.game.GameData;
import org.game.component.mesh.MeshComponent;
import org.game.component.PositionComponent;
import org.game.component.WindComponent;

public class WindSystem extends BaseSystem {

    public WindSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        move(deltaTime);
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    private void move(float dt) {
        getGameData().getEntities(WindComponent.class, PositionComponent.class, MeshComponent.class).forEach((id, entity) -> {
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            WindComponent windComponent = entity.getComponent(WindComponent.class);
            float lastWindSin = windComponent.getLastWindSin();
            float rotationX = (float) Math.sin(positionComponent.getPosition().x + lastWindSin);
            if (windComponent.isForwardDirection()) {
                windComponent.setLastWindSin(lastWindSin+0.1f);
            } else {
                windComponent.setLastWindSin(lastWindSin-0.1f);
            }

            if (lastWindSin + 0.1f >= 1.1f) {
                windComponent.setForwardDirection(false);
            }
            if (lastWindSin -0.1f <= -1.0f) {
                windComponent.setForwardDirection(true);
            }
            entity.getComponent(PositionComponent.class).setRotationX(rotationX);
        });
    }
}
