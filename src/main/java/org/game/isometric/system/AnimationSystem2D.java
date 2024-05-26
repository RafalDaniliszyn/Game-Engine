package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.MeshComponent2D;
import org.game.system.BaseSystem;
import java.util.List;

public class AnimationSystem2D extends BaseSystem {
    public AnimationSystem2D(GameData gameData) {
        super(gameData);
        addRequiredComponent(ComponentEnum.AnimationComponent2D, ComponentEnum.MeshComponent2D);
    }

    @Override
    public void update(float deltaTime) {
        GameData gameData = getGameData();
        getEntitiesToProcess().forEach(id -> {
            Entity entity = gameData.getEntity(id);
            List<AnimationComponent2D> animationComponent = entity.getComponents(AnimationComponent2D.class);
            MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
            for (AnimationComponent2D animation : animationComponent) {
                if (animation.isActive()) {
                    int nextFrameTextureId = animation.nextFrame();
                    meshComponent.setTextureID(nextFrameTextureId);
                }
            }
        });
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
