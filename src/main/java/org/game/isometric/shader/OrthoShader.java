package org.game.isometric.shader;

import org.game.system.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class OrthoShader extends ShaderProgram {

    public OrthoShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    public void use() {
        glUseProgram(programID);
    }
}
