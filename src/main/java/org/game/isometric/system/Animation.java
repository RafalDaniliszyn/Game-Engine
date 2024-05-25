package org.game.isometric.system;

import java.util.Map;

public class Animation {
    private AnimationTimer animationTimer;
    private Map<Integer, Integer> textures;

    public Animation(Map<Integer, Integer> textures, float duration) {
        this.textures = textures;
        this.animationTimer = new AnimationTimer(duration);
    }
}
