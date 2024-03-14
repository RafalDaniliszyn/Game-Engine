package org.game.helper;

import org.joml.Vector3f;
import java.util.List;

public class MapHelper {
    public static float X_SIDE = 1.0f;
    public static float Z_SIDE = 1.0f;
    public static float[][] getHeightMap(float[] vertices) {
        List<Vector3f> verticesList = PositionHelper.getXyzPositionList(vertices);
        int size = (int) Math.sqrt(verticesList.size());
        float highestX = verticesList.get(0).x;
        float highestZ = verticesList.get(0).z;

        float lowestX = verticesList.get(0).x;
        float lowestZ = verticesList.get(0).z;

        for (int i = 0; i < verticesList.size(); i++) {
            Vector3f vertex = verticesList.get(i);
            if (vertex.x > highestX) {
                highestX = vertex.x;
            }
            if (vertex.z > highestZ) {
                highestZ = vertex.z;
            }
            if (vertex.x < lowestX) {
                lowestX = vertex.x;
            }
            if (vertex.z < lowestZ) {
                lowestZ = vertex.z;
            }
        }
        float xSide = highestX / (size-1);
        float zSide = highestZ / (size-1);
        X_SIDE = xSide;
        Z_SIDE = zSide;


        System.out.println(xSide);
        System.out.println(zSide);
        float[][] heightMap = new float[size][size];
        int[][] empty = new int[size][size];
        int indexCounter = 0;
        for (int i = 0; i < verticesList.size(); i++) {
            if (indexCounter < verticesList.size()) {
                Vector3f vertex = verticesList.get(indexCounter);
                int x = Math.round(Math.abs(vertex.x / xSide));
                int z = Math.round(Math.abs(vertex.z / zSide));
                if (x < heightMap.length && z < heightMap.length) {
                    heightMap[x][z] = verticesList.get(indexCounter).y;
                    empty[x][z] = 1;
                    indexCounter+=1;
                }
            }
        }
        for (int i = 0; i < empty.length; i++) {
            for (int i1 = 0; i1 < empty.length; i1++) {
                if (empty[i][i1] == 0) {
                    Vector3f nearest = PositionHelper.getNearestPositionY(vertices, i*xSide, i1*zSide);
                    heightMap[i][i1] = nearest.y;
                }
            }
        }
        return heightMap;
    }
}
