package org.game.isometric.worldMap;

import org.game.GameData;
import org.game.GraphicsDisplay;
import org.game.Key;
import org.game.debugWindow.SelectedItem;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.Camera2D;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.EntityMapper;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.entity.ItemEntity2D;
import org.game.isometric.entity.TerrainEntity2D;
import org.game.isometric.system.EdgeReplacer;
import org.game.isometric.texture2D.TextureEnum2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.mouse.MouseInput;
import org.game.system.BaseSystem;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class MapEditor extends BaseSystem {

    private GameData gameData;
    private WorldMapData worldMapData;
    private EdgeReplacer edgeReplacer;

    public MapEditor(GameData gameData, WorldMapData worldMapData) {
        super(gameData);
        this.gameData = gameData;
        this.worldMapData = worldMapData;
        this.edgeReplacer = new EdgeReplacer(gameData, worldMapData);
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
        if (MouseInput.LEFT_CLICK && Key.key == GLFW_KEY_LEFT_SHIFT && Key.action != 0) {
            int floor = GameState.getCurrentFloor();
            float tileSize = WorldSettings.TILE_SIZE;
            double mouseX = (MouseInput.x - (GraphicsDisplay.WIDTH / 2.0f)) + camX;
            double mouseY = ((GraphicsDisplay.HEIGHT / 2.0f) - MouseInput.y) + camY;

            int x = (int) ((int) mouseX / tileSize);
            int y = (int) ((int) mouseY / tileSize);

            if (SelectedItem.id == null) {
                return;
            }

            TextureEnum2D textureEnum2D = TextureManager2D.getTextureById(SelectedItem.id);
            if (textureEnum2D == null) {
                Entity entityBase = BlocksReader.getEntity(SelectedItem.label);
                Entity entity = EntityMapper.getNewEntity(entityBase);
                if (entity == null) {
                    return;
                }
                entity.addComponent(new PositionComponent2D(new Vector2f(x * tileSize,y * tileSize), floor));
                entity.addComponent(new MeshComponent2D(entityBase.getComponent(MeshComponent2D.class).getTextureID()));
                gameData.addEntity(entity);
                boolean isTerrain = "terrain".equals(entity.getProperties().getType());
                worldMapData.addEntityToTile(floor, x, y, entity.getId(), isTerrain);
                if (isTerrain) {
                    edgeReplacer.replaceEdges(floor, x, y);
                }
                return;
            }
            if (textureEnum2D.getQuantity() == null) {
                TerrainEntity2D terrainEntity = new TerrainEntity2D(SelectedItem.id, new Vector2f(x * tileSize, y * tileSize), floor,
                        new EntityProperties.EntityPropertiesBuilder()
                                .setCollidable(false)
                                .setDraggable(false)
                                .setLabel(SelectedItem.label)
                                .setQuantity(null)
                                .setStackable(false)
                                .setDepth(-2.0f)
                                .build());
                gameData.addEntity(terrainEntity);
                worldMapData.addEntityToTile(floor, x, y, terrainEntity.getId(), true);
                edgeReplacer.replaceEdges(floor, x, y);
            } else if (SelectedItem.label != null){
                ItemEntity2D itemEntity2D =
                        new ItemEntity2D(new Vector2f(x * tileSize,y * tileSize), floor, SelectedItem.label, SelectedItem.id, true);

                gameData.addEntity(itemEntity2D);
                worldMapData.addEntityToTile(floor, x, y, itemEntity2D.getId(), false);
            }
        }
    }
}
