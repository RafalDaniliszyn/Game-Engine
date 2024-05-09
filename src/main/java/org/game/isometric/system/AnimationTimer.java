package org.game.isometric.system;

public class AnimationTimer {
    private float animationTimer;
    private float duration;
    private boolean active;

    public AnimationTimer(float duration) {
        this.duration = duration;
    }

    public void increment(float time) {
        this.animationTimer += time;
    }

    public float getAnimationTimer() {
        return animationTimer;
    }

    public void setAnimationTimer(float animationTimer) {
        this.animationTimer = animationTimer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
