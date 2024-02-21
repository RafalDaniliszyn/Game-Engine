package org.game;

import org.game.renderer.ShaderProgram;
import org.game.renderer.TextureEnum;
import org.joml.Vector3f;

public class MeshData {

    private String name;
    private TextureEnum texture;
    private int textureID;
    private int textureType = 0;
    private int textureTypeUniformLocation;
    private ShaderProgram shaderProgram;
    private float[] vertices;
    private int[] indices;
    private Vector3f position;
    private Vector3f scale;

    public MeshData(float[] vertices, int[] indices, Vector3f position, Vector3f scale) {
        this.vertices = vertices;
        this.indices = indices;
        this.position = position;
        this.scale = scale;

    }

    public int getTextureID() {
        return textureID;
    }

    public int getTextureType() {
        return textureType;
    }

    public int getTextureTypeUniformLocation() {
        return textureTypeUniformLocation;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextureEnum getTexture() {
        return texture;
    }

    public void setTexture(TextureEnum texture) {
        this.texture = texture;
    }
}
