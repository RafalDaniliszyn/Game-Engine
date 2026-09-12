package org.game.isometric;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera2D {
    public static Vector2f cameraPosition = new Vector2f(0.0f, 0.0f);
    private static Matrix4f viewMatrix;

    public static Matrix4f getView() {
        if (viewMatrix == null) {
            viewMatrix = new Matrix4f();
        }
        Vector2f cameraPos = cameraPosition;
        viewMatrix.identity();
        long time = System.currentTimeMillis();
        //viewMatrix.rotateY((float) Math.toRadians(-70.0));
        //viewMatrix.rotateX((float) Math.toRadians(-10.0));

        //viewMatrix.translate(-cameraPos.x, -cameraPos.y, -500.0f);
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, 0.0f);
        return viewMatrix;
    }


    public static Vector2f getCameraPosition() {
        return cameraPosition;
    }

    public static void setCameraPosition(Vector2f cameraPosition) {
        Camera2D.cameraPosition = cameraPosition;
    }

    public static Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public static void setViewMatrix(Matrix4f viewMatrix) {
        Camera2D.viewMatrix = viewMatrix;
    }
}
