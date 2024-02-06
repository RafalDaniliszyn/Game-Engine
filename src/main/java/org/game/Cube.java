package org.game;

import org.game.renderer.Model;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cube {

    public static Model getCube(Vector3f position, float color, Vector3f scale, float size) {
        Model model = new Model(
                getVertices(color, size),
                getIndices(),
                position,
                0.0f,
                scale);
        return model;
    }

    public static Map<Side, Model> getCubeParts(Vector3f position, float color, Vector3f scale, float size) {
        float[] vertices = getVertices(color, size);
        float[] front = Arrays.copyOfRange(vertices, 0, 36);
        float[] left = Arrays.copyOfRange(vertices, 36, 72);
        float[] right = Arrays.copyOfRange(vertices, 72, 108);
        float[] back = Arrays.copyOfRange(vertices, 108, 144);
        float[] top = Arrays.copyOfRange(vertices, 144, 180);
        float[] bottom = Arrays.copyOfRange(vertices, 180, 216);

        Map<Side, Model> modelMap = new HashMap<>();
        //FRONT
        Model modelFront = new Model(
                front,
                new int[]{
                        0, 1, 2,
                        3, 2, 0
                },
                position,
                0.0f,
                scale
        );
        modelMap.put(Side.FRONT, modelFront);

        //LEFT
        Model modelLeft = new Model(
                left,
                new int[]{
                        0, 1, 3,
                        2, 3, 0
                },
                position,
                0.0f,
                scale
        );
        modelMap.put(Side.LEFT, modelLeft);

        //RIGHT
        Model modelRight = new Model(
                right,
                new int[]{
                        0, 1, 3,
                        2, 3, 0
                },
                position,
                0.0f,
                scale
        );
        modelMap.put(Side.RIGHT, modelRight);

        //BACK
        Model modelBack = new Model(
                back,
                new int[]{
                        0, 1, 2,
                        3, 2, 0
                },
                position,
                0.0f,
                scale
        );
        modelMap.put(Side.BACK, modelBack);

        //TOP
        Model modelTop = new Model(
                top,
                new int[]{
                        0, 1, 2,
                        3, 2, 0
                },
                position,
                0.0f,
                scale
        );
        modelMap.put(Side.TOP, modelTop);

        //BOTTOM
        Model modelBottom = new Model(
                bottom,
                new int[]{
                        0, 1, 2,
                        3, 2, 0
                },
                position,
                0.0f,
                scale
        );
        modelMap.put(Side.BOTTOM, modelBottom);
        return modelMap;
    }

    private static float[] getVertices(float color, float size) {
        float sc = 0.7f;
        float vertices[] = {
                //back
                -1.0f,  1.0f, -1.0f,     sc, sc, sc, 1.0f,   0.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,     sc, sc, sc, 1.0f,   0.0f, 0.0f,
                1.0f, -1.0f, -1.0f,     sc, sc, sc, 1.0f,    1.0f, 0.0f,
                1.0f,  1.0f, -1.0f,     sc, sc, sc, 1.0f,    1.0f, 1.0f,

                //left
                -1.0f,  1.0f,  1.0f,   sc, sc, sc, 1.0f,   0.0f, 1.0f,
                -1.0f, -1.0f,  1.0f,   sc, sc, sc, 1.0f,   0.0f, 0.0f,
                -1.0f, -1.0f, -1.0f,   sc, sc, sc, 1.0f,   1.0f, 0.0f,
                -1.0f,  1.0f, -1.0f,   sc, sc, sc, 1.0f,   1.0f, 1.0f,

                //right
                1.0f,  1.0f,  1.0f,    sc, sc, sc, 1.0f,   0.0f, 1.0f,
                1.0f, -1.0f,  1.0f,    sc, sc, sc, 1.0f,   0.0f, 0.0f,
                1.0f, -1.0f, -1.0f,    sc, sc, sc, 1.0f,   1.0f, 0.0f,
                1.0f,  1.0f, -1.0f,    sc, sc, sc, 1.0f,   1.0f, 1.0f,

                //front
                1.0f,  1.0f,  1.0f,    sc, sc, sc, 1.0f,   0.0f, 1.0f,
                1.0f, -1.0f,  1.0f,    sc, sc, sc, 1.0f,   0.0f, 0.0f,
               -1.0f, -1.0f,  1.0f,    sc, sc, sc, 1.0f,   1.0f, 0.0f,
               -1.0f,  1.0f,  1.0f,    sc, sc, sc, 1.0f,   1.0f, 1.0f,

                //top
               -1.0f,  1.0f,  1.0f,   sc, sc, sc, 1.0f,   0.0f, 1.0f,
               -1.0f,  1.0f, -1.0f,   sc, sc, sc, 1.0f,   0.0f, 0.0f,
                1.0f,  1.0f, -1.0f,   sc, sc, sc, 1.0f,   1.0f, 0.0f,
                1.0f,  1.0f,  1.0f,   sc, sc, sc, 1.0f,   1.0f, 1.0f,

                //bottom
               -1.0f, -1.0f, -1.0f,    sc, sc, sc, 1.0f,   0.0f, 1.0f,
               -1.0f, -1.0f,  1.0f,    sc, sc, sc, 1.0f,   0.0f, 0.0f,
                1.0f, -1.0f,  1.0f,    sc, sc, sc, 1.0f,   1.0f, 0.0f,
                1.0f, -1.0f, -1.0f,    sc, sc, sc, 1.0f,   1.0f, 1.0f
        };
        return vertices;
    }

    private static int[] getIndices() {
        int[] indices = {
                0, 1, 2,
                3, 2, 0,

                4, 5, 6,
                7, 6, 4,

                8, 9, 10,
                11, 10, 8,

                12, 13, 14,
                15, 14, 12,

                16, 17, 18,
                19, 18, 16,

                20, 21, 22,
                23, 22, 20
        };
        return indices;
    }

    public enum Side{
        LEFT, RIGHT, TOP, BOTTOM, FRONT, BACK;
    }

}
