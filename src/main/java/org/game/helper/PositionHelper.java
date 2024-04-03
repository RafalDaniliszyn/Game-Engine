package org.game.helper;

import org.game.GraphicsDisplay;
import org.joml.Interpolationf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;

public class PositionHelper {

    public static Vector2f getScreenCoordinates(double x, double y, double height, double width) {
        float resultX = (float) (((x+1) * (GraphicsDisplay.WIDTH / 2.0)) - (width/2));
        float resultY = (float) (((GraphicsDisplay.HEIGHT - ((y+1) * (GraphicsDisplay.HEIGHT / 2.0))) - (height/2)) - 25.0f);
        return new Vector2f(resultX, resultY);
    }

    public static Vector2f getClipCoordinates(double x, double y) {
        float resultX = (float) (x / (WIDTH * 0.5f)) -1.0f;
        float resultY = (float) ((y / (HEIGHT * 0.5f)) - 1.0f) * -1.0f;
        return new Vector2f(resultX, resultY);
    }

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
