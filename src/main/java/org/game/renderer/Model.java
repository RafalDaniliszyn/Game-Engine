package org.game.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL40.*;

public class Model implements Renderable {

    public int textureID;
    public int textureType = 0;
    public int textureTypeUniformLocation;
    public ShaderProgram shaderProgram;
    private float[] vertices;
    private int[] indices;
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private Vector3f scale;
    private int vaoID, vboID, iboID;

    public Model(float[] vertices, int[] indices, Vector3f position, Vector3f scale, int vaoID) {
        this.vaoID = vaoID;
        this.vertices = vertices;
        this.indices = indices;
        this.position = position;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.rotationZ = 0.0f;
        this.scale = scale;
        this.create();
    }

    public Model(float[] vertices, int[] indices, Vector3f position, float rotation, Vector3f scale) {
        this.vaoID = glGenVertexArrays();
        this.vertices = vertices;
        this.indices = indices;
        this.position = position;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.rotationZ = 0.0f;
        this.scale = scale;
        this.create();
    }


    @Override
    public void render() {
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);

        if (textureType == 0) {
            glBindTexture(GL_TEXTURE_2D, textureID);
        } else {
            glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);
        }

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        setPointers();

        glDrawElements(GL_TRIANGLES, getIndexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindBuffer(GL_TEXTURE_CUBE_MAP, 0);
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteBuffers(vboID);
        glDeleteBuffers(iboID);
        glDeleteBuffers(textureID);
        glDeleteVertexArrays(vaoID);
    }

    public void setPointers() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 + 4 + 2) * Float.BYTES, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, (3 + 4 + 2) * Float.BYTES, 3 * Float.BYTES);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 9 * Float.BYTES, 7 * Float.BYTES);
    }

    public void setTextureType(int textureTypeUniformLocation, int textureType) {
        this.textureTypeUniformLocation = textureTypeUniformLocation;
        this.textureType = textureType;
        glUniform1i(textureTypeUniformLocation, textureType);
    }

    public void setRenderType(int renderType, ShaderProgram shaderProgram) {
        int renderTypeID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "uRenderType");
        glUniform1i(renderTypeID, renderType);
    }

    public void setLocation(ShaderProgram shaderProgram) {
        int uTransformID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "uTransform");
        Matrix4f uTransform = transformation(this.scale, this.position);
        FloatBuffer transformMatrix = BufferUtils.createFloatBuffer(16);
        uTransform.get(transformMatrix);
        glUniformMatrix4fv(uTransformID, false, transformMatrix);
    }

    public void setMvp(Matrix4f uProjection, ShaderProgram shaderProgram) {
        int uMvpID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
        Matrix4f MVP = new Matrix4f();
        MVP.set(uProjection).mul(Camera.getView()).mul(transformation(scale, position, rotationX, rotationY, rotationZ));
        FloatBuffer MVPmatrix = BufferUtils.createFloatBuffer(16);
        MVP.get(MVPmatrix);
        glUniformMatrix4fv(uMvpID, false, MVPmatrix);
    }

    public void setLocation(ShaderProgram shaderProgram, Vector3f scale, Vector3f position, float rotationX, float rotationY, float rotationZ) {
        int uTransformID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "uTransform");
        Matrix4f uTransform = transformation(scale, position, rotationX, rotationY, rotationZ);
        FloatBuffer transformMatrix = BufferUtils.createFloatBuffer(16);
        uTransform.get(transformMatrix);
        glUniformMatrix4fv(uTransformID, false, transformMatrix);
    }

    private void create() {
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        iboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    }

    private Matrix4f transformation(Vector3f scale, Vector3f position) {
        Matrix4f transform = new Matrix4f();
        transform.identity()
                .translate(position)
                .rotateX(Math.toRadians(rotationX))
                .rotateY(Math.toRadians(rotationY))
                .rotateZ(Math.toRadians(rotationZ))
                .scale(scale);
        return transform;
    }

    public Matrix4f transformation(Vector3f scale, Vector3f position, float rotationX, float rotationY, float rotationZ) {
        Matrix4f transform = new Matrix4f();
        transform.identity()
                .translate(position)
                .rotateX(Math.toRadians(rotationX))
                .rotateY(Math.toRadians(rotationY))
                .rotateZ(Math.toRadians(rotationZ))
                .scale(scale);
        return transform;
    }

    public void setTexture(int textureID) {
        this.textureID = textureID;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public void setVertice(float vertice, int index) {
        this.vertices[index] = vertice;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public int getVboID() {
        return vboID;
    }

    public int getIboID() {
        return iboID;
    }

    public int getIndexCount() {
        return indices.length;
    }

    public int getTextureID() {
        return textureID;
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


}
