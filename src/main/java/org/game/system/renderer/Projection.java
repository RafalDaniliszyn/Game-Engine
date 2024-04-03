package org.game.system.renderer;

import org.joml.Math;
import org.joml.Matrix4f;

import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;

public class Projection {

    public static Matrix4f getPerspectiveProjection(float near, float far, float fov) {
        float windowAspect = (float) WIDTH / (float) HEIGHT;
        Matrix4f projection = new Matrix4f();
        projection.perspective(Math.toRadians(fov), windowAspect, near, far);
        return projection;
    }

    public static Matrix4f get2DProjection() {
        float windowAspect = (float) WIDTH / (float) HEIGHT;
        Matrix4f projection = new Matrix4f();
        projection.ortho2D(-1, 1, -1, 1);
        return projection;
    }
}
