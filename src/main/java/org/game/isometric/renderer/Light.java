package org.game.isometric.renderer;

public class Light {
    public float x;
    public float y;

    public float radius;
    public float intensity;

    public float r;
    public float g;
    public float b;

    public Light(float x, float y, float radius, float intensity, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.intensity = intensity;
        this.r = r;
        this.g = g;
        this.b = b;
    }

}

