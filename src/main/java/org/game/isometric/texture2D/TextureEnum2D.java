package org.game.isometric.texture2D;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public enum TextureEnum2D implements ITexture {
    //2D textures
    GRASS2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "grass.png", null, GL_CLAMP_TO_EDGE, 1, 1, "grass.png", "GRASS_2D"),
    DIRT2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "dirt.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dirt.png", "DIRT_2D"),
    HAM_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + "ham2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "ham2D.png", "HAM_2D"),
    CUT_TREE_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + "cutTree2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "cutTree2D.png.png", "CUT_TREE_2D"),
    TREE_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "tree64.png", 1L, GL_CLAMP_TO_EDGE, 1, 1, "tree64.png", "TREE_2D"),
    HOLE_2D(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "holeDown2D.png", null, GL_CLAMP_TO_EDGE, 1, 1, "holeDown2D.png", "HOLE_2D"),
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
    PURPLE_BAR(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.purpleCoin + "purpleBar2D.png", 0L, GL_CLAMP_TO_EDGE, 1, 1, "purpleBar2D.png", "purpleBar"),

    //Blue Coin
    BLUE_COIN_1(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin2D.png", 1L, GL_CLAMP_TO_EDGE, 1, 1,  "blueCoin2D.png", "blueCoin"),
    BLUE_COIN_2(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin22D.png", 2L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin22D.png", "blueCoin"),
    BLUE_COIN_3(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin32D.png", 3L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin32D.png", "blueCoin"),
    BLUE_COIN_4(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin42D.png", 4L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin42D.png", "blueCoin"),
    BLUE_COIN_5(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + TextureEnum2D.blueCoin + "blueCoin52D.png", 5L, GL_CLAMP_TO_EDGE, 1, 1, "blueCoin52D.png", "blueCoin"),

    PLAYER_1(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\" + "player1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "player1.png", "player1"),
    PLAYER_2(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\" + "player2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "player2.png", "player2"),
    PLAYER_3(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\" + "player3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "player3.png", "player3"),
    PLAYER_4(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\" + "player4.png", null, GL_CLAMP_TO_EDGE, 1, 1, "player4.png", "player4"),
    PLAYER_5(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\" + "player5.png", null, GL_CLAMP_TO_EDGE, 1, 1, "player5.png", "player5"),
    PLAYER_6(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\" + "player6.png", null, GL_CLAMP_TO_EDGE, 1, 1, "player6.png", "player6"),

    TRACTOR_UP(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\" + "tractorUp.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_UP.png", "TRACTOR_UP"),
    TRACTOR_DOWN(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\" + "tractorDown.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_DOWN.png", "TRACTOR_DOWN"),
    TRACTOR_LEFT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\" + "tractorLeft.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_LEFT.png", "TRACTOR_LEFT"),
    TRACTOR_RIGHT(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\" + "tractorRight.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_RIGHT.png", "TRACTOR_RIGHT"),

    //Animation up
    TRACTOR_UP_1_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digUp\\" + "tractorUp1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_UP_1_ANIMATION.png", "TRACTOR_UP_1_ANIMATION"),
    TRACTOR_UP_2_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digUp\\" + "tractorUp2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_UP_2_ANIMATION.png", "TRACTOR_UP_2_ANIMATION"),
    TRACTOR_UP_3_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digUp\\" + "tractorUp3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_UP_3_ANIMATION.png", "TRACTOR_UP_3_ANIMATION"),

    //Animation down
    TRACTOR_DOWN_1_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digDown\\" + "tractorDown1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_DOWN_1_ANIMATION.png", "TRACTOR_DOWN_1_ANIMATION"),
    TRACTOR_DOWN_2_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digDown\\" + "tractorDown2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_DOWN_2_ANIMATION.png", "TRACTOR_DOWN_2_ANIMATION"),
    TRACTOR_DOWN_3_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digDown\\" + "tractorDown3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_DOWN_3_ANIMATION.png", "TRACTOR_DOWN_3_ANIMATION"),

    //Animation left
    TRACTOR_LEFT_1_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digLeft\\" + "tractorLeft1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_LEFT_1_ANIMATION.png", "TRACTOR_LEFT_1_ANIMATION"),
    TRACTOR_LEFT_2_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digLeft\\" + "tractorLeft2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_LEFT_2_ANIMATION.png", "TRACTOR_LEFT_2_ANIMATION"),
    TRACTOR_LEFT_3_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digLeft\\" + "tractorLeft3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_LEFT_3_ANIMATION.png", "TRACTOR_LEFT_3_ANIMATION"),

    //Animation right
    TRACTOR_RIGHT_1_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digRight\\" + "tractorRight1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_RIGHT_1_ANIMATION.png", "TRACTOR_RIGHT_1_ANIMATION"),
    TRACTOR_RIGHT_2_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digRight\\" + "tractorRight2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_RIGHT_2_ANIMATION.png", "TRACTOR_RIGHT_2_ANIMATION"),
    TRACTOR_RIGHT_3_ANIMATION(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "player\\tractor\\digRight\\" + "tractorRight3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "TRACTOR_RIGHT_3_ANIMATION.png", "TRACTOR_RIGHT_3_ANIMATION"),

    //Dynamite Animation
    DYNAMITE_ANIMATION_1 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation1.png", "DYNAMITE_ANIMATION_1"),
    DYNAMITE_ANIMATION_2 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation2.png", "DYNAMITE_ANIMATION_2"),
    DYNAMITE_ANIMATION_3 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation3.png", "DYNAMITE_ANIMATION_3"),
    DYNAMITE_ANIMATION_4 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation4.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation4.png", "DYNAMITE_ANIMATION_4"),
    DYNAMITE_ANIMATION_5 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation5.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation5.png", "DYNAMITE_ANIMATION_5"),
    DYNAMITE_ANIMATION_6 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation6.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation6.png", "DYNAMITE_ANIMATION_6"),
    DYNAMITE_ANIMATION_7 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation7.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation7.png", "DYNAMITE_ANIMATION_7"),
    DYNAMITE_ANIMATION_8 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation8.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation8.png", "DYNAMITE_ANIMATION_8"),
    DYNAMITE_ANIMATION_9 (org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation9.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation9.png", "DYNAMITE_ANIMATION_9"),
    DYNAMITE_ANIMATION_10(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\" + "dynamiteAnimation10.png", null, GL_CLAMP_TO_EDGE, 1, 1, "dynamiteAnimation10.png", "DYNAMITE_ANIMATION_10"),

    //Explosion animation
    EXPLOSION_ANIMATION_1(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation1.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation1.png", "EXPLOSION_ANIMATION_1"),
    EXPLOSION_ANIMATION_2(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation2.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation2.png", "EXPLOSION_ANIMATION_2"),
    EXPLOSION_ANIMATION_3(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation3.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation3.png", "EXPLOSION_ANIMATION_3"),
    EXPLOSION_ANIMATION_4(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation4.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation4.png", "EXPLOSION_ANIMATION_4"),
    EXPLOSION_ANIMATION_5(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation5.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation5.png", "EXPLOSION_ANIMATION_5"),
    EXPLOSION_ANIMATION_6(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation6.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation6.png", "EXPLOSION_ANIMATION_6"),
    EXPLOSION_ANIMATION_7(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation7.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation7.png", "EXPLOSION_ANIMATION_7"),
    EXPLOSION_ANIMATION_8(org.game.helper.IdGenerator.getNextIntegerId(), TextureEnum2D.root + TextureEnum2D.textures2D + "explosion\\explosionAnimation\\" + "explosionAnimation8.png", null, GL_CLAMP_TO_EDGE, 1, 1, "explosionAnimation8.png", "EXPLOSION_ANIMATION_8");

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
