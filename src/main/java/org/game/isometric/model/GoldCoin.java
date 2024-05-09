package org.game.isometric.model;

import org.game.isometric.texture2D.TextureEnum2D;

public class GoldCoin extends Stackable {

    private static final Integer maxOnStack = 20;
    @Override
    public Integer getTextureId(int quantity) {
        return switch (quantity) {
            case 1 -> TextureEnum2D.GOLD_COIN_1.getId();
            case 2 -> TextureEnum2D.GOLD_COIN_2.getId();
            case 3 -> TextureEnum2D.GOLD_COIN_3.getId();
            case 4 -> TextureEnum2D.GOLD_COIN_4.getId();
            case 5 -> TextureEnum2D.GOLD_COIN_5.getId();
            case 6 -> TextureEnum2D.GOLD_COIN_6.getId();
            case 7 -> TextureEnum2D.GOLD_COIN_7.getId();
            case 8 -> TextureEnum2D.GOLD_COIN_8.getId();
            case 9 -> TextureEnum2D.GOLD_COIN_9.getId();
            case 10 -> TextureEnum2D.GOLD_COIN_10.getId();
            case 11 -> TextureEnum2D.GOLD_COIN_11.getId();
            case 12 -> TextureEnum2D.GOLD_COIN_12.getId();
            case 13 -> TextureEnum2D.GOLD_COIN_13.getId();
            case 14 -> TextureEnum2D.GOLD_COIN_14.getId();
            case 15 -> TextureEnum2D.GOLD_COIN_15.getId();
            case 16 -> TextureEnum2D.GOLD_COIN_16.getId();
            case 17 -> TextureEnum2D.GOLD_COIN_17.getId();
            case 18 -> TextureEnum2D.GOLD_COIN_18.getId();
            case 19 -> TextureEnum2D.GOLD_COIN_19.getId();
            case 20 -> TextureEnum2D.GOLD_COIN_20.getId();
            default -> null;
        };
    }

    @Override
    public Integer getMaxOnStack() {
        return maxOnStack;
    }
}
