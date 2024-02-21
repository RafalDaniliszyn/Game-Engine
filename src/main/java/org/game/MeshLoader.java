package org.game;

import org.game.component.MeshComponent;
import org.game.renderer.Model;
import org.game.renderer.TextureEnum;
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
        List<Vector3f> vertList = new ArrayList<>();
        Deque<Vector3f> fourNearest = new ArrayDeque<>();

        for (int i = 0; i < vert.length; i+=9) {
            vertList.add(new Vector3f(vert[i], vert[i+1], vert[i+2]));
        }
        Vector3f nearest = vertList.get(0);
        Vector2f toCompare = new Vector2f(nearest.x, nearest.z);
        float distance = toCompare.distance(x, z);


        for (int i = 0; i < vertList.size(); i++) {
            toCompare = new Vector2f(vertList.get(i).x, vertList.get(i).z);

            if (toCompare.distance(x, z) < distance) {
                nearest = vertList.get(i);
                if (fourNearest.size() < 4) {
                    fourNearest.add(nearest);
                } else {
                    fourNearest.removeFirst();
                    fourNearest.add(nearest);
                }

                distance = toCompare.distance(x, z);
            }
        }
        return nearest.y;
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
}
