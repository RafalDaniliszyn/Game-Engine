package org.game.isometric.system;

import org.game.entity.Entity;
import org.game.isometric.GameState;
import org.game.isometric.event.MouseClickEvent;
import org.game.isometric.event.MouseClickEventHandler;
import org.game.isometric.utils.TilePosition;
import org.lwjgl.glfw.GLFW;

import static org.game.GameData.gameData;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.GraphicsDisplay.getDisplayId;
import static org.game.isometric.WorldSettings.TILE_SIZE;
import static org.game.isometric.utils.PositionUtils.AbsoluteTilePosition;
import static org.game.isometric.utils.PositionUtils.getAbsoluteTilePosition;


public class GameObjectClickInspector implements MouseClickEventHandler {

    @Override
    public void handleEvent(MouseClickEvent event) {
        TilePosition playerPosition = GameState.getPlayerPosition();
        AbsoluteTilePosition absTilePos = getAbsoluteTilePosition(playerPosition);

        int x = absTilePos.x();
        int y = absTilePos.y();

        int[] width = new int[1];
        int[] height = new int[1];
        GLFW.glfwGetWindowSize(getDisplayId(), width, height);
        double mouseX = event.getX();
        double mouseY = height[0] - event.getY();
        double tilesHorizontally = WIDTH / TILE_SIZE;
        double tilesVertically = HEIGHT / TILE_SIZE;
        double mouseTileX = mouseX / TILE_SIZE;
        double mouseTileY = mouseY / TILE_SIZE;
        int mouseAbsTileX = (int) (mouseTileX + (x - (tilesHorizontally / 2)));
        int mouseAbsTileY = (int) (mouseTileY + (y - (tilesVertically / 2)));

        Long entityId = gameData.getWorldMapData().getTopEntityIdFromTile(event.getFloor(), mouseAbsTileX, mouseAbsTileY);
        Entity entity = gameData.getEntity(entityId);
        if (entity == null ) {
            return;
        }
        String label = entity.getProperties().getLabel();
        System.out.println("id: " + entityId);
        System.out.println("width " + width[0]);
        System.out.println("height " + height[0]);
        System.out.println("YOU SEE: " + label + " X: " + mouseAbsTileX + " Y: " + mouseAbsTileY);
        System.out.println("MOUSE POS: " + " X: " + mouseX + " Y: " + mouseY);
    }
}
