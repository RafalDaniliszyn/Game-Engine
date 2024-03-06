package org.game;

import org.game.component.MeshComponent;
import org.game.renderer.TextureEnum;
import org.joml.Interpolationf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MeshLoader {

    public static List<MeshComponent> loadMesh(String path, Vector3f scale, Vector3f position) {
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Vector2f> uv = new ArrayList<>();
        List<MeshComponent> meshList = new ArrayList<>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] split = data.split("\\s+");
                if (split[0].equals("v")) {
                    float x = (float) Double.parseDouble(split[1]);
                    float y = (float) Double.parseDouble(split[2]);
                    float z = (float) Double.parseDouble(split[3]);

                    vertices.add(x);
                    vertices.add(y);
                    vertices.add(z);

                    //color
                    vertices.add(0.7f);
                    vertices.add(0.7f);
                    vertices.add(0.7f);
                    vertices.add(1.0f);

                    //texture coordinates only to keep correct order in vertices array
                    vertices.add(0.0f);
                    vertices.add(0.0f);
                }

                if (split[0].equals("vt")) {
                    float u = (float) Double.parseDouble(split[1]);
                    float v = (float) Double.parseDouble(split[2]);
                    uv.add(new Vector2f(u, v));
                }

                if (split[0].equals("f")) {
                    String[] indUVx = split[1].split("/");
                    String[] indUVy = split[2].split("/");
                    String[] indUVz = split[3].split("/");

                    int xVertex = Integer.parseInt(indUVx[0])-1;
                    int yVertex = Integer.parseInt(indUVy[0])-1;
                    int zVertex = Integer.parseInt(indUVz[0])-1;
                    indices.add(xVertex);
                    indices.add(yVertex);
                    indices.add(zVertex);

                    int uvXVertexIndex = Integer.parseInt(indUVx[1])-1;
                    int uvYVertexIndex = Integer.parseInt(indUVy[1])-1;
                    int uvZVertexIndex = Integer.parseInt(indUVz[1])-1;

                    vertices.set( ((xVertex+1) * 9)-2, uv.get(uvXVertexIndex).x);
                    vertices.set( ((xVertex+1) * 9)-1, uv.get(uvXVertexIndex).y);
                    vertices.set( ((yVertex+1) * 9)-2, uv.get(uvYVertexIndex).x);
                    vertices.set( ((yVertex+1) * 9)-1, uv.get(uvYVertexIndex).y);
                    vertices.set( ((zVertex+1) * 9)-2, uv.get(uvZVertexIndex).x);
                    vertices.set( ((zVertex+1) * 9)-1, uv.get(uvZVertexIndex).y);
                }

                if (split[0].equals("s") && split.length == 1) {
                    float[] vert = getVertices(vertices);
                    int[] ind = getIndices(indices);
                    MeshComponent mesh = new MeshComponent(vert, ind, position, scale);
                    meshList.add(mesh);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return meshList;
    }


    public static List<MeshData> loadMeshData(String path) {
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Vector2f> uv = new ArrayList<>();
        List<MeshData> meshList = new ArrayList<>();
        List<TextureEnum> textureEnums = new ArrayList<>();
        int verticesIndexCounter = 0;
        int indicesIndexCounter = 0;
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] split = data.split("\\s+");
                if (split[0].equals("v")) {
                    float x = (float) Double.parseDouble(split[1]);
                    float y = (float) Double.parseDouble(split[2]);
                    float z = (float) Double.parseDouble(split[3]);

                    vertices.add(x);
                    vertices.add(y);
                    vertices.add(z);

                    //color
                    vertices.add(0.9f);
                    vertices.add(0.9f);
                    vertices.add(0.9f);
                    vertices.add(1.0f);

                    //texture coordinates only to keep correct order in vertices array
                    vertices.add(0.0f);
                    vertices.add(0.0f);
                }

                if (split[0].equals("vt")) {
                    float u = (float) Double.parseDouble(split[1]);
                    float v = (float) Double.parseDouble(split[2]);
                    uv.add(new Vector2f(u, v));
                }

                if (split[0].equals("f")) {
                    String[] indUVx = split[1].split("/");
                    String[] indUVy = split[2].split("/");
                    String[] indUVz = split[3].split("/");

                    int xVertex = Integer.parseInt(indUVx[0])-1;
                    int yVertex = Integer.parseInt(indUVy[0])-1;
                    int zVertex = Integer.parseInt(indUVz[0])-1;
                    indices.add(xVertex);
                    indices.add(yVertex);
                    indices.add(zVertex);

                    int uvXVertexIndex = Integer.parseInt(indUVx[1])-1;
                    int uvYVertexIndex = Integer.parseInt(indUVy[1])-1;
                    int uvZVertexIndex = Integer.parseInt(indUVz[1])-1;

                    vertices.set( ((xVertex+1) * 9)-2, uv.get(uvXVertexIndex).x);
                    vertices.set( ((xVertex+1) * 9)-1, uv.get(uvXVertexIndex).y);
                    vertices.set( ((yVertex+1) * 9)-2, uv.get(uvYVertexIndex).x);
                    vertices.set( ((yVertex+1) * 9)-1, uv.get(uvYVertexIndex).y);
                    vertices.set( ((zVertex+1) * 9)-2, uv.get(uvZVertexIndex).x);
                    vertices.set( ((zVertex+1) * 9)-1, uv.get(uvZVertexIndex).y);
                }

                if (split[0].equals("t")) {
                    String textureName = split[1];
                    textureEnums.addAll(Arrays.stream(TextureEnum.values())
                            .filter(texture -> texture.getName().equals(textureName)).toList());
                }

                if (split[0].equals("s") && split.length == 1) {

                    float[] vert = getVertices(vertices.subList(verticesIndexCounter, vertices.size()));
                    int[] ind = getIndices(indices.subList(indicesIndexCounter, indices.size()));
                    for (int i = 0; i < ind.length; i++) {
                        ind[i]-=(verticesIndexCounter/9);
                    }
                    MeshData mesh = new MeshData(vert, ind, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
                    if (textureEnums.size() != 0) {
                        TextureEnum textureEnum = textureEnums.get(0);
                        mesh.setTexture(textureEnum);
                    }
                    verticesIndexCounter += vert.length;
                    indicesIndexCounter += ind.length;
                    meshList.add(mesh);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return meshList;
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

    private static List<Vector3f> getXyzPositionList(float[] vertices) {
        List<Vector3f> vertList = new ArrayList<>();
        for (int i = 0; i < vertices.length; i+=9) {
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

    public static float[][] getHeightMap(float[] vertices) {
        List<Vector3f> verticesList = getXyzPositionList(vertices);
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
                    Vector3f nearest = getNearestPositionY(vertices, i*xSide, i1*zSide);
                    heightMap[i][i1] = nearest.y;
                }
            }
        }
        return heightMap;
    }

    private static List<Vector3f> sortByHeight(Deque<Vector3f> vertices) {
        List<Vector3f> sorted = new ArrayList<>(vertices);
        sorted.sort((o1, o2) -> {
            if (o1.y == o2.y) {
                return 0;
            }
            return o1.y > o2.y ? 1 : -1;
        });
        return sorted;
    }

    private static float[] getVertices(List<Float> vertices) {
        float[] vert = new float[vertices.size()];
        for (int i = 0; i < vert.length; i++) {
            vert[i] = vertices.get(i);
        }
        return vert;
    }

    private static int[] getIndices(List<Integer> indices) {
        int[] ind = new int[indices.size()];
        for (int i = 0; i < ind.length; i++) {
            ind[i] = indices.get(i);
        }
        return ind;
    }

    public static void setLightColors(MeshData meshData, float rotationY) {
        int[] indices = meshData.getIndices();
        float[] vertices = meshData.getVertices();
        for (int i = 0; i < indices.length-3; i+=3) {
            int index1 = indices[i];
            int index2 = indices[i+1];
            int index3 = indices[i+2];

            float x1 = vertices[ index1 * 9];
            float y1 = vertices[(index1 * 9) + 1];
            float z1 = vertices[(index1 * 9) + 2];
            Vector3f vec1 = new Vector3f(x1, y1, z1);

            float x2 = vertices[ index2 * 9];
            float y2 = vertices[(index2 * 9) + 1];
            float z2 = vertices[(index2 * 9) + 2];
            Vector3f vec2 = new Vector3f(x2, y2, z2);

            float x3 = vertices[ index3 * 9];
            float y3 = vertices[(index3 * 9) + 1];
            float z3 = vertices[(index3 * 9) + 2];
            Vector3f vec3 = new Vector3f(x3, y3, z3);

            vec1.rotateY(rotationY);
            vec2.rotateY(rotationY);
            vec3.rotateY(rotationY);

            Vector3f u = new Vector3f(vec2.sub(vec1));
            Vector3f v = new Vector3f(vec3.sub(vec1));

            u.cross(v);
            u.normalize();

            Vector3f light = new Vector3f(10.0f, 100.0f, 0.0f);
            light.normalize();
            float dot = u.dot(light);
            float color = 0.8f;
            color += dot * 0.5f;


            vertices[(index1*9) + 3] = color;
            vertices[(index1*9) + 4] = color;
            vertices[(index1*9) + 5] = color;
        }
        meshData.setVertices(vertices);
    }
}
