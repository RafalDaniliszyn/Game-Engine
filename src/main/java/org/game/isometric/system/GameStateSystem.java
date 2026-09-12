package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.GameLoadingState;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.action.Action;
import org.game.isometric.action.MoveDownAction;
import org.game.isometric.action.MoveUpAction;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.EntityMapper;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.entity.PlayerEntity2D;
import org.game.isometric.helper.IdGeneratorSessionServerPlayer;
import org.game.isometric.texture2D.TextureEnum2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.isometric.utils.PositionUtils;
import org.game.isometric.utils.TilePosition;
import org.game.network.client.DataSync;
import org.game.network.client.incomingDataHandler.HandlerAction;
import org.game.network.model.GameStateModel;
import org.game.network.model.PlayerStateModel;
import org.game.system.BaseSystem;
import org.joml.Vector2f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.game.isometric.action.Action.Invoke.ON_ENTER;
import static org.game.isometric.action.Action.Invoke.ON_PUT;
import static org.game.isometric.utils.PositionUtils.AbsoluteTilePosition;
import static org.game.isometric.utils.PositionUtils.getAbsoluteTilePositionFromWorldSpace;
import static org.game.isometric.utils.PositionUtils.getTilePosition;

public class GameStateSystem extends BaseSystem {

    private GameStateModel lastModelState;
    private final Map<String, Long> serverPlayerEntities;
    private static final String TERRAIN = "terrain";

    public GameStateSystem(GameData gameData) {
        super(gameData);
        addRequiredComponent(ComponentEnum.PlayerComponent2D);
        serverPlayerEntities = new HashMap<>();
        lastModelState = new GameStateModel();
    }

    @Override
    public void update(float deltaTime) {
        GameData gameData = getGameData();
        updateServerEntities();
        getEntitiesToProcess().forEach(id -> {
            Entity entity = gameData.getEntity(id);
            if (EntityType.NETWORK.equals(entity.getEntityType())) {
                return;
            }
            updatePlayerData(entity);
            this.lastModelState = DataSync.sendLocalGameState(lastModelState);
        });
        HandlerAction.invokeAll();
    }

    private void updatePlayerData(Entity entity) {
        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        Vector2f position = positionComponent.getPosition();
        MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);
        AbsoluteTilePosition absoluteTilePosition = getAbsoluteTilePositionFromWorldSpace(position, moveComponent.getDirection());
        TilePosition playerTilePosition = getTilePosition(absoluteTilePosition.x(), absoluteTilePosition.y());
        GameState.setPlayerPosition(playerTilePosition);
        GameState.setCurrentChunkX(playerTilePosition.chunkX());
        GameState.setCurrentChunkY(playerTilePosition.chunkY());
        GameState.setPlayerDirection(moveComponent.getDirection());
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    private void updateServerEntities() {
        Map<String, PlayerStateModel> playersMap = GameState.getPlayersStateMap();
        playersMap.forEach((name, playerStateModel) -> {
            GameData gameData = getGameData();
            if (!serverPlayerEntities.containsKey(name)) {
                createEntity(name, playerStateModel, gameData);
            } else {
                Long localEntityId = serverPlayerEntities.get(name);
                Entity entity = gameData.getEntity(localEntityId);
                PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
                MoveComponent2D moveComponent = entity.getComponent(MoveComponent2D.class);
                MeshComponent2D meshComponent = entity.getComponent(MeshComponent2D.class);
                if (positionComponent != null && moveComponent != null && meshComponent != null) {
                    if (moveComponent.getAnimationTimer().isActive()) {
                        return;
                    }
                    updateEntityAndActivateAnimation(playerStateModel, positionComponent, moveComponent);
                    setTextureBasedOnDirection(playerStateModel, entity, meshComponent);
                }
            }
        });
    }

