package org.game.math;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {
    public static Matrix4f transformation(Vector3f scale, Vector3f position, float rotationX, float rotationY, float rotationZ) {
        Matrix4f transform = new Matrix4f();
        transform.identity()
                .translate(position)
                .rotateX(Math.toRadians(rotationX))
                .rotateY(Math.toRadians(rotationY))
                .rotateZ(Math.toRadians(rotationZ))
                .scale(scale);
        return transform;
    }

    public static Matrix4f transformation(Vector3f scale, Vector3f position) {
        Matrix4f transform = new Matrix4f();
        transform.identity()
                .translate(position)
                .scale(scale);
        return transform;
    }

    /**
     * This method checks whether objects overlap.
     *
     * @param x1 X position
     * @param y1 Y position
     * @param x2 X position of the element containing width and height.
     * @param y2 Y position of the element containing width and height.
     * @param width2 WIDTH of element associated with x2 and y2 variable.
     * @param height2 HEIGHT of element associated with x2 and y2 variable.
     * @return true if overlap or else false.
     */
    public static boolean overlap(double x1, double y1, double x2, double y2, double width2, double height2) {
        return x1 > x2 && x1 < x2 + width2 && y1 > y2 && y1 < y2 + height2;
    }
}
