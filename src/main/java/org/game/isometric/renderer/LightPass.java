package org.game.isometric.renderer;

import org.game.GameData;
import org.game.WindowCallbackProcessor;
import org.game.isometric.Camera2D;
import org.game.network.client.DataSync;
import org.game.network.client.model.ClientMessage;
import org.game.network.client.model.PickupItemMessage;
import org.game.network.client.model.ToolUseRequestMessage;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.game.ui.newUiConcept.UiKeyCallback;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.isometric.WorldSettings.TILE_SIZE;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;

public class LightPass {
    private final ShaderProgram lightShader;
    private int lightFBO;
    private int lightTexture;
    private List<Light> lights;
    private Light playerLight;
    private int quadVAO;
    private int quadVBO;

    private final int lightRadiusID;
    private final int lightColorID;
    private final int intensityID;
    private final int lightPosID;

    private final float[] quadVertices = {
            // pos      // uv
            -1f,  1f,   0f, 1f,
            -1f, -1f,   0f, 0f,
            1f, -1f,   1f, 0f,

            -1f,  1f,   0f, 1f,
            1f, -1f,   1f, 0f,
            1f,  1f,   1f, 1f
    };

    public LightPass() {
        lightShader = GameData.gameData.getShaderManager().getShader(ShaderEnum.LIGHT_ORTHO);
        lightRadiusID = GL20.glGetUniformLocation(lightShader.getProgramID(), "lightRadius");
        lightColorID  = GL20.glGetUniformLocation(lightShader.getProgramID(), "lightColor");
        intensityID   = GL20.glGetUniformLocation(lightShader.getProgramID(), "intensity");
        lightPosID    = GL20.glGetUniformLocation(lightShader.getProgramID(), "lightPos");
        setupLightFBO();
        setupLights();
        setupQuadVAO();
        WindowCallbackProcessor.getInstance().addUiKeyCallback(new UiKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                //test
                //DataSync.sendClientMessage(new ClientMessage(key));
//                DataSync.sendClientMessage(new PickupItemMessage(15, 15));
//                DataSync.sendClientMessage(new ToolUseRequestMessage(12325));

                if (key == GLFW_KEY_5 && action == GLFW_PRESS) {
                    randomLightTest();
                }
            }
        });
    }

    public void render(Matrix4f projection) {
        lightShader.use();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, lightFBO);
        glDisable(GL_DEPTH_TEST);

        glClearColor(0f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE); // ADDITIVE

        glBindVertexArray(quadVAO);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, lightTexture);

        for (Light light : lights) {
            lightPosToWorldSpace(light.x, light.y, projection);
            glUniform1f(lightRadiusID, light.radius);
            glUniform3f(lightColorID, light.r, light.g, light.b);
            glUniform1f(intensityID, light.intensity);

            glDrawArrays(GL_TRIANGLES, 0, 6);
        }

        lightPosToWorldSpace(playerLight.x, playerLight.y, projection);
        glUniform1f(lightRadiusID, playerLight.radius);
        glUniform3f(lightColorID, playerLight.r, playerLight.g, playerLight.b);
        glUniform1f(intensityID, playerLight.intensity);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glDisable(GL_BLEND);
        lightShader.stop();
    }

    public void randomLightTest() {
        Random random = new Random();
        playerLight.r = random.nextFloat();
        playerLight.g = random.nextFloat();
        playerLight.b = random.nextFloat();
//        for (Light light : lights) {
//            light.r = random.nextFloat();
//            light.g = random.nextFloat();
//            light.b = random.nextFloat();
//            light.x = TILE_SIZE * random.nextInt(45) + 1;
//            light.y = TILE_SIZE * random.nextInt(45) + 1;
//        }

    }

    public void refresh() {
        glDeleteFramebuffers(lightFBO);
        glDeleteTextures(lightTexture);
        setupLightFBO();
        setupQuadVAO();
    }

    private void setupQuadVAO() {
        quadVAO = glGenVertexArrays();
        quadVBO = glGenBuffers();

        glBindVertexArray(quadVAO);
        glBindBuffer(GL_ARRAY_BUFFER, quadVBO);

        glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW);

        // position
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // uv
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    private void lightPosToWorldSpace(float lightX, float lightY, Matrix4f projection) {
        // convert world → screen for shader
        Matrix4f lightVP = new Matrix4f();
        lightVP.set(projection)
                .mul(Camera2D.getView());

        Vector4f lightWorld = new Vector4f(lightX, lightY, 0, 1);
        Vector4f clip = new Vector4f();
        lightVP.transform(lightWorld, clip);

        float screenX = (clip.x / clip.w) * 0.5f + 0.5f;
        float screenY = (clip.y / clip.w) * 0.5f + 0.5f;

        glUniform2f(lightPosID, screenX, screenY);
    }

    private void setupLightFBO() {
        this.lightFBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, lightFBO);

        lightTexture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, lightTexture);

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

        glFramebufferTexture2D(
                GL_FRAMEBUFFER,
                GL_COLOR_ATTACHMENT0,
                GL_TEXTURE_2D,
                lightTexture,
                0
        );

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("LIGHT FBO ERROR");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void setPlayerLightTest(Vector2f position) {
        float x = position.x() + (TILE_SIZE * 0.5f);
        float y = position.y() + (TILE_SIZE * 0.5f);
        if (playerLight != null) {
            playerLight.x = x;
            playerLight.y = y;
        } else {
            playerLight = new Light(x, y,0.8f,0.5f, 0.1f, 0.5f, 1.0f);
        }
    }

    private void setupLights() {
        lights = new ArrayList<>();
        lights.add(new Light(10.0f*TILE_SIZE, 15.0f*TILE_SIZE, 0.7584f,0.0541f, 0.2451f, 0.124f, 0.454f));
        lights.add(new Light(15.0f*TILE_SIZE, 15.0f*TILE_SIZE, 1.4224f,0.03214f, 0.7f, 0.7f, 0.1f));
        lights.add(new Light(45.0f*TILE_SIZE, 15.0f*TILE_SIZE, 3.452f, 0.024f, 0.5565f, 0.454f, 0.0f));
        lights.add(new Light(5.0f*TILE_SIZE,  35.0f*TILE_SIZE, 2.9604f,0.02f, 0.77f, 0.54f, 0.0112f));
        lights.add(new Light(15.0f*TILE_SIZE, 25.0f*TILE_SIZE, 1.756f, 0.09412f, 0.741f, 0.758f, 0.1414f));
        lights.add(new Light(45.0f*TILE_SIZE, 35.0f*TILE_SIZE, 7.7101f,0.0121f, 0.544f, 0.0124f, 0.0354f));

    }

    public int getLightTexture() {
        return lightTexture;
    }
}
