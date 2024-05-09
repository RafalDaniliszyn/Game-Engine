package org.game.isometric.component;

import org.game.component.Component;
import org.game.isometric.mesh.RawModel;
import org.game.system.shader.ShaderEnum;
import org.joml.Vector2f;

public class MeshComponent2D extends Component {
    private int textureID;
    private RawModel rawModel;
    private final ShaderEnum shaderType;
    private Vector2f scale;

    public MeshComponent2D(int textureID, Vector2f scale) {
        this.shaderType = ShaderEnum.ORTHO;
        this.textureID = textureID;
        this.rawModel = new RawModel();
        this.scale = scale;
    }

    public MeshComponent2D(int textureID) {
        this.shaderType = ShaderEnum.ORTHO;
        this.rawModel = new RawModel();
        this.scale = new Vector2f(1.0f, 1.0f);
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public void setRawModel(RawModel rawModel) {
        this.rawModel = rawModel;
    }

    public ShaderEnum getShaderType() {
        return shaderType;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

}
