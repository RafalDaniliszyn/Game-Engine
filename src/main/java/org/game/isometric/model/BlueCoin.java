package org.game.isometric.model;

import org.game.isometric.texture2D.TextureEnum2D;

public class BlueCoin extends Stackable {

    private static final Integer maxOnStack = 5;
    @Override
    public Integer getTextureId(int quantity) {
        return switch (quantity) {
            case 1 -> TextureEnum2D.BLUE_COIN_1.getId();
            case 2 -> TextureEnum2D.BLUE_COIN_2.getId();
            case 3 -> TextureEnum2D.BLUE_COIN_3.getId();
            case 4 -> TextureEnum2D.BLUE_COIN_4.getId();
            case 5 -> TextureEnum2D.BLUE_COIN_5.getId();
            default -> null;
        };
    }

    @Override
    public Integer getMaxOnStack() {
        return maxOnStack;
    }
}
