package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.action.Action;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.utils.PositionUtils;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TileActionSystem extends BaseSystem {
    public TileActionSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        GameData gameData = getGameData();
        Map<Long, Entity> entitiesWithPredicate = gameData.getEntitiesWithPredicate(entity -> {
            EntityProperties properties = entity.getProperties();
            List<Action> actionList = properties.getActionListToDo();
            return actionList != null && actionList.size() > 0;
        });
        for (Entity entity : entitiesWithPredicate.values()) {
            AnimationComponent2D animationComponent = entity.getComponent(AnimationComponent2D.class);
            if (animationComponent != null && animationComponent.isActive()) {
                continue;
            }
            EntityProperties properties = entity.getProperties();
            List<Action> actionList = properties.getActionListToDo();

            boolean remove = false;
            for (Iterator<Action> action = actionList.iterator(); action.hasNext(); ) {
                Action nextAction = action.next();
                nextAction.processAction(entity);
                nextAction.processActions(entity, gameData);
                if (nextAction.isRemoveEntityAfter()) {
                    remove = true;
                }
                if (nextAction.isRemoveActionAfter()) {
                    action.remove();
                }
            }
            if (remove) {
                PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
                if (positionComponent == null) {
                    return;
                }
                Vector2f position = positionComponent.getPosition();
                PositionUtils.AbsoluteTilePosition absoluteTilePosition = PositionUtils.getAbsoluteTilePositionFromWorldSpace(position);
                gameData.getWorldMapData().removeEntityFromTile(positionComponent.getFloor(), absoluteTilePosition.x(), absoluteTilePosition.y(), entity.getId());
                gameData.removeEntity(entity.getId());
            }
        }
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }
}
