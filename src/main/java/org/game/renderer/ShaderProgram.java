package org.game.renderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private String vertexFile = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertex.glsl";
    private String fragmentFile = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\alphaFragment.glsl";

    public int programID, vertexID, fragmentID;

    public ShaderProgram() {
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

    public void create(String vertexFile, String fragmentFile) {
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

    public void use() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    private String readFile(String shaderFile) {
        File file = new File(shaderFile);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String string = "";
        while(scanner.hasNextLine()) {
            string += scanner.nextLine() + "\n";
        }
        return string;
    }

    private int loadShader(int type, String file) {
        int id = glCreateShader(type);
        glShaderSource(id, readFile(file));
        glCompileShader(id);
        return id;
    }

    public int getProgramID() {
        return programID;
    }
}
