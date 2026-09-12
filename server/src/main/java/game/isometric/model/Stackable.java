package game.isometric.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Stackable {
    protected Map<Long, String> textureMap;

    public Stackable() {
        textureMap = new HashMap<>();
    }

    public abstract Integer getTextureId(int quantity);
    public abstract Integer getMaxOnStack();
}
