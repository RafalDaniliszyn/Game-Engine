package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.isometric.utils.MapEditorUtils;
import org.game.key.Key;
import org.game.ui.component.SelectedItem;
import org.game.isometric.Camera2D;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.texture2D.TextureEnum2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.mouse.MouseInput;
import org.game.system.BaseSystem;

import static org.game.GraphicsDisplay.BASE_WIDTH;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.LEFT_SHIFT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.isometric.WorldSettings.TILE_SIZE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class MapEditor extends BaseSystem {
    private long lastUpdate;
    private final long delay;

    public MapEditor(GameData gameData) {
        super(gameData);
        this.delay = 50;
    }

    @Override
    public void update(float deltaTime) {
        addToTile();
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    public void addToTile() {
        double camX = Camera2D.getCameraPosition().x;
        double camY = Camera2D.getCameraPosition().y;
        if (MouseInput.LEFT_CLICK && Key.getKey() == GLFW_KEY_LEFT_SHIFT && Key.getAction() != 0) {
            long now = System.currentTimeMillis();
            long deltaTime = now - lastUpdate;
            lastUpdate = now;
            if (deltaTime < delay) {
                return;
            }
            int floor = GameState.getCurrentFloor();
            float scale = (float) WIDTH / (float) BASE_WIDTH;
            float tileSize = (TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH) * scale;

            double mouseX = (MouseInput.x - (WIDTH / 2.0f)) + (camX * scale) - (WIDTH * LEFT_SHIFT);
            double mouseY = ((HEIGHT / 2.0f) - MouseInput.y) + (camY * scale);

            int x = (int) (mouseX / tileSize);
            int y = (int) (mouseY / tileSize);

            if (SelectedItem.id == null || x <= 0 || y <= 0) {
                return;
            }

            TextureEnum2D textureEnum2D = TextureManager2D.getTextureById(SelectedItem.id);
            if (textureEnum2D == null) {
                MapEditorUtils.setEntityOnTile(floor, x, y, SelectedItem.label, true);
                return;
            }
            MapEditorUtils.setEntityOnTile(floor, x, y, SelectedItem.label, true);
        }
    }

}
