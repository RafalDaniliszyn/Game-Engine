package org.game.renderer;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

public enum TextureEnum {
    GRASS(TextureEnum.root + "grass.png", GL_MIRRORED_REPEAT, 0, 0),
    STONES(TextureEnum.root + "stones.png", GL_REPEAT, 0, 0),
    WOOD(TextureEnum.root + "wall.png", GL_MIRRORED_REPEAT, 0, 0),
    SKY_TOP(TextureEnum.root + "background\\sunny\\top.bmp", GL_CLAMP_TO_EDGE, 0, 0),
    SKY_LEFT(TextureEnum.root + "background\\sunny\\left.bmp", GL_CLAMP_TO_EDGE, 0, 0),
    SKY_RIGHT(TextureEnum.root + "background\\sunny\\right.bmp", GL_CLAMP_TO_EDGE, 0, 0),
    SKY_FRONT(TextureEnum.root + "background\\sunny\\front.bmp", GL_CLAMP_TO_EDGE, 0, 0),
    SKY_BACK(TextureEnum.root + "background\\sunny\\back.bmp", GL_CLAMP_TO_EDGE, 0, 0),
    SKY_BOTTOM(TextureEnum.root + "background\\sunny\\bottom.bmp", GL_CLAMP_TO_EDGE, 0, 0),
    DRY_GRASS(TextureEnum.root + "trawa.png", GL_REPEAT, 1, 1),
    TREES(TextureEnum.root + "forestt.png", GL_MIRRORED_REPEAT, 0, 0),
    TREE_COLORS(TextureEnum.root + "tree.png", GL_MIRRORED_REPEAT, 1, 1),
    TOWER(TextureEnum.root + "tower.png", GL_CLAMP_TO_EDGE, 1, 1),
    MEDIUM_HOUSE(TextureEnum.root + "mediumhouse.png", GL_CLAMP_TO_EDGE, 1, 1),
    MEDIUM_HOUSE_2(TextureEnum.root + "mediumhouse2.png", GL_CLAMP_TO_EDGE, 1, 1),
    BIG_HOUSE(TextureEnum.root + "bighouse.png", GL_CLAMP_TO_EDGE, 1, 1),
    PLAYER(TextureEnum.root + "ConeBaseColor.png", GL_CLAMP_TO_EDGE, 1, 1);


    private final String path;
    private static final String root = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\textures\\";
    private final int param;
    private final int flip;
    private final int alpha;

    TextureEnum(String path, int param, int flip, int alpha) {
        this.path = path;
        this.param = param;
        this.flip = flip;
        this.alpha = alpha;
    }

    public String getPath() {
        return path;
    }

    public int getParam() {
        return param;
    }

    public int getFlip() {
        return flip;
    }

    public int getAlpha() {
        return alpha;
    }
}