package org.game.isometric.system;

import org.game.GameData;
import org.game.GraphicsDisplay;
import org.game.Key;
import org.game.editWindow.Panel;
import org.game.entity.Entity;
import org.game.isometric.Camera2D;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.DragComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.worldMap.WorldMapData;
import org.game.mouse.MouseInput;
import org.game.mouse.MouseState;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.Deque;
import java.util.Optional;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class DragSystem extends BaseSystem {
    private Entity activeEntity;
    private WorldMapData worldMapData;
    private int initialXonTile;
    private int initialYonTile;

    public DragSystem(GameData gameData) {
        super(gameData);
        this.activeEntity = null;
    }

    @Override
    public void update(float deltaTime) {
        drag();
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {
        this.worldMapData = getGameData().getWorldMapData();
    }

    private void drag() {
        double camX = Camera2D.getCameraPosition().x;
        double camY = Camera2D.getCameraPosition().y;
        float tileSize = WorldSettings.getTileSize();
        float tileSizeHalf = tileSize / 2.0f;
        if (MouseInput.RELEASE && activeEntity != null) {
            PositionComponent2D positionComponent = activeEntity.getComponent(PositionComponent2D.class);
            Vector2f position = new Vector2f(positionComponent.getPosition());
            double offsetX = position.x % tileSize;
            double offsetY = position.y % tileSize;
            if (offsetX > tileSizeHalf) {
                offsetX = -(tileSize - offsetX);
            }
            if (offsetY > tileSizeHalf) {
                offsetY = -(tileSize - offsetY);
            }
            position.x = (float) (position.x - offsetX);
            position.y = (float) (position.y - offsetY);
            int x = (int) (position.x / tileSize);
            int y = (int) (position.y / tileSize);

            int floor = GameState.getCurrentFloor();

            worldMapData.removeEntityFromTile(floor, initialXonTile, initialYonTile, activeEntity.getId());
            worldMapData.addEntityToTile(floor, x, y, activeEntity.getId(), false);
            positionComponent.setPosition(position);
            MouseState.drag.setInUse(false);
            activeEntity = null;
            return;
        }
        if (this.activeEntity != null) {
            PositionComponent2D positionComponent = activeEntity.getComponent(PositionComponent2D.class);
            Vector2f position = new Vector2f(positionComponent.getPosition());
            position.x = (float) ((MouseInput.x - (GraphicsDisplay.WIDTH / 2.0f)) + camX) - tileSizeHalf;
            position.y = (float) ((GraphicsDisplay.HEIGHT / 2.0f - MouseInput.y) + camY) - (tileSizeHalf + 25.0f);
            positionComponent.setPosition(position);
            return;
        }

        if (MouseInput.LEFT_CLICK && (Key.getKey() != GLFW_KEY_LEFT_SHIFT || Key.getAction() == 0)) {
            int floor = GameState.getCurrentFloor();
            double mouseX = (MouseInput.x - (GraphicsDisplay.WIDTH / 2.0f)) + camX;
            double mouseY = ((GraphicsDisplay.HEIGHT / 2.0f) - MouseInput.y) + camY;

            //
            Optional<Deque<Long>> entitiesOnTile = worldMapData.getEntitiesOnTile(floor, (int) (mouseX / tileSize), (int) (mouseY / tileSize));
            if (entitiesOnTile.isPresent()) {
                Panel.clearDescription();
                entitiesOnTile.get().forEach(id -> {
                    Entity entity = getGameData().getEntity(id);
                    if (entity != null) {
                        Panel.addDescription("entity id: " + entity.getId(), "label: " + entity.getProperties().getLabel(), "quantity: " + entity.getProperties().getQuantity());
                    }
                });
            }
            //

            Long firstEntityIdFromTile = worldMapData.getTopEntityIdFromTile(floor, (int) (mouseX / tileSize), (int) (mouseY / tileSize));
            if (firstEntityIdFromTile != null) {
                Entity entity = getGameData().getEntity(firstEntityIdFromTile);
                if (entity != null) {
                    activeEntity = entity;
                }
                if (activeEntity != null && activeEntity.getComponent(DragComponent2D.class) == null) {
                    activeEntity = null;
                    return;
                }
                if (activeEntity == null) {
                    return;
                }
                Vector2f position = activeEntity.getComponent(PositionComponent2D.class).getPosition();
                initialXonTile = (int) ((int) position.x / tileSize);
                initialYonTile = (int) ((int) position.y / tileSize);
                MouseState.drag.setInUse(true);
            }
        }
    }
}