    private void createEntity(String name, PlayerStateModel playerStateModel, GameData gameData) {
        float tileSize = WorldSettings.TILE_SIZE;
        Integer textureIdByLabel = TextureManager2D.getTextureIdByLabel(playerStateModel.getLabel());
        TextureEnum2D textureEnum2D = TextureManager2D.getTextureById(textureIdByLabel);
        if (textureEnum2D == null) {
            // TODO: 8/26/2024 to remove
            Long entityId = addNewEntity(gameData, playerStateModel.getLabel(), playerStateModel.getFloor(), tileSize, playerStateModel.getTileX(), playerStateModel.getTileY());
            if (entityId != null) {
                serverPlayerEntities.put(name, entityId);
                GameState.addToEntityIdByUserUuidMap(playerStateModel.getUserUuid(), entityId);
            }
        } else {
            Long entityId = createPlayerEntity(gameData, playerStateModel.getFloor(), playerStateModel.getTileX(), playerStateModel.getTileY(), textureIdByLabel);
            serverPlayerEntities.put(name, entityId);
            GameState.addToEntityIdByUserUuidMap(playerStateModel.getUserUuid(), entityId);
        }
    }

    private void updateEntityAndActivateAnimation(PlayerStateModel playerStateModel, PositionComponent2D positionComponent, MoveComponent2D moveComponent) {
        positionComponent.setFloor(playerStateModel.getFloor());
        moveComponent.setDirection(playerStateModel.getDirection());
        AbsoluteTilePosition receivedAbsoluteTilePosition = new AbsoluteTilePosition(playerStateModel.getTileX(), playerStateModel.getTileY());
        Vector2f currentWorldSpacePosition = positionComponent.getPosition();
        AbsoluteTilePosition currentAbsoluteTilePosition = getAbsoluteTilePositionFromWorldSpace(currentWorldSpacePosition);
        if (!currentAbsoluteTilePosition.equals(receivedAbsoluteTilePosition)) {
            PositionUtils.setDestinationTile(moveComponent, receivedAbsoluteTilePosition, null);
            moveComponent.getAnimationTimer().setActive(true);
        }
    }

    private void setTextureBasedOnDirection(PlayerStateModel playerStateModel, Entity entity, MeshComponent2D meshComponent) {
        EntityProperties properties = entity.getProperties();
        Map<Side, Integer> replaceableTextureIdMap = properties.getReplaceableTextureIdMap();
        Side side = null;
        switch (playerStateModel.getDirection()) {
            case LEFT -> side = Side.LEFT;
            case RIGHT -> side = Side.RIGHT;
            case UP -> side = Side.UP;
            case DOWN -> side = Side.DOWN;
        }
        meshComponent.setTextureID(replaceableTextureIdMap.get(side));
    }

    private Long createPlayerEntity(GameData gameData, int floor, int x, int y, Integer textureId) {
        Long nextId = IdGeneratorSessionServerPlayer.getNextId();
        PlayerEntity2D playerEntity2D = new PlayerEntity2D(nextId, textureId, x, y, floor, EntityType.NETWORK);
        gameData.addEntity(playerEntity2D);
        return playerEntity2D.getId();
    }

    private Long addNewEntity(GameData gameData, String label, int floor, float tileSize, int x, int y) {
        Entity entityBase = BlocksReader.getEntity(label);
        Entity entity = EntityMapper.getNewEntity(entityBase, EntityType.NETWORK, new Vector2f(x * tileSize, y * tileSize), floor);
        if (entity == null) {
            return null;
        }
//        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
//        if (positionComponent != null) {
//            positionComponent.setPosition(new Vector2f(x * tileSize, y * tileSize));
//            positionComponent.setFloor(floor);
//        } else {
//            PositionComponent2D newPositionComponent = new PositionComponent2D(new Vector2f(x * tileSize, y * tileSize));
//            newPositionComponent.setFloor(floor);
//            entity.addComponent(newPositionComponent);
//        }

        EntityProperties properties = entity.getProperties();
        List<Action> actionList = properties.getActionList();
        if (properties.getLabel().equals("HOLE_DOWN_2D")) {
            actionList.add(new MoveDownAction(false, ON_ENTER));
        }
        if (properties.getLabel().equals("HOLE_UP_2D")) {
            actionList.add(new MoveUpAction(false, ON_ENTER));
        }

        List<Action> actions = EntityMapper.toActionList(actionList);
        for (Action action : actions) {
            if (ON_PUT.equals(action.getInvoke())) {
                properties.getActionListToDo().add(action);
            }
        }

        gameData.addEntity(entity);
        boolean isTerrain = TERRAIN.equals(properties.getType());
        gameData.getWorldMapData().addEntityToTile(floor, x, y, entity.getId(), isTerrain);
        if (isTerrain) {
           // edgeReplacer.replaceEdges(floor, x, y);
        }
        return entity.getId();
    }
}
