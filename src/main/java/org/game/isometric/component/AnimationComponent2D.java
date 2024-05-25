package org.game.isometric.component;

import org.game.component.Component;
import java.util.Map;

public class AnimationComponent2D extends Component {
    private final Map<Integer, Integer> textures;
    private int currentFrame;
    private boolean active;
    private final int frames;
    private final double frameDuration;
    private double elapsedTime;
    private double lastUpdateTime;
    private final String label;

    public AnimationComponent2D(Map<Integer, Integer> textures, float animationDuration, String label) {
        this.textures = textures;
        this.currentFrame = 1;
        this.active = false;
        this.frames = textures.size();
        this.frameDuration = (double) animationDuration / this.frames;
        this.elapsedTime = 0;
        this.lastUpdateTime = 0;
        this.label = label;
    }

    public int nextFrame() {
        double currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - lastUpdateTime;
        elapsedTime += deltaTime;
        lastUpdateTime = currentTime;

        if (currentFrame >= frames) {
            currentFrame = 1;
            lastUpdateTime = 0;
            active = false;
        }

        if (active && elapsedTime >= frameDuration) {
            Integer textureId = textures.get(currentFrame);
            currentFrame += 1;
            elapsedTime = 0;
            return textureId;
        }
        return textures.get(currentFrame);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public ComponentEnum getType() {
        return ComponentEnum.AnimationComponent2D;
    }
}
