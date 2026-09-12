package org.game.isometric.renderer;

import org.game.GameData;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class FinalPass {
    private final ShaderProgram finalShader;
    private final int sceneTextureID;
    private final int lightTextureID;
    private int quadVAO;
    private final float[] quadVertices = {
            // pos      // uv
            -1f,  1f,   0f, 1f,
            -1f, -1f,   0f, 0f,
            1f, -1f,   1f, 0f,

            -1f,  1f,   0f, 1f,
            1f, -1f,   1f, 0f,
            1f,  1f,   1f, 1f
    };

    public FinalPass() {
        this.finalShader = GameData.gameData.getShaderManager().getShader(ShaderEnum.FINAL_ORTHO);
        setupFinalQuadVAO();
        sceneTextureID = glGetUniformLocation(finalShader.getProgramID(), "sceneTexture");
        lightTextureID = glGetUniformLocation(finalShader.getProgramID(), "lightTexture");
    }

    public void refresh() {
        setupFinalQuadVAO();
    }

    public void render(int sceneTexture, int lightTexture) {
        finalShader.use();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glBindVertexArray(quadVAO);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, sceneTexture);
        glUniform1i(sceneTextureID, 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, lightTexture);
        glUniform1i(lightTextureID, 1);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        finalShader.stop();
    }

    private void setupFinalQuadVAO() {
        quadVAO = glGenVertexArrays();
        int quadVBO = glGenBuffers();

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
}
