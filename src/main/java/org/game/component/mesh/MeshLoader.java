package org.game.component.mesh;

import org.game.component.mesh.texture.TextureEnum;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MeshLoader {

    public static List<MeshData> loadMeshData(String path) {
        int STRIDE = 12;
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Vector3f> normal = new ArrayList<>();
        List<Vector2f> uv = new ArrayList<>();
        List<MeshData> meshList = new ArrayList<>();
        List<TextureEnum> textureEnums = new ArrayList<>();
        boolean hasNormal = true;
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

                    //normals only to keep correct order in array
                    vertices.add(0.0f);
                    vertices.add(0.0f);
                    vertices.add(0.0f);
                }
                if (split[0].equals("vn")) {
                    float normalX = Float.parseFloat(split[1]);
                    float normalY = Float.parseFloat(split[2]);
                    float normalZ = Float.parseFloat(split[3]);
                    normal.add(new Vector3f(normalX, normalY, normalZ));
                }
                if (split[0].equals("vt")) {
                    float u = (float) Double.parseDouble(split[1]);
                    float v = (float) Double.parseDouble(split[2]);
                    uv.add(new Vector2f(u, v));
                }
                if (split[0].equals("f")) {
                    String[] split1 = split[1].split("/");
                    String[] split2 = split[2].split("/");
                    String[] split3 = split[3].split("/");

                    //indices
                    int xVertex = Integer.parseInt(split1[0])-1;
                    int yVertex = Integer.parseInt(split2[0])-1;
                    int zVertex = Integer.parseInt(split3[0])-1;
                    indices.add(xVertex);
                    indices.add(yVertex);
                    indices.add(zVertex);

                    //texture coordinates
                    int uvXVertexIndex = Integer.parseInt(split1[1])-1;
                    int uvYVertexIndex = Integer.parseInt(split2[1])-1;
                    int uvZVertexIndex = Integer.parseInt(split3[1])-1;
                    vertices.set( ((xVertex+1) * STRIDE)-(2+3), uv.get(uvXVertexIndex).x);
                    vertices.set( ((xVertex+1) * STRIDE)-(1+3), uv.get(uvXVertexIndex).y);
                    vertices.set( ((yVertex+1) * STRIDE)-(2+3), uv.get(uvYVertexIndex).x);
                    vertices.set( ((yVertex+1) * STRIDE)-(1+3), uv.get(uvYVertexIndex).y);
                    vertices.set( ((zVertex+1) * STRIDE)-(2+3), uv.get(uvZVertexIndex).x);
                    vertices.set( ((zVertex+1) * STRIDE)-(1+3), uv.get(uvZVertexIndex).y);

                    //normals
                    if (split1.length == 3) {
                        int normalXIndex = Integer.parseInt(split1[2])-1;
                        int normalYIndex = Integer.parseInt(split2[2])-1;
                        int normalZIndex = Integer.parseInt(split3[2])-1;
                        vertices.set( ((xVertex+1) * STRIDE)-3, normal.get(normalXIndex).x);
                        vertices.set( ((xVertex+1) * STRIDE)-2, normal.get(normalXIndex).y);
                        vertices.set( ((xVertex+1) * STRIDE)-1, normal.get(normalXIndex).z);
                        vertices.set( ((yVertex+1) * STRIDE)-3, normal.get(normalYIndex).x);
                        vertices.set( ((yVertex+1) * STRIDE)-2, normal.get(normalYIndex).y);
                        vertices.set( ((yVertex+1) * STRIDE)-1, normal.get(normalYIndex).z);
                        vertices.set( ((zVertex+1) * STRIDE)-3, normal.get(normalZIndex).x);
                        vertices.set( ((zVertex+1) * STRIDE)-2, normal.get(normalZIndex).y);
                        vertices.set( ((zVertex+1) * STRIDE)-1, normal.get(normalZIndex).z);
                    } else {
                        hasNormal = false;
                    }
                }
                if (split[0].equals("t")) {
                    String textureName = split[1];
                    textureEnums.addAll(Arrays.stream(TextureEnum.values())
                            .filter(texture -> texture.getName().equals(textureName)).toList());
                }
                if (split[0].equals("s") && split.length == 1) {
                    float[] vert = toFloatArray(vertices.subList(verticesIndexCounter, vertices.size()));
                    int[] ind = toIntArray(indices.subList(indicesIndexCounter, indices.size()));
                    for (int i = 0; i < ind.length; i++) {
                        ind[i]-=(verticesIndexCounter / STRIDE);
                    }
                    MeshData mesh = new MeshData(vert, ind, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), hasNormal);
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

    private static float[] toFloatArray(List<Float> vertices) {
        float[] vert = new float[vertices.size()];
        for (int i = 0; i < vert.length; i++) {
            vert[i] = vertices.get(i);
        }
        return vert;
    }

    private static int[] toIntArray(List<Integer> indices) {
        int[] ind = new int[indices.size()];
        for (int i = 0; i < ind.length; i++) {
            ind[i] = indices.get(i);
        }
        return ind;
    }
}
