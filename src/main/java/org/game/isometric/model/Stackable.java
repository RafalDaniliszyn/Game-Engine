package org.game.isometric.model;

import org.game.isometric.texture2D.TextureEnum2D;
import java.util.HashMap;
import java.util.Map;

public abstract class Stackable {
    protected Map<Long, TextureEnum2D> textureMap;

    public Stackable() {
        textureMap = new HashMap<>();
    }

    public abstract Integer getTextureId(int quantity);
    public abstract Integer getMaxOnStack();
}
