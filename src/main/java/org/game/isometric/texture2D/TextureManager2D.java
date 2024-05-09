package org.game.isometric.texture2D;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureManager2D {

    private static final Map<TextureEnum2D, Integer> textures;
    private static final Map<Integer, TextureEnum2D> texturesById;
    private static final Map<String, Integer> textureIdByLabel;
    static {
        textures = new HashMap<>();
        texturesById = new HashMap<>();
        textureIdByLabel= new HashMap<>();
    }

    public static Integer loadTexture(String path) {
        STBImage.stbi_set_flip_vertically_on_load(true);

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

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(), y.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(data);
        return texID;
    }

    public static void loadTextures() {
        TextureEnum2D[] values = TextureEnum2D.values();
        int texCount = values.length;
        for (int i = 0; i < texCount; i++) {
            String path = values[i].getPath();
            STBImage.stbi_set_flip_vertically_on_load(values[i].getFlip() == 1);

            int texID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
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

            textures.put(values[i], texID);
            texturesById.put(texID, values[i]);
            textureIdByLabel.put(values[i].getLabel(), texID);
        }
    }

    public static Integer getTextureId(TextureEnum2D textureEnum2D) {
        return textures.get(textureEnum2D);
    }

    public static TextureEnum2D getTextureById(Integer id) {
        return texturesById.get(id);
    }

    public static Integer getTextureIdByLabel(String label) {
        return textureIdByLabel.get(label);
    }

    public static Map<TextureEnum2D, Integer> getTextures() {
        return textures;
    }

}
