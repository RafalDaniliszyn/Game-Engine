package org.game.isometric.mesh;

import org.game.isometric.WorldSettings;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RawModel {
    private int vaoID;
    private int vboID;
    private final float[] vertices;

    public RawModel() {
        float tileSize = WorldSettings.getTileSize();
        vertices = new float[] {
                0.0f, tileSize, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                tileSize, tileSize, 1.0f, 1.0f,
                tileSize, 0.0f, 1.0f, 0.0f
        };
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
}
