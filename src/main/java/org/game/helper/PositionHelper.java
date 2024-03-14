package org.game.helper;

import org.joml.Interpolationf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class PositionHelper {

    public static Vector3f getNearestPositionY(float[] vert, float x, float z) {
        List<Vector3f> vertList = getXyzPositionList(vert);
        Vector3f nearest = vertList.get(0);
        Vector2f toCompare = new Vector2f(nearest.x, nearest.z);
        float distance = toCompare.distance(x, z);

        for (int i = 0; i < vertList.size(); i++) {
            toCompare = new Vector2f(vertList.get(i).x, vertList.get(i).z);

            if (toCompare.distance(x, z) < distance) {
                nearest = vertList.get(i);
                distance = toCompare.distance(x, z);
            }
        }
        return nearest;
    }

    public static List<Vector3f> getXyzPositionList(float[] vertices) {
        int STRIDE = 12;
        List<Vector3f> vertList = new ArrayList<>();
        for (int i = 0; i < vertices.length; i+=STRIDE) {
            vertList.add(new Vector3f(vertices[i], vertices[i+1], vertices[i+2]));
        }
        return vertList;
    }
    public static float getPositionY(float[][] heightMap, float x, float z, float xSide, float zSide) {
        int x1 = (int) (Math.floor(x / xSide));
        int z1 = (int) (Math.floor(z / zSide));
        float v1 = heightMap[x1][z1];
        float v2 = heightMap[x1 + 1][z1];
        float v3 = heightMap[x1][z1+1];
        return Interpolationf.interpolateTriangle(0, 0, v1, xSide, 0, v2, 0, zSide, v3,x % xSide,z % zSide);
    }
    public static float getPositionY(float[] vert, float x, float z) {
        List<Vector3f> vertList = getXyzPositionList(vert);
        Vector3f nearest = vertList.get(0);
        Vector2f toCompare = new Vector2f(nearest.x, nearest.z);
        float distance = toCompare.distance(x, z);

        for (int i = 0; i < vertList.size(); i++) {
            toCompare = new Vector2f(vertList.get(i).x, vertList.get(i).z);

            if (toCompare.distance(x, z) < distance) {
                nearest = vertList.get(i);
                distance = toCompare.distance(x, z);
            }
        }
        return nearest.y;
    }

}
