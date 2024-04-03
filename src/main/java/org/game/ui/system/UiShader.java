package org.game.ui.system;

import org.game.system.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class UiShader extends ShaderProgram {
    public UiShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    public void use() {
        glUseProgram(programID);
    }

}
