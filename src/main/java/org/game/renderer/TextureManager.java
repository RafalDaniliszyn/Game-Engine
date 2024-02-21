package org.game.renderer;

import org.game.IdGenerator;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureManager {
    private final Map<TextureEnum, Integer> textures;

    public TextureManager() {
        textures = new HashMap<>();
        loadTextures();
    }

    public int loadTexture(TextureEnum textureEnum, int alpha, int flip) {
        String path = textureEnum.getPath();
        STBImage.stbi_set_flip_vertically_on_load(flip == 1);
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureEnum.getParam());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureEnum.getParam());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer chanels = BufferUtils.createIntBuffer(1);
        ByteBuffer data = STBImage.stbi_load(path, x, y, chanels, 0);

        switch (alpha){
            case 0:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, x.get(), y.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, data);
                glGenerateMipmap(GL_TEXTURE_2D);
                break;
            case 1:
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(), y.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
                glGenerateMipmap(GL_TEXTURE_2D);
                break;
        }
        STBImage.stbi_image_free(data);
        return texID;
    }

    private void loadTextures() {
        TextureEnum[] values = TextureEnum.values();
        int texCount = values.length;
        for (int i = 0; i < texCount; i++) {
            String path = values[i].getPath();
            STBImage.stbi_set_flip_vertically_on_load(values[i].getFlip() == 1);

            int texID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            IntBuffer x = BufferUtils.createIntBuffer(1);
            IntBuffer y = BufferUtils.createIntBuffer(1);
            IntBuffer chanels = BufferUtils.createIntBuffer(1);
            ByteBuffer data = STBImage.stbi_load(path, x, y, chanels, 0);

            boolean alpha = values[i].getAlpha() == 1;
            int internalFormat = alpha ? GL_RGBA : GL_RGB;
            glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, x.get(), y.get(), 0, internalFormat, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(data);

            textures.put(values[i], IdGenerator.getNextIntegerId());
        }

    }


    public Map<TextureEnum, Integer> getTextures() {
        return textures;
    }
}
