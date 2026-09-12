package org.game.ui.system;

import org.game.GameData;
import org.game.WindowCallbackProcessor;
import org.game.system.renderer.BaseRenderer;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.game.ui.component.Container;
import org.game.ui.component.RawUiModel;
import org.game.ui.newUiConcept.UiContainer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.game.GraphicsDisplay.BASE_HEIGHT;
import static org.game.GraphicsDisplay.BASE_WIDTH;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;
import static org.game.math.Maths.transformation;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_BLEND;
import static org.lwjgl.opengl.GL15.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL15.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL15.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL15.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBindTexture;
import static org.lwjgl.opengl.GL15.glBlendFunc;
import static org.lwjgl.opengl.GL15.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class UiRendererSystem extends BaseRenderer {

    private final ShaderProgram shaderProgram;
    private final List<RawUiModel> guiList;
    private final List<UiContainer> containerList;

    public UiRendererSystem(GameData gameData) {
        super(gameData);
        this.guiList = new ArrayList<>();
        this.containerList = new ArrayList<>();
        this.shaderProgram = getGameData().getShaderManager().getShader(ShaderEnum.UI);
    }

    @Override
    public void update(float deltaTime) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, WIDTH, HEIGHT);
        renderContainers();
        render();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void delete() {
        guiList.forEach(RawUiModel::remove);
    }

    @Override
    public void init() {
        WindowCallbackProcessor.getInstance();
    }

    public void addGui(RawUiModel rawUiModel) {
        this.guiList.add(rawUiModel);
    }

    public void addGuiContainer(Container container) {
        this.guiList.addAll(container.getElements());
        this.guiList.addAll(container.getRawUiModelList());
    }

    public void addUiContainer(UiContainer uiContainer) {
        this.containerList.add(uiContainer);
    }

    public void addUiContainers(List<UiContainer> uiContainers) {
        this.containerList.addAll(uiContainers);
    }

    private void setPointer() {
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    private void setUniforms(RawUiModel rawUiModel) {
        int transformationMatrixID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "transformation");
        Matrix4f transformation = new Matrix4f();
        transformation.set(get2DProjection()).mul(transformation(rawUiModel.getScale(), rawUiModel.getPosition()));
        FloatBuffer transformationMatrix = BufferUtils.createFloatBuffer(16);
        transformation.get(transformationMatrix);
        glUniformMatrix4fv(transformationMatrixID, false, transformationMatrix);
    }

    private Matrix4f get2DProjection() {
        Matrix4f projection = new Matrix4f();
        projection.ortho2D(0.0f, BASE_WIDTH, 0.0f, BASE_HEIGHT);
        return projection;
    }

    private void renderContainers() {
        shaderProgram.use();
        glDisable(GL_BLEND);
        for (UiContainer uiContainer : containerList) {
            if (uiContainer.isVisible()) {
                List<RawUiModel> rawUiModelList = uiContainer.generateRawUiModels();
                for (RawUiModel rawUiModel : rawUiModelList) {
                    setUniforms(rawUiModel);
                    glBindVertexArray(rawUiModel.getVaoID());
                    glBindBuffer(GL_ARRAY_BUFFER, rawUiModel.getVboID());
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, rawUiModel.getTextureID());

                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    setPointer();

                    glDisable(GL_CULL_FACE);
                    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
                    glEnable(GL_CULL_FACE);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);

                    glBindBuffer(GL_ARRAY_BUFFER, 0);
                    glBindBuffer(GL_TEXTURE_2D, 0);
                    glBindVertexArray(0);
                }
            }
        }
        shaderProgram.stop();
    }

    private void render() {
        shaderProgram.use();
        glDisable(GL_BLEND);
        for (RawUiModel rawUiModel : guiList) {
            setUniforms(rawUiModel);
            glBindVertexArray(rawUiModel.getVaoID());
            glBindBuffer(GL_ARRAY_BUFFER, rawUiModel.getVboID());
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, rawUiModel.getTextureID());

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            setPointer();

            glDisable(GL_CULL_FACE);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
            glEnable(GL_CULL_FACE);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_TEXTURE_2D, 0);
            glBindVertexArray(0);
        }
        shaderProgram.stop();
    }
}
