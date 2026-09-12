package org.game.ui.component;

import org.game.isometric.texture2D.TextureManager2D;
import org.game.system.shader.ShaderEnum;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class RawUiModel {

    private int vaoID;
    private int vboID;
    private final ShaderEnum shaderType;
    private Vector3f scale;
    private Vector3f position;
    private int textureID;

    private final float[] vertices;

    public RawUiModel(Vector3f scale, Vector3f position, float width, float height) {
        this.vertices = new float[]{
                0.0f, height, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                width, height, 1.0f, 1.0f,
                width, 0.0f, 1.0f, 0.0f
        };
        this.shaderType = ShaderEnum.UI;
        this.scale = scale;
        this.position = position;
        this.textureID = TextureManager2D.loadTexture("/textures/2D/ui/uiBackground.png", "uiBackground");
        create();
    }

    public RawUiModel(Vector3f position, float width, float height, Vector2f uv1, Vector2f uv2, Vector2f uv3, Vector2f uv4) {
        this.vertices = new float[] {
                0.0f, height, uv1.x, uv1.y,
                0.0f, 0.0f, uv2.x, uv2.y,
                width, height, uv3.x, uv3.y,
                width, 0.0f, uv4.x, uv4.y
        };
        this.shaderType = ShaderEnum.UI;
        this.scale = new Vector3f(1.0f, 1.0f, 0.0f);
        this.position = position;
        this.textureID = TextureManager2D.loadTexture("/textures/2D/ui/uiBackground.png", "uiBackground");
        create();
    }

    public void remove() {
        glDeleteBuffers(vaoID);
        glDeleteVertexArrays(vboID);
    }

    private void create() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVboID() {
        return vboID;
    }

    public ShaderEnum getShaderType() {
        return shaderType;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }
}
