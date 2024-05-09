package org.game.isometric.renderer;

import org.game.GameData;
import org.game.entity.EntityProperties;
import org.game.isometric.Camera2D;
import org.game.isometric.GameState;
import org.game.isometric.WorldSettings;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.mesh.RawModel;
import org.game.system.renderer.BaseRenderer;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;

import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.isometric.utils.MathUtils.transformation2D;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer2D extends BaseRenderer {

    private final ShaderProgram shaderProgram;

    public Renderer2D(GameData gameData) {
        super(gameData);
        this.shaderProgram = getGameData().getShaderManager().getShader(ShaderEnum.ORTHO);
    }

    @Override
    public void update(float deltaTime) {
        GameData gameData = getGameData();
        int currentChunkX = GameState.getCurrentChunkX();
        int currentChunkY = GameState.getCurrentChunkY();
        float offset = WorldSettings.CHUNK_SIZE * WorldSettings.TILE_SIZE;
        int currentX = (int) ((currentChunkX + 0.5) * offset);
        int currentY = (int) ((currentChunkY + 0.5) * offset);

        gameData.getEntities(entity -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            if (positionComponent != null) {
                Vector2f position = positionComponent.getPosition();
                if (position.x > currentX - offset && position.x < currentX + offset
                        && position.y > currentY - offset && position.y < currentY + offset) {
                    return positionComponent.getFloor() == GameState.getCurrentFloor();
                }
            }
            return false;
        }, MeshComponent2D.class).forEach((id, entity) -> {
            PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
            Vector2f position = positionComponent.getPosition();
            if (position.x > currentX - offset && position.x < currentX + offset
                    && position.y > currentY - offset && position.y < currentY + offset) {
                MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
                EntityProperties properties = entity.getProperties();
                render(mesh, positionComponent, properties.getDepth());
            }
        });
    }

    @Override
    public void delete() {
        getGameData().getEntities(MeshComponent2D.class).forEach((id, entity) -> {
            entity.getComponent(MeshComponent2D.class).getRawModel().remove();
        });
    }

    @Override
    public void init() {

    }

    private void setPointer() {
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    private void setUniforms(MeshComponent2D meshComponent2D, PositionComponent2D position, float depth) {
        int transformationMatrixID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
        Matrix4f MVP = new Matrix4f();
        MVP.set(get2DProjection()).mul(Camera2D.getView()).mul(transformation2D(meshComponent2D.getScale(), position.getPosition()));
        FloatBuffer transformationMatrix = BufferUtils.createFloatBuffer(16);
        MVP.get(transformationMatrix);
        glUniformMatrix4fv(transformationMatrixID, false, transformationMatrix);

        int depthID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "depth");
        glUniform1f(depthID, depth);
    }

    private Matrix4f get2DProjection() {
        Matrix4f projection = new Matrix4f();
        projection.ortho(-WIDTH/2.0f, WIDTH/2.0f, -HEIGHT/2.0f, HEIGHT/2.0f, 0.0f, 100.0f);
        return projection;
    }

    private void render(MeshComponent2D meshComponent2D, PositionComponent2D position, float depth) {
        shaderProgram.use();
        RawModel rawModel = meshComponent2D.getRawModel();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        setUniforms(meshComponent2D, position, depth);

        glBindVertexArray(rawModel.getVaoID());
        glBindBuffer(GL_ARRAY_BUFFER, rawModel.getVboID());
        glBindTexture(GL_TEXTURE_2D, meshComponent2D.getTextureID());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        setPointer();

        //glDisable(GL_CULL_FACE);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        //glEnable(GL_CULL_FACE);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);

        glDisable(GL_BLEND);

        shaderProgram.stop();
    }
}
