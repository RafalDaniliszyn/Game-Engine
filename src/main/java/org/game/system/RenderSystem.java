package org.game.system;

import org.game.GameData;
import org.game.component.MeshComponent;
import org.game.component.PositionComponent;
import org.game.renderer.Camera;
import org.game.renderer.ShaderProgram;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class RenderSystem extends BaseSystem {

    public RenderSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        getGameData().getEntities(MeshComponent.class, PositionComponent.class).forEach(((id, entity) -> {
            MeshComponent mesh = entity.getComponent(MeshComponent.class);
            PositionComponent pos = entity.getComponent(PositionComponent.class);
            setMvp(getGameData().getProjection(), getGameData().getShaderProgram(), pos);
            setTextureType(mesh);
            render(mesh);
        }));
    }

    @Override
    public void delete() {
        getGameData().getEntities(MeshComponent.class).forEach(((id, entity) -> {
            MeshComponent mesh = entity.getComponent(MeshComponent.class);
            remove(mesh);
        }));
    }

    @Override
    public void init() {

    }

    private void remove(MeshComponent mesh) {
        glDeleteBuffers(mesh.getVboID());
        glDeleteBuffers(mesh.getIboID());
        glDeleteBuffers(mesh.getTextureID());
        glDeleteVertexArrays(mesh.getVaoID());
    }

    private void setMvp(Matrix4f uProjection, ShaderProgram shaderProgram, PositionComponent pos) {
        int uMvpID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
        Matrix4f MVP = new Matrix4f();
        MVP.set(uProjection).mul(Camera.getView()).mul(transformation(pos.getScale(), pos.getPosition(), pos.getRotationX(), pos.getRotationY(), pos.getRotationZ()));
        FloatBuffer MVPmatrix = BufferUtils.createFloatBuffer(16);
        MVP.get(MVPmatrix);
        glUniformMatrix4fv(uMvpID, false, MVPmatrix);
    }

    private Matrix4f transformation(Vector3f scale, Vector3f position, float rotationX, float rotationY, float rotationZ) {
        Matrix4f transform = new Matrix4f();
        transform.identity()
                .translate(position)
                .rotateX(Math.toRadians(rotationX))
                .rotateY(Math.toRadians(rotationY))
                .rotateZ(Math.toRadians(rotationZ))
                .scale(scale);
        return transform;
    }


    private void setTextureType(MeshComponent mesh) {
        glUniform1i(mesh.getTextureTypeUniformLocation(), mesh.getTextureType());
    }

    private void setPointers() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 + 4 + 2) * Float.BYTES, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, (3 + 4 + 2) * Float.BYTES, 3 * Float.BYTES);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 9 * Float.BYTES, 7 * Float.BYTES);
    }

    private void render(MeshComponent mesh) {
        glBindVertexArray(mesh.getVaoID());
        glBindBuffer(GL_ARRAY_BUFFER, mesh.getVboID());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getIboID());

        if (mesh.getTextureType() == 0) {
            glBindTexture(GL_TEXTURE_2D, mesh.getTextureID());
        } else {
            glBindTexture(GL_TEXTURE_CUBE_MAP, mesh.getTextureID());
        }

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        setPointers();

        glDrawElements(GL_TRIANGLES, mesh.getIndexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindBuffer(GL_TEXTURE_CUBE_MAP, 0);
        glBindVertexArray(0);
    }
}
