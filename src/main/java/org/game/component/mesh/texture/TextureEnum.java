package org.game.component.mesh.texture;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

public enum TextureEnum {
    GRASS(TextureEnum.root + "grass.png", GL_MIRRORED_REPEAT, 0, 0, "grass.png"),
    GRASS_COLOR(TextureEnum.root + "grassColor.png", GL_MIRRORED_REPEAT, 1, 0, "grassColor.png"),
    STONES(TextureEnum.root + "stones.png", GL_REPEAT, 0, 0, "stones.png"),
    WOOD(TextureEnum.root + "wall.png", GL_MIRRORED_REPEAT, 0, 0, "name"),
    DRY_GRASS(TextureEnum.root + "trawa.png", GL_REPEAT, 1, 1, "trawa.png"),
    TREES(TextureEnum.root + "forestt.png", GL_MIRRORED_REPEAT, 0, 0, "forestt.png"),
    OAK_LEAF(TextureEnum.root + "leaf2.png", GL_MIRRORED_REPEAT, 1, 1, "leaf2.png"),
    TREE_COLORS(TextureEnum.root + "tree.png", GL_MIRRORED_REPEAT, 1, 1, "tree.png"),
    TOWER(TextureEnum.root + "tower.png", GL_CLAMP_TO_EDGE, 1, 1, "tower.png"),
    MEDIUM_HOUSE(TextureEnum.root + "mediumhouse.png", GL_CLAMP_TO_EDGE, 1, 1, "mediumhouse.png"),
    MEDIUM_HOUSE_2(TextureEnum.root + "mediumhouse2.png", GL_CLAMP_TO_EDGE, 1, 1, "mediumhouse2.png"),
    BIG_HOUSE(TextureEnum.root + "bighouse.png", GL_CLAMP_TO_EDGE, 1, 1, "bighouse.png"),
    PLAYER(TextureEnum.root + "player.png", GL_CLAMP_TO_EDGE, 1, 0, "player.png"),
    FLOWER(TextureEnum.root + "colors.png", GL_CLAMP_TO_EDGE, 1, 1, "colors.png"),
    BASE_COLOR(TextureEnum.root + "baseColor.png", GL_CLAMP_TO_EDGE, 0, 0, "baseColor.png"),
    FENCE_PALETTE(TextureEnum.root + "palette.png", GL_CLAMP_TO_EDGE, 1, 0, "palette.png"),
    SKY(TextureEnum.root + "background\\sky.png", GL_CLAMP_TO_EDGE, 1, 1, "sky.png"),
    GROUND(TextureEnum.root + "ground.png", GL_CLAMP_TO_EDGE, 1, 1, "ground.png"),
    GROUND_2(TextureEnum.root + "ground2.png", GL_CLAMP_TO_EDGE, 1, 1, "ground2.png"),
    WATER(TextureEnum.root + "water.png", GL_CLAMP_TO_EDGE, 1, 1, "water.png"),
    BASE_STONE(TextureEnum.root + "baseStone.png", GL_CLAMP_TO_EDGE, 1, 1, "baseStone.png"),
    BUTTON(TextureEnum.root + "button.png", GL_CLAMP_TO_EDGE, 1, 1, "button.png"),
    OLD_HOUSE(TextureEnum.root + "oldhouse.png", GL_CLAMP_TO_EDGE, 1, 1, "oldhouse.png"),
    PALACE(TextureEnum.root + "test.png", GL_CLAMP_TO_EDGE, 1, 0, "test.png"),
    SUN(TextureEnum.root + "sun.png", GL_CLAMP_TO_EDGE, 1, 1, "sun.png"),
    STONE_GROUP(TextureEnum.root + "grey.png", GL_CLAMP_TO_EDGE, 0, 0, "grey.png");


    private final String path;
    private static final String root = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\textures\\";
    private final int param;
    private final int flip;
    private final int alpha;
    private final String name;

    TextureEnum(String path, int param, int flip, int alpha, String name) {
        this.path = path;
        this.param = param;
        this.flip = flip;
        this.alpha = alpha;
        this.name = name;
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

    public String getName() {
        return name;
    }
}