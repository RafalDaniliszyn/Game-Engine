package org.game.system.renderer;

import org.game.GameData;
import org.game.component.LightComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.PositionComponent;
import org.game.Camera;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import java.util.List;

import static org.game.math.Maths.transformation;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_CULL_FACE;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glDisable;
import static org.lwjgl.opengl.GL15.glEnable;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class RenderSystem extends BaseRenderer {

    public RenderSystem(GameData gameData) {
        super(gameData);
        this.projection = Projection.getPerspectiveProjection(0.1f, 2500.0f, 60.0f);
    }
    public ShaderEnum currentShader;
    private final Matrix4f projection;

    @Override
    public void update(float deltaTime) {
        getGameData().getEntities(MeshComponent.class, PositionComponent.class).forEach(((id, entity) -> {
            List<MeshComponent> meshList = entity.getComponents(MeshComponent.class);
            ShaderEnum shaderType = entity.getProperties().getShaderType();
            ShaderProgram shaderProgram = getGameData().getShaderManager().getShader(shaderType);

            if (shaderType.equals(ShaderEnum.UI)) {
                return;
            }
            if (!shaderType.equals(currentShader)) {
                getGameData().getShaderManager().useShader(shaderType);
                currentShader = shaderType;
            }
            setLight(shaderProgram);
            for (MeshComponent mesh : meshList) {
                List<PositionComponent> pos = entity.getComponents(PositionComponent.class);
                for (PositionComponent position : pos) {
                    setMvp(this.projection, shaderProgram, position);
                    render(mesh);
                }
            }
        }));
    }

    @Override
    public void delete() {
        getGameData().getEntities(MeshComponent.class).forEach(((id, entity) -> {
            List<MeshComponent> meshList = entity.getComponents(MeshComponent.class);
            meshList.forEach(this::remove);
        }));
    }

    @Override
    public void init() {

    }

    private void remove(MeshComponent mesh) {
        getGameData().getShaderManager().remove();
        glDeleteBuffers(mesh.getVboID());
        glDeleteBuffers(mesh.getIboID());
        glDeleteBuffers(mesh.getTextureID());
        glDeleteVertexArrays(mesh.getVaoID());
    }

    private void setLight(ShaderProgram shaderProgram) {
        getGameData().getEntities(LightComponent.class, PositionComponent.class).forEach((id, entity) -> {
            LightComponent lightComponent = entity.getComponent(LightComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            setLightUniforms(shaderProgram, positionComponent, lightComponent);
        });
    }

    private void setMvp(Matrix4f uProjection, ShaderProgram shaderProgram, PositionComponent pos) {
        int uMvpID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
        Matrix4f MVP = new Matrix4f();
        MVP.set(uProjection).mul(Camera.getView()).mul(transformation(pos.getScale(), pos.getPosition(), pos.getRotationX(), pos.getRotationY(), pos.getRotationZ()));
        FloatBuffer MVPmatrix = BufferUtils.createFloatBuffer(16);
        MVP.get(MVPmatrix);
        glUniformMatrix4fv(uMvpID, false, MVPmatrix);

        int transformationMatrixID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "transformationMatrix");
        Matrix4f transformation = new Matrix4f();
        transformation.set(transformation(pos.getScale(), pos.getPosition(), pos.getRotationX(), pos.getRotationY(), pos.getRotationZ()));
        FloatBuffer transformationMatrix = BufferUtils.createFloatBuffer(16);
        transformation.get(transformationMatrix);
        glUniformMatrix4fv(transformationMatrixID, false, transformationMatrix);
    }

    private void setLightUniforms(ShaderProgram shaderProgram, PositionComponent pos, LightComponent lightComponent) {
        int lightPosition = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "lightPosition");
        Vector3f vec = new Vector3f(pos.getPosition());
        glUniform3f(lightPosition, vec.x, vec.y, vec.z);
        int lightColorID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "lightColor");
        Vector3f lightColorVec = new Vector3f(lightComponent.getLightColor());
        glUniform3f(lightColorID, lightColorVec.x, lightColorVec.y, lightColorVec.z);
    }

    private void setPointers() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 + 4 + 2 + 3) * Float.BYTES, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, (3 + 4 + 2 + 3) * Float.BYTES, 3 * Float.BYTES);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 12 * Float.BYTES, 7 * Float.BYTES);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 12 * Float.BYTES, 9 * Float.BYTES);
    }

    private void render(MeshComponent mesh) {
        glBindVertexArray(mesh.getVaoID());
        glBindBuffer(GL_ARRAY_BUFFER, mesh.getVboID());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getIboID());
        glBindTexture(GL_TEXTURE_2D, mesh.getTextureID());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        setPointers();

        if (mesh.isCullFace()) {
            glDisable(GL_CULL_FACE);
            glDrawElements(mesh.getRenderMode(), mesh.getIndexCount(), GL_UNSIGNED_INT, 0);
            glEnable(GL_CULL_FACE);
        } else {
            glDrawElements(mesh.getRenderMode(), mesh.getIndexCount(), GL_UNSIGNED_INT, 0);
        }

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);
    }
}
