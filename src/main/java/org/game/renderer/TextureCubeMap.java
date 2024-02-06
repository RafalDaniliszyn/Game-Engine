package org.game.renderer;

import java.util.Arrays;
import java.util.List;

public enum TextureCubeMap {

    SKY(Arrays.asList(
            TextureEnum.SKY_RIGHT,
            TextureEnum.SKY_LEFT,
            TextureEnum.SKY_TOP,
            TextureEnum.SKY_BOTTOM,
            TextureEnum.SKY_FRONT,
            TextureEnum.SKY_BACK


    ));
    private final List<TextureEnum> textureList;

    TextureCubeMap(List<TextureEnum> textureList) {
            this.textureList = textureList;
    }

    public List<TextureEnum> getTextureList() {
        return textureList;
    }
}
