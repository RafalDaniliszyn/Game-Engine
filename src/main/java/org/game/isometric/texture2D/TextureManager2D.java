package org.game.isometric.texture2D;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.event.EventHandler;
import org.game.isometric.event.EventPublisher;
import org.game.isometric.event.LoadTextureEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
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
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureManager2D {

    private static final Map<TextureEnum2D, Integer> textures;
    private static final Map<Integer, TextureEnum2D> texturesById;
    private static final Map<String, Integer> textureIdByLabel;
    private static final Map<Integer, String> texturePathRoot;

    // TODO: 5/30/2024 to remove
    private static final Map<Integer, String> textureLabelById;
    static {
        textures = new HashMap<>();
        texturesById = new HashMap<>();
        textureIdByLabel = new HashMap<>();
        textureLabelById = new HashMap<>();
        texturePathRoot = new HashMap<>();
//        EventPublisher.getInstance().addListener(LoadTextureEvent.class, new EventHandler<LoadTextureEvent>() {
//            @Override
//            public void handleEvent(LoadTextureEvent event) {
//                Integer textureId = loadTexture(event.getPath(), "");
//                String entityLabel = event.getEntityLabel();
//                Entity entity = BlocksReader.getEntity(entityLabel);
//                if (entity != null) {
//                    entity.getProperties().getRotatedEntityIdMap().put(event.getRotation(), textureId);
//                }
//            }
//        });
    }

    public static Integer loadTexture(String path, int[] getWidth, int[] getHeight) {
        STBImage.stbi_set_flip_vertically_on_load(true);

        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];
        ByteBuffer data = loadImage(path, width, height, channels);
        getWidth[0] = width[0];
        getHeight[0] = height[0];

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        //glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(data);
        if (!texturePathRoot.containsKey(texID)) {
            texturePathRoot.put(texID, path);
        }
        return texID;
    }

    public static Integer loadTexture(String path, String label) {
        STBImage.stbi_set_flip_vertically_on_load(true);

        int texID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];
        ByteBuffer data = loadImage(path, width, height, channels);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        //glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(data);
        if (!texturePathRoot.containsKey(texID)) {
            texturePathRoot.put(texID, path);
        }
        return texID;
    }

    public static void loadTextures() {
        TextureEnum2D[] values = TextureEnum2D.values();
        int texCount = values.length;
        for (int i = 0; i < texCount; i++) {
            String path = values[i].getPath();
            STBImage.stbi_set_flip_vertically_on_load(values[i].getFlip() == 1);

            int texID = glGenTextures();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            int[] width = new int[1];
            int[] height = new int[1];
            int[] channels = new int[1];
            ByteBuffer data = loadImage(path, width, height, channels);
            boolean alpha = values[i].getAlpha() == 1;
            int internalFormat = alpha ? GL_RGBA : GL_RGB;
            glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width[0], height[0], 0, internalFormat, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(data);

            textures.put(values[i], texID);
            texturesById.put(texID, values[i]);
            textureIdByLabel.put(values[i].getLabel(), texID);
            textureLabelById.put(texID, values[i].getLabel());
        }
    }

    public static ByteBuffer loadImage(String path, int[] width, int[] height, int[] channels) {
        ByteBuffer imageBuffer;

        try (InputStream resourceAsStream = TextureManager2D.class.getResourceAsStream(path)) {
            byte[] bytes;
            if (resourceAsStream != null) {
                bytes = resourceAsStream.readAllBytes();
            } else {
                Path filePath = Path.of(path);
                bytes = Files.readAllBytes(filePath);
            }
            imageBuffer = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            imageBuffer.flip();
        } catch (IOException e) {
            throw new RuntimeException("Failed: " + path, e);
        }

        ByteBuffer image = null;
        try {
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer c = BufferUtils.createIntBuffer(1);

            image = STBImage.stbi_load_from_memory(imageBuffer, w, h, c, 0);
            if (image == null) {
                throw new RuntimeException("Failed: " + STBImage.stbi_failure_reason());
            }

            width[0] = w.get(0);
            height[0] = h.get(0);
            channels[0] = c.get(0);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return image;
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

    public static String getTexturePath(Integer textureId) {
        return texturePathRoot.get(textureId);
    }

}
