package org.game.isometric.renderer.renderer3DTest;

import org.game.GameData;
import org.game.GraphicsDisplay;
import org.game.WindowCallbackProcessor;
import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.Camera2D;
import org.game.isometric.GameLoadingState;
import org.game.isometric.GameState;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.event.EventHandler;
import org.game.isometric.event.EventPublisher;
import org.game.isometric.event.LoadTextureEvent;
import org.game.isometric.mesh.CubeModel;
import org.game.isometric.mesh.RawModel;
import org.game.isometric.utils.TilePosition;
import org.game.system.renderer.BaseRenderer;
import org.game.system.renderer.Projection;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.Optional;
import java.util.Random;

import static org.game.GraphicsDisplay.BASE_HEIGHT;
import static org.game.GraphicsDisplay.BASE_WIDTH;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.LEFT_SHIFT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.isometric.WorldSettings.*;
import static org.game.isometric.texture2D.TextureManager2D.loadTexture;
import static org.game.isometric.utils.MathUtils.transformation2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer2DV1 extends BaseRenderer {

    private final ShaderProgram shaderProgram;
    private Matrix4f orthoProjection;

    private final int transformationMatrixID;
    private final int depthID;
    private CubeModel cubeModel = new CubeModel();

    public Renderer2DV1(GameData gameData) {
        super(gameData);
        this.shaderProgram = getGameData().getShaderManager().getShader(ShaderEnum.ORTHO);
        addRequiredComponent(ComponentEnum.MeshComponent2D);
        this.orthoProjection = get2DProjection();
        this.transformationMatrixID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
        this.depthID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "depth");

        WindowCallbackProcessor.getInstance().addWindowSizeCallback((window, width, height) -> {
            WIDTH = width;
            HEIGHT = height;
            glViewport((int) (WIDTH * LEFT_SHIFT), 0, width, height);
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

        if (idQueuesOnChunk.isEmpty()) {
            return;
        }
        Deque<Long>[][] deques = idQueuesOnChunk.get();
        int rxStart = Math.max(playerX - range, 0);
        int ryStart = Math.max(playerY - range, 0);
        int rxEnd = Math.min(playerX + range, deques.length-1);
        int ryEnd = Math.min(playerY + range, deques.length-1);

        Vector2f tilePosition = new Vector2f();
        shaderProgram.use();
        for(int x = rxEnd; x >= rxStart; x--) {
            for (int y = ryStart; y < ryEnd; y++) {
                int tileX = (int) (x * TILE_SIZE);
                int tileY = (int) (y * TILE_SIZE);
                deques[x][y].descendingIterator().forEachRemaining(id -> {
                    Entity entity = gameData.getEntity(id);
                    if (entity != null) {
                        String label = entity.getProperties().getLabel();
                        setWindUniform("TREE_2D".equals(label));
                        EntityProperties properties = entity.getProperties();
                        tilePosition.set(tileX, tileY);
                        render(entity.getComponent(MeshComponent2D.class), tilePosition, properties.getDepth());
                    }
                });
            }
        }
        gameData.getEntities(ComponentEnum.MeshComponent2D, ComponentEnum.ServerPlayerComponent2D).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
            if (positionComponent.getFloor() == GameState.getCurrentFloor()) {
                EntityProperties properties = entity.getProperties();
                render(mesh, positionComponent.getPosition(), properties.getDepth());
            }
        });
        gameData.getEntities(ComponentEnum.MeshComponent2D, ComponentEnum.PlayerComponent2D).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
            if (positionComponent.getFloor() == GameState.getCurrentFloor()) {
                EntityProperties properties = entity.getProperties();
                render(mesh, positionComponent.getPosition(), properties.getDepth());
            }
        });
        shaderProgram.stop();
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

    private void setPointer() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
    }

    private void setUniforms(MeshComponent2D meshComponent2D, Vector2f position, float depth) {
        Matrix4f MVP = new Matrix4f();
        MVP.set(orthoProjection).mul(Camera2D.getView()).mul(transformation2D(meshComponent2D.getScale(), position, depth));
        FloatBuffer transformationMatrix = BufferUtils.createFloatBuffer(16);
        MVP.get(transformationMatrix);
        glUniformMatrix4fv(transformationMatrixID, false, transformationMatrix);

        glUniform1f(depthID, depth);
        setTimeUniform();
        setLightUniforms();
        //setWindSpeedUniform();
    }

    private void setLightUniforms() {
        int screenSizeID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "screenSize");
        Vector2f screenSize = GraphicsDisplay.getWindowSize();
        glUniform2f(screenSizeID, screenSize.x, screenSize.y);
    }

    private void setTimeUniform() {
        int timeID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "time");
        Random random = new Random();
        glUniform1f(timeID, (float) GLFW.glfwGetTime() + random.nextFloat(0.005f));
    }

    private void setWindUniform(boolean isWind) {
        float wind = isWind ? 1.0f : 0.0f;
        int windID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "wind");
        glUniform1f(windID, wind);
    }

    private void setWindSpeedUniform() {
        Random random = new Random();
        int windSpeedID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "windSpeed");
        glUniform1f(windSpeedID, 1.0f + random.nextFloat(0.5f));
    }

    private Matrix4f get2DProjection() {
        float aspect = (float) BASE_WIDTH / (float) BASE_HEIGHT;

        float fovY  = (float) Math.toRadians(60.0f);
        float zNear = 0.1f;
        float zFar  = 1000.0f;
        Matrix4f projection = new Matrix4f();
        projection.perspective(fovY, aspect, zNear, zFar);
        return projection;


//        Matrix4f projection = new Matrix4f();
//        projection.ortho(
//                -BASE_WIDTH /2.0f,
//                BASE_WIDTH /2.0f,
//                -BASE_HEIGHT /2.0f,
//                BASE_HEIGHT /2.0f,
//                -1000.0f,
//                1000.0f);
//        return projection;
    }

    private void render(MeshComponent2D meshComponent2D, Vector2f position, float depth) {
        glEnable(GL_BLEND);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBlendFunc(GL_ONE, GL_ZERO);
        setUniforms(meshComponent2D, position, depth);

        glBindVertexArray(cubeModel.getVaoID());
        glBindBuffer(GL_ARRAY_BUFFER, cubeModel.getVboID());
        glBindTexture(GL_TEXTURE_2D, meshComponent2D.getTextureID());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        setPointer();

        for (int face = 0; face < 6; face++) {
            glDrawArrays(GL_TRIANGLE_STRIP, face * 4, 4);
        }

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);

        glDisable(GL_BLEND);
    }
}

