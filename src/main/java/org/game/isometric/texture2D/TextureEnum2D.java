package org.game.isometric.texture2D;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public enum TextureEnum2D implements ITexture {
    //2D textures
    GRASS2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "grass.png", null, GL_CLAMP_TO_EDGE, 1, 1, "grass.png", "GRASS_2D"),
    DIRT2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "dirt.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dirt.png", "DIRT_2D"),
    HAM_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + "ham2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "ham2D.png", "HAM_2D"),
    CUT_TREE_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + "cutTree2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "cutTree2D.png.png", "CUT_TREE_2D"),
    TREE_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "tree64.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "tree64.png", "TREE_2D"),
    HOLE_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "hole2D.png", null, GL_CLAMP_TO_EDGE, 1, 1, "hole2D.png", "HOLE_2D"),
    //Gold Coins
    GOLD_COIN_1(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin2D.png",  1L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin2D.png", "goldCoin"),
    GOLD_COIN_2(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin22D.png", 2L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin22D.png", "goldCoin"),
    GOLD_COIN_3(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin32D.png", 3L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin32D.png", "goldCoin"),
    GOLD_COIN_4(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin42D.png", 4L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin42D.png", "goldCoin"),
    GOLD_COIN_5(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin52D.png", 5L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin52D.png", "goldCoin"),
    GOLD_COIN_6(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin62D.png", 6L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin62D.png", "goldCoin"),
    GOLD_COIN_7(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin72D.png", 7L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin72D.png", "goldCoin"),
    GOLD_COIN_8(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin82D.png", 8L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin82D.png", "goldCoin"),
    GOLD_COIN_9(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin92D.png", 9L, GL_CLAMP_TO_EDGE, 1, 1,  "goldCoin92D.png", "goldCoin"),
    GOLD_COIN_10(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin102D.png", 10L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin102D.png", "goldCoin"),
    GOLD_COIN_11(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin112D.png", 11L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin112D.png", "goldCoin"),
    GOLD_COIN_12(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin122D.png", 12L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin122D.png", "goldCoin"),
    GOLD_COIN_13(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin132D.png", 13L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin132D.png", "goldCoin"),
    GOLD_COIN_14(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin142D.png", 14L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin142D.png", "goldCoin"),
    GOLD_COIN_15(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin152D.png", 15L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin152D.png", "goldCoin"),
    GOLD_COIN_16(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin162D.png", 16L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin162D.png", "goldCoin"),
    GOLD_COIN_17(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin172D.png", 17L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin172D.png", "goldCoin"),
    GOLD_COIN_18(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin182D.png", 18L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin182D.png", "goldCoin"),
    GOLD_COIN_19(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin192D.png", 19L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin192D.png", "goldCoin"),
    GOLD_COIN_20(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.goldCoin + "goldCoin202D.png", 20L, GL_CLAMP_TO_EDGE, 1, 1, "goldCoin202D.png", "goldCoin"),
    GOLD_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "gold2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "gold2D.png", "GOLD_2D"),

    //Purple Coin
    PURPLE_COIN_1(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleCoin2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1,  "purpleCoin2D.png", "purpleCoin"),
    PURPLE_COIN_2(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleCoin22D.png", 2L, GL_CLAMP_TO_EDGE, 1, 1, "purpleCoin22D.png", "purpleCoin"),
    PURPLE_COIN_3(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleCoin32D.png", 3L, GL_CLAMP_TO_EDGE, 1, 1, "purpleCoin32D.png", "purpleCoin"),
    PURPLE_COIN_4(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleCoin42D.png", 4L, GL_CLAMP_TO_EDGE, 1, 1, "purpleCoin42D.png", "purpleCoin"),
    PURPLE_COIN_5(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleCoin52D.png", 5L, GL_CLAMP_TO_EDGE, 1, 1, "purpleCoin52D.png", "purpleCoin"),

    //Purple Bar
    PURPLE_BAR(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleBar2D.png", 0L, GL_CLAMP_TO_EDGE, 1, 1, "purpleBar2D.png", "purpleCoin"),

    //Blue Coin
    BLUE_COIN_1(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1,  "blueCoin2D.png", "blueCoin"),
    BLUE_COIN_2(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin22D.png", 2L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin22D.png", "blueCoin"),
    BLUE_COIN_3(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin32D.png", 3L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin32D.png", "blueCoin"),
    BLUE_COIN_4(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin42D.png", 4L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin42D.png", "blueCoin"),
    BLUE_COIN_5(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin52D.png", 5L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin52D.png", "blueCoin"),

    //Water
    WATER(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "water.png", null, GL_CLAMP_TO_EDGE, 1, 1, "water.png", "WATER_2D"),
    WATER_UP(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterUpGround.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterUpGround.png", "WATER_UP_2D"),
    WATER_DOWN(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterDownGround.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterDownGround.png", "WATER_DOWN_2D"),
    WATER_LEFT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterLeftGround.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterLeftGround.png", "WATER_LEFT_2D"),
    WATER_RIGHT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterRightGround.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterRightGround.png", "WATER_RIGHT_2D"),

    WATER_UP_RIGHT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterUpRight.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterUpRight.png", "WATER_UP_RIGHT_2D"),
    WATER_UP_LEFT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterUpLeft.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterUpLeft.png", "WATER_UP_LEFT_2D"),
    WATER_DOWN_RIGHT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterDownRight.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterDownRight.png", "WATER_DOWN_RIGHT_2D"),
    WATER_DOWN_LEFT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.water + "waterDownLeft.png", null, GL_CLAMP_TO_EDGE, 1, 1, "waterDownLeft.png", "WATER_DOWN_LEFT_2D");

    private final int id;
    private final String path;
    private static final String root = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\textures\\";
    private static final String textures2D = "2D\\";
    private static final String goldCoin = "goldCoin\\";
    private static final String purpleCoin = "purpleCoin\\";
    private static final String blueCoin = "blueCoin\\";
    private static final String water = "water\\";
    private final Long quantity;
    private final int param;
    private final int flip;
    private final int alpha;
    private final String name;
    private final String label;

    @Override
    public String getPath() {
        return path;
    }

    public int getParam() {
        return param;
    }

    @Override
    public int getFlip() {
        return flip;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public int getAlpha() {
        return alpha;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public Long getQuantity() {
        return quantity;
    }

    TextureEnum2D(int id, String path, Long quantity, int param, int flip, int alpha, String name, String label) {
        this.id = id;
        this.path = path;
        this.quantity = quantity;
        this.param = param;
        this.flip = flip;
        this.alpha = alpha;
        this.name = name;
        this.label = label;
    }
}
