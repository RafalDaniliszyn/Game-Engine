package org.game.isometric.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class MathUtils {

    private MathUtils() {}

    public static Matrix4f transformation2D(Vector2f scale, Vector2f position) {
        Vector3f scale3f = new Vector3f(scale, 1.0f);
        Vector3f position3f = new Vector3f(position, 0.0f);
        Matrix4f transform = new Matrix4f();
        transform.identity()
                .translate(position3f)
                .scale(scale3f);
        return transform;
    }
}
