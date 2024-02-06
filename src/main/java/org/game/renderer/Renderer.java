package org.game.renderer;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    public void render(int vaoID, Model model, int textureType) {
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, model.getVboID());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.getIboID());

        if (textureType == 0) {
            glBindTexture(GL_TEXTURE_2D, model.getTextureID());
        } else {
            glBindTexture(GL_TEXTURE_CUBE_MAP, model.getTextureID());
        }

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        model.setPointers();

        glDrawElements(GL_TRIANGLES, model.getIndexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_TEXTURE_2D, 0);
        glBindBuffer(GL_TEXTURE_CUBE_MAP, 0);
        glBindVertexArray(0);
    }

    public void render(Renderable renderable) {
        renderable.render();
    }
}
