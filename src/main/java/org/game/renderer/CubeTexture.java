package org.game.renderer;

import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

public enum CubeTexture {

    WOOD(CubeTexture.root + "wood.png", GL_MIRRORED_REPEAT);

    private final String path;
    private static final String root = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\textures\\";
    private final int param;

    CubeTexture(String path, int param) {
        this.path = path;
        this.param = param;
    }

    public String getPath() {
        return path;
    }

    public int getParam() {
        return param;
    }
}
