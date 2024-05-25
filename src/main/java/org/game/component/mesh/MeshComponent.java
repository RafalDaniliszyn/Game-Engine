package org.game.component.mesh;

import org.game.component.Component;
import org.game.isometric.component.ComponentEnum;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MeshComponent extends Component {

    private int textureID;
    private int textureType = 0;
    private int textureTypeUniformLocation;
    private float[] vertices;
    private int[] indices;
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private Vector3f scale;
    private int vaoID, vboID, iboID;
    private int renderMode;
    private boolean cullFace;
    private boolean settings;
    private static final int STRIDE = 12;

    public MeshComponent(float[] vertices, int[] indices, Vector3f position, Vector3f scale) {
        super();
        this.vaoID = glGenVertexArrays();
        this.vertices = vertices;
        this.indices = indices;
        this.position = position;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.rotationZ = 0.0f;
        this.scale = scale;
        this.cullFace = true;
        this.create();
    }

    public MeshComponent(float[] vertices, int[] indices, Vector3f position, Vector3f scale, int textureID) {
        super();
        this.vaoID = glGenVertexArrays();
        this.vertices = vertices;
        this.indices = indices;
        this.position = position;
        this.rotationX = 0.0f;
        this.rotationY = 0.0f;
        this.rotationZ = 0.0f;
        this.scale = scale;
        this.textureID = textureID;
        this.renderMode = GL_TRIANGLES;
        this.cullFace = true;
        this.create();
    }

    public void create() {
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);

        iboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
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

    public void setTextureType(int textureType) {
        this.textureType = textureType;
    }

    public void setTextureTypeUniformLocation(int textureTypeUniformLocation) {
        this.textureTypeUniformLocation = textureTypeUniformLocation;
    }

    public int getVertexCount() {
        return vertices.length / STRIDE;
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

    public int getVaoID() {
        return vaoID;
    }

    public int getTextureType() {
        return textureType;
    }

    public int getTextureTypeUniformLocation() {
        return textureTypeUniformLocation;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public int getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(int renderMode) {
        this.renderMode = renderMode;
    }

    public boolean isCullFace() {
        return cullFace;
    }

    public void setCullFace(boolean cullFace) {
        this.cullFace = cullFace;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }

    @Override
    public ComponentEnum getType() {
        return null;
    }
}
