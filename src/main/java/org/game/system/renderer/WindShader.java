package org.game.system.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class WindShader extends ShaderProgram {

    public WindShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    public void use() {
        glUseProgram(programID);
        setTimeUniform();
    }

    private void setTimeUniform() {
        int timeID = GL20.glGetUniformLocation(this.getProgramID(), "time");
        glUniform1f(timeID, (float) GLFW.glfwGetTime());
    }
}
