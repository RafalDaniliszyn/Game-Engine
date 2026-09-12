package org.game.isometric.utils;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.action.Action;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.EntityMapper;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.system.SwapEdgeHelper;
import org.game.isometric.worldMap.WorldMapData;
import org.game.network.client.DataSync;
import org.game.network.client.incomingDataHandler.ItemSpawnMessage;
import org.joml.Vector2f;
import java.util.Deque;
import java.util.List;

import static org.game.isometric.WorldSettings.TILE_SIZE;
import static org.game.isometric.action.Action.Invoke.ON_PUT;

public class MapEditorUtils {

    private static final String TERRAIN = "terrain";

    public static void setEntityOnTile(int floor, int x, int y, String label, boolean sendToServer) {
        GameData gameData = GameData.gameData;
        Entity entity = BlocksReader.getEntity(label);
        if (entity == null || gameData == null) {
            return;
        }

        WorldMapData worldMapData = gameData.getWorldMapData();
        worldMapData.getEntitiesOnTile(floor, x, y).ifPresent(entitiesOnTile -> {
            if (TERRAIN.equals(entity.getProperties().getType())) {
                TileUtils.replaceEntityOnBottom(entity.getId(), entitiesOnTile);
                //SwapEdgeHelper.changeAround(floor, x, y);
            } else {
                DataSync.sendPutModel(x, y, label);
                //addItemEntity(entity, entitiesOnTile, floor, x, y);
            }
            if (sendToServer) {
                //DataSync.sendPutModel(x, y, label);
            }
        });
        SwapEdgeHelper.changeAround(floor, x, y);
    }

    public static void itemSpawnMessageHandler(ItemSpawnMessage message) {
        GameData gameData = GameData.gameData;
        Entity entityBase = BlocksReader.getEntity(message.getPrefab());
        if (entityBase == null || gameData == null) {
            return;
        }

        WorldMapData worldMapData = gameData.getWorldMapData();
        worldMapData.getEntitiesOnTile(message.getFloor(), message.getTileX(), message.getTileY()).ifPresent(entitiesOnTile -> {
            addNewEntity(entityBase, message.getId(), entitiesOnTile, message.getFloor(), message.getTileX(), message.getTileY());
        });
    }

    private static void addNewEntity(Entity entityBase, long id, Deque<Long> entitiesQueue, int floor, int x, int y) {
        GameData gameData = GameData.gameData;

        Entity entity = EntityMapper.getNewEntity(entityBase, EntityType.LOCAL, new Vector2f(x * TILE_SIZE, y * TILE_SIZE), floor);
        if (entity == null || gameData == null) {
            return;
        }
        entity.setId(id);

        EntityProperties properties = entity.getProperties();
        List<Action> actionList = properties.getActionList();
        for (Action action : actionList) {
            if (ON_PUT.equals(action.getInvoke())) {
                properties.getActionListToDo().add(action);
            }
        }

        //Add to gameData to make it visible in TileActionSystem
        gameData.addEntity(entity);
        TileUtils.addEntity(entity.getId(), entitiesQueue);
    }

    private static void addItemEntity(Entity entityBase, Deque<Long> entitiesQueue, int floor, int x, int y) {
        GameData gameData = GameData.gameData;

        boolean containsOnPutAction = !entityBase.getProperties().getActionList().stream()
                .filter(action -> ON_PUT.equals(action.getInvoke()))
                .toList().isEmpty();

        if (!containsOnPutAction && !entityBase.getProperties().isStackable()) {
            TileUtils.addEntity(entityBase.getId(), entitiesQueue);
            return;
        }

        Entity entity = EntityMapper.getNewEntity(entityBase, EntityType.LOCAL, new Vector2f(x * TILE_SIZE, y * TILE_SIZE), floor);
        if (entity == null || gameData == null) {
            return;
        }
//        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
//        if (positionComponent != null) {
//            positionComponent.setPosition(new Vector2f(x * TILE_SIZE, y * TILE_SIZE));
//            positionComponent.setFloor(floor);
//        } else {
//            PositionComponent2D newPositionComponent = new PositionComponent2D(new Vector2f(x * TILE_SIZE, y * TILE_SIZE));
//            newPositionComponent.setFloor(floor);
//            entity.addComponent(newPositionComponent);
//        }

        EntityProperties properties = entity.getProperties();
        List<Action> actionList = properties.getActionList();
        for (Action action : actionList) {
            if (ON_PUT.equals(action.getInvoke())) {
                properties.getActionListToDo().add(action);
            }
        }

        //Add to gameData to make it visible in TileActionSystem
        gameData.addEntity(entity);
        TileUtils.addEntity(entity.getId(), entitiesQueue);
    }
}
