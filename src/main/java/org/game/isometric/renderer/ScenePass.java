package org.game.isometric.renderer;

import org.game.GameData;
import org.game.isometric.Camera2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.mesh.RawModel;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.isometric.utils.MathUtils.transformation2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;

public class ScenePass {

    private final ShaderProgram shaderProgram;
    private final int transformationMatrixID;
    private final int depthID;
    private final int timeID;
    private final int windID;
    private final int seedID;
    private final int xyID;
    private final int windWaveID;


    private int sceneFBO;
    private int rbo;
    private int sceneTexture;

    public ScenePass() {
        this.shaderProgram = GameData.gameData.getShaderManager().getShader(ShaderEnum.ORTHO);
        setupSceneFBO();
        this.transformationMatrixID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "MVP");
        this.depthID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "depth");
        this.timeID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "time");
        this.windID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "wind");

        this.seedID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "seed");
        this.xyID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "xy");
        this.windWaveID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "windWave");
    }

    public void refresh() {
        glDeleteFramebuffers(sceneFBO);
        glDeleteTextures(sceneTexture);
        glDeleteRenderbuffers(rbo);
        setupSceneFBO();
    }

    public void beforeRender() {
        shaderProgram.use();
        //bind sceneFBO to draw on it and not directly on screen
        glBindFramebuffer(GL_FRAMEBUFFER, sceneFBO);
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void afterRender() {
        shaderProgram.stop();
    }

    public void render(Matrix4f projection, MeshComponent2D meshComponent2D, int textureId, Vector2f position, float depth) {
        RawModel rawModel = meshComponent2D.getRawModel();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ZERO);
        setUniforms(projection, meshComponent2D, position, depth);

        glBindVertexArray(rawModel.getVaoID());
        glBindBuffer(GL_ARRAY_BUFFER, rawModel.getVboID());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        setPointer();

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);

        glDisable(GL_BLEND);
    }

    public void render(Matrix4f projection, MeshComponent2D meshComponent2D, Vector2f position, float depth) {
        RawModel rawModel = meshComponent2D.getRawModel();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ZERO);
        setUniforms(projection, meshComponent2D, position, depth);

        glBindVertexArray(rawModel.getVaoID());
        glBindBuffer(GL_ARRAY_BUFFER, rawModel.getVboID());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, meshComponent2D.getTextureID());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        setPointer();

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);

        glDisable(GL_BLEND);
    }

    public void setWindUniform(boolean isWind) {
        float wind = isWind ? 1.0f : 0.0f;
        glUniform1f(windID, wind);
    }

    private void setWindSpeedUniform() {
//        Random random = new Random();
//        int windSpeedID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "windSpeed");
//        glUniform1f(windSpeedID, 1.0f + random.nextFloat(0.5f));
    }

    private void setUniforms(Matrix4f projection, MeshComponent2D meshComponent2D, Vector2f position, float depth) {
        Matrix4f MVP = new Matrix4f();
        MVP.set(projection).mul(Camera2D.getView()).mul(transformation2D(meshComponent2D.getScale(), position));
        FloatBuffer transformationMatrix = BufferUtils.createFloatBuffer(16);
        MVP.get(transformationMatrix);
        glUniformMatrix4fv(transformationMatrixID, false, transformationMatrix);
        seedUniform(position);
        setWindWaveUniform(position);
        posXYUniform(position);
        glUniform1f(depthID, depth);
        setTimeUniform();
        //setWindSpeedUniform();
    }


    private void seedUniform(Vector2f pos) {
        glUniform1f(seedID, (float) Math.log(pos.x + pos.y));
    }

    private void posXYUniform(Vector2f pos) {
        glUniform2f(xyID, pos.x, pos.y);
    }

    private void setPointer() {
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    private void setTimeUniform() {
        glUniform1f(timeID, (float) GLFW.glfwGetTime());
    }

    private void setWindWaveUniform(Vector2f pos) {
        double time = System.currentTimeMillis() / 1000.0; // czas w sekundach
        double sinValue = sin(((2*Math.PI) /18020.0) * pos.x + time)+cos(pos.y%time);
        float normalizedValue = (float) ((sinValue + 1.0) / 2.0);

        glUniform1f(windWaveID, (float) sinValue);
    }

    private void setupSceneFBO() {
        //Create framebuffer
        this.sceneFBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, sceneFBO);

        //Create texture
        this.sceneTexture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, sceneTexture);

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                WIDTH,
                HEIGHT,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                0
        );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //Attach texture to FBO
        glFramebufferTexture2D(
                GL_FRAMEBUFFER,
                GL_COLOR_ATTACHMENT0,
                GL_TEXTURE_2D,
                sceneTexture,
                0
        );

        this.rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);

        glRenderbufferStorage(
                GL_RENDERBUFFER,
                GL_DEPTH24_STENCIL8,
                WIDTH,
                HEIGHT
        );

        glFramebufferRenderbuffer(
                GL_FRAMEBUFFER,
                GL_DEPTH_STENCIL_ATTACHMENT,
                GL_RENDERBUFFER,
                rbo
        );

        //check if it works
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("FBO ERROR");
        }

        //unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getSceneTexture() {
        return sceneTexture;
    }
}
