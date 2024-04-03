package org.game.ui.system;

import org.game.GameData;
import org.game.system.renderer.BaseRenderer;
import org.game.system.shader.ShaderEnum;
import org.game.system.shader.ShaderProgram;
import org.game.ui.component.RawUiModel;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

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
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class UiRendererSystem extends BaseRenderer {

    private final ShaderProgram shaderProgram;
    private final List<RawUiModel> guiList;

    public UiRendererSystem(GameData gameData) {
        super(gameData);
        this.guiList = new ArrayList<>();
        this.shaderProgram = getGameData().getShaderManager().getShader(ShaderEnum.UI);
    }

    @Override
    public void update(float deltaTime) {
        render();
    }

    @Override
    public void delete() {
        guiList.forEach(RawUiModel::remove);
    }

    @Override
    public void init() {

    }

    public void addGui(RawUiModel rawUiModel) {
        this.guiList.add(rawUiModel);
    }

    public void addGui(List<RawUiModel> rawUiModels) {
        this.guiList.addAll(rawUiModels);
    }

    private void setPointer() {
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
    }

    private void setUniforms(RawUiModel rawUiModel) {
        int transformationMatrixID = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "transformation");
        Matrix4f transformation = new Matrix4f();
        transformation.set(transformation(rawUiModel.getScale(), rawUiModel.getPosition()));
        FloatBuffer transformationMatrix = BufferUtils.createFloatBuffer(16);
        transformation.get(transformationMatrix);
        glUniformMatrix4fv(transformationMatrixID, false, transformationMatrix);
    }

    private void render() {
        shaderProgram.use();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        for (RawUiModel rawUiModel : guiList) {
            setUniforms(rawUiModel);
            glBindVertexArray(rawUiModel.getVaoID());
            glBindBuffer(GL_ARRAY_BUFFER, rawUiModel.getVboID());
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
        glDisable(GL_BLEND);
        shaderProgram.stop();
    }
}
