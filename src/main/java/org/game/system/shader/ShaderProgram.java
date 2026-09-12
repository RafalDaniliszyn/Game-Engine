package org.game.system.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public abstract class ShaderProgram {

    private String vertexFile;
    private String fragmentFile;

    public int programID, vertexID, fragmentID;

    public ShaderProgram(String vertexFile, String fragmentFile) {
        this.vertexFile = vertexFile;
        this.fragmentFile = fragmentFile;
    }

    public void create() {
        programID = glCreateProgram();
        vertexID = loadShader(GL_VERTEX_SHADER, vertexFile);
        fragmentID = loadShader(GL_FRAGMENT_SHADER, fragmentFile);
        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);
        glLinkProgram(programID);
        glValidateProgram(programID);
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void delete() {
        stop();
        glDetachShader(programID, vertexID);
        glDetachShader(programID, fragmentID);
        glDeleteProgram(programID);
    }

    public abstract void use();

    public void stop() {
        glUseProgram(0);
    }

    private String readFileStream(String shaderFile) {
        InputStream resourceStream = DefaultShader.class.getResourceAsStream(shaderFile);
        if (resourceStream == null) {
            throw new RuntimeException("Resource not found: " + shaderFile);
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File loading completed.");
        return builder.toString();
    }

    private int loadShader(int type, String file) {
        int id = glCreateShader(type);
        glShaderSource(id, readFileStream(file));
        glCompileShader(id);
        return id;
    }

    public int getProgramID() {
        return programID;
    }
}
