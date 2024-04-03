package org.game.system.shader;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class DefaultShader extends ShaderProgram {
    public DefaultShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    public void use() {
        glUseProgram(programID);
    }

}
