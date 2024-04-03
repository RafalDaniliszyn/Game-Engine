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
}
