package org.game.isometric.renderer;

import org.game.GameData;
import org.game.WindowCallbackProcessor;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.entity.EntityType;
import org.game.isometric.GameLoadingState;
import org.game.isometric.GameState;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.event.EventHandler;
import org.game.isometric.event.EventPublisher;
import org.game.isometric.event.LoadTextureEvent;
import org.game.isometric.utils.TilePosition;
import org.game.system.renderer.BaseRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.lang.Math;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.Optional;

import static org.game.GraphicsDisplay.BASE_HEIGHT;
import static org.game.GraphicsDisplay.BASE_WIDTH;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.LEFT_SHIFT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.isometric.WorldSettings.*;
import static org.game.isometric.texture2D.TextureManager2D.loadTexture;
import static org.lwjgl.opengl.GL11.glViewport;


public class Renderer2D extends BaseRenderer {

    private Matrix4f orthoProjection;
    private final ScenePass scenePass;
    private final LightPass lightPass;
    private final FinalPass finalPass;

    public Renderer2D(GameData gameData) {
        super(gameData);
        addRequiredComponent(ComponentEnum.MeshComponent2D);
        scenePass = new ScenePass();
        lightPass = new LightPass();
        finalPass = new FinalPass();
        this.orthoProjection = get2DProjection();
        WindowCallbackProcessor.getInstance().addWindowSizeCallback((window, width, height) -> {
            WIDTH = width;
            HEIGHT = height;
            glViewport((int) (WIDTH * LEFT_SHIFT), 0, width, height);
            scenePass.refresh();
            lightPass.refresh();
            finalPass.refresh();
            this.orthoProjection = get2DProjection();
        });
        EventPublisher.getInstance().addListener(LoadTextureEvent.class, new EventHandler<LoadTextureEvent>() {
            @Override
            public void handleEvent(LoadTextureEvent event) {
                Path texturePath = Paths.get(System.getProperty("user.dir"));
                Integer textureId = loadTexture(texturePath + "\\" + event.getPath(), "");
                String entityLabel = event.getEntityLabel();
                Entity entity = BlocksReader.getEntity(entityLabel);
                if (entity != null) {
                    entity.getProperties().getRotatedEntityIdMap().put(event.getRotation(), textureId);
                }
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        if (!GameLoadingState.CREATE_PLAYER_COMPLETE && !GameLoadingState.PLAYER_SERVER_DATA_RECEIVED) {
            return;
        }
        glViewport((int) (WIDTH * LEFT_SHIFT), 0, WIDTH, HEIGHT);
        GameData gameData = getGameData();

        int currentChunkX = GameState.getCurrentChunkX();
        int currentChunkY = GameState.getCurrentChunkY();
        int currentFloor = GameState.getCurrentFloor();
        TilePosition playerPosition = GameState.getPlayerPosition();
        int playerX = playerPosition.x() + currentChunkX * CHUNK_SIZE;
        int playerY = playerPosition.y() + currentChunkY * CHUNK_SIZE;
        int range = 14;

        Optional<Deque<Long>[][]> idQueuesOnChunk = gameData.getWorldMapData().getIdQueues(currentFloor);
        Optional<Deque<Side>[][]> edgeQueues = gameData.getWorldMapData().getEdgeQueues(currentFloor);

        if (idQueuesOnChunk.isEmpty()) {
            return;
        }
        Deque<Long>[][] deques = idQueuesOnChunk.get();
        Deque<Side>[][] edges = edgeQueues.get();
        int rxStart = Math.max(playerX - range, 0);
        int ryStart = Math.max(playerY - range, 0);
        int rxEnd = Math.min(playerX + range, deques.length-1);
        int ryEnd = Math.min(playerY + range, deques.length-1);

        Vector2f tilePosition = new Vector2f();
        scenePass.beforeRender();

        for (int y = ryStart; y < ryEnd; y++) {
            for(int x = rxEnd; x >= rxStart; x--) {
                int tileX = (int) (x * TILE_SIZE);
                int tileY = (int) (y * TILE_SIZE);
                int finalX = x;
                int finalY = y;
                deques[x][y].descendingIterator().forEachRemaining(id -> {
                    Entity entity = gameData.getEntity(id);
                    if (entity != null) {
                        String label = entity.getProperties().getLabel();

                        //to test only
                        scenePass.setWindUniform("TREE_2D".equals(label));

                        EntityProperties properties = entity.getProperties();
                        MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
                        int tWidth = mesh.gettWidth();
                        int offset = 0;
                        if (tWidth != TILE_SIZE && tWidth != 0) {
                            offset = (int) (TILE_SIZE % tWidth);
                        }

                        tilePosition.set(tileX - offset, tileY);

                        if ("terrain".equals(entity.getProperties().getType()) && entity.getProperties().hasReplaceableEdges() && edges[finalX][finalY].getLast() != null) {
                            scenePass.render(orthoProjection, mesh, entity.getProperties().getReplaceableTextureIdMap().get(edges[finalX][finalY].getFirst()), tilePosition, properties.getDepth());
                        } else {
                            scenePass.render(orthoProjection, mesh, tilePosition, properties.getDepth());
                        }
                    }
                });
            }
        }

        //Online Players
        gameData.getEntities(ComponentEnum.MeshComponent2D, ComponentEnum.ServerPlayerComponent2D).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
            if (positionComponent.getFloor() == GameState.getCurrentFloor()) {
                EntityProperties properties = entity.getProperties();
                scenePass.render(orthoProjection, mesh, positionComponent.getPosition(), properties.getDepth());
            }
        });

        //Player
        gameData.getEntities(ComponentEnum.MeshComponent2D, ComponentEnum.PlayerComponent2D).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
            if (positionComponent.getFloor() == GameState.getCurrentFloor()) {
                EntityProperties properties = entity.getProperties();
                scenePass.render(orthoProjection, mesh, positionComponent.getPosition(), properties.getDepth());
            }

            lightPass.setPlayerLightTest(positionComponent.getPosition());
        });
        scenePass.afterRender();

        lightPass.render(orthoProjection);
        finalPass.render(scenePass.getSceneTexture(), lightPass.getLightTexture());
    }

    @Override
    public void delete() {
        getGameData().getEntities(ComponentEnum.MeshComponent2D).forEach((id, entity) -> {
            entity.getComponent(MeshComponent2D.class).getRawModel().remove();
        });
    }

    @Override
    public void init() {

    }

    private Matrix4f get2DProjection() {
        Matrix4f projection = new Matrix4f();
        projection.ortho(
                -BASE_WIDTH /2.0f,
                BASE_WIDTH /2.0f,
                -BASE_HEIGHT /2.0f,
                BASE_HEIGHT /2.0f,
                0.0f,
                52.0f);
        return projection;
    }
}
