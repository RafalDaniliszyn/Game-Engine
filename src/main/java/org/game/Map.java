package org.game;

import org.game.renderer.Model;
import org.game.renderer.TextureEnum;
import org.game.renderer.TextureManager;
import org.joml.Vector3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Map {


    /**
     *
     * @param size This value is number of vertices in one side
     * @return Generated Model consisting of side-1 * side-1 quads
     */
    public static Model getQuadsMap(int size) {
        float[] vertices = getVertices(size);
        int[] quads = new int[((size-1) * (size-1))*6];
        int mapColumn = 0;
        int mapColumnIncrementCounter = -1;
        int counter = 0;
        for (int i = 0; i < ((size-1)*(size-1)); i++) { //This is numbers of quads
            mapColumnIncrementCounter+=1;
            if (mapColumnIncrementCounter == size-1) {
                mapColumn+=1;
                mapColumnIncrementCounter = 0;
            }

            quads[counter]   = i + 1 + mapColumn;
            quads[counter+1] = i   +   mapColumn;
            quads[counter+2] = i + size + mapColumn;    //add size because of column change
            quads[counter+3] = i + size+1 + mapColumn;
            quads[counter+4] = i + size + mapColumn;
            quads[counter+5] = i + 1 + mapColumn;

            counter += 6;
        }
        return new Model(
                vertices,
                quads,
                new Vector3f(-100.0f, 0.0f, -100.0f),
                0.0f,
                new Vector3f(2.0f, 2.0f, 2.0f)
        );
    }

    public static List<Model> getQuadsMapModels(int size, int[] textures) {
        int[][][] tile = loadTileMap("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\map.txt", 110);
        List<Model> models = new ArrayList<>();
        float tileLength = 1.0f;

        for (int z = 0; z < tile.length; z++) {
            for (int x = 0; x < tile.length; x++) {
                Vector3f position  = new Vector3f(x * tileLength, 0, z * tileLength);
                float[] vertices = {
                         0.0f,        0.0f, tileLength,      0.7f, 0.7f, 0.7f, 1.0f,      0.0f, 1.0f,
                         0.0f,        0.0f,       0.0f,      0.7f, 0.7f, 0.7f, 1.0f,      0.0f, 0.0f,
                         tileLength,  0.0f,       0.0f,      0.7f, 0.7f, 0.7f, 1.0f,      1.0f, 0.0f,
                         tileLength,  0.0f, tileLength,      0.7f, 0.7f, 0.7f, 1.0f,      1.0f, 1.0f

                };
                int[] quad = {
                        0, 1, 2,
                        3, 2, 0
                };
                Model model = new Model(
                        vertices,
                        quad,
                        position,
                        0.0f,
                        new Vector3f(1.0f, 1.0f, 1.0f)
                );
                int textureID = tile[x][0][z];
                model.textureID = textures[textureID];
                models.add(model);
            }
        }
        return models;
    }

    public static List<Model> getGrassMap(TextureManager textureManager) {
        int vaoID = glGenVertexArrays();
        List<Model> grassList = new ArrayList<>();
        Random random = new Random();
        float[] vert = {
                1.000000f, 0.000000f, -1.000000f, 0.7f, 0.7f, 0.7f, 1.0f, 0.0f, 0.0f,
                -1.000000f, 0.000000f, -1.000000f, 0.7f, 0.7f, 0.7f, 1.0f, 1.0f, 0.0f,
                1.000000f, 0.000000f, 1.000000f,  0.7f, 0.7f, 0.7f, 1.0f, 0.0f, 1.0f,
                -1.000000f, 0.000000f, 1.000000f,  0.7f, 0.7f, 0.7f, 1.0f, 1.0f, 1.0f
        };
        int[] ind = {
                0, 2, 1, 2, 3, 1
        };
        int grassTexture = textureManager.getTextures().get(TextureEnum.DRY_GRASS);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                float randomPos = random.nextFloat();
                float randomScale = random.nextFloat();
                float randomRotationY = random.nextInt(360);
                randomScale *= 0.7f;
                Model grass = new Model(vert, ind, new Vector3f((i*0.8f)+randomPos, 0.5f, (j*0.9f)+randomPos), new Vector3f(randomScale, randomScale, randomScale), vaoID);
                grass.setRotationX(-90.0f);
                grass.setRotationZ(randomRotationY);
                grass.textureID = grassTexture;
                grassList.add(grass);
            }
        }
        return grassList;
    }


    private static int[][][] getTileArray(int size) {
        int[][][] verticesXYZ = new int[size][2][size];
        for (int z = 0; z < verticesXYZ.length; z++) {
            for (int x = 0; x < verticesXYZ.length; x++) {
                verticesXYZ[x][0][z] = 0;//testowo 1
            }
        }
        return verticesXYZ;
    }

    private static int[][][] loadTileMap(String path, int size) {
        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int[][][] map = new int[size][2][size];
        while(scanner.hasNextLine()) {

            for (int j = 0; j < size; j++) {
                if (scanner.hasNext()) {
                    String x = scanner.nextLine();
                    String[] split = x.split(",");
                    for (int i = 0; i < split.length; i++) {
                        map[i][0][j] = Integer.parseInt(split[i]);
                    }
                }
            }
        }
        return map;
    }

    private static float[] getVertices(int size) {
        List<Vector3f> verticesXYZ = new ArrayList<>();
        Random random = new Random();
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                float functX = (float) Math.toRadians(x*3.79f);
                float functZ = (float) Math.toRadians(z*3.79f);
                float y = (float) Math.sin(functX)*2;
                float cos = (float) Math.cos(functZ/3.0f);
                y+=cos;
                if (z % 3 == 0 || x % 3 == 0) {
                    y+=random.nextFloat()-0.3f;
                }
                verticesXYZ.add(new Vector3f((float)x, 0.0f, (float)z));
            }
        }

        float[] vertices = new float[verticesXYZ.size()*9];
        int rowCounter = 0;
        for (int i = 0; i < verticesXYZ.size(); i++) {
            //coordinates
            vertices[rowCounter] = verticesXYZ.get(i).x;
            vertices[rowCounter + 1] = verticesXYZ.get(i).y;
            vertices[rowCounter + 2] = verticesXYZ.get(i).z;

            //colors
            vertices[rowCounter + 3] = 0.7f;
            vertices[rowCounter + 4] = 0.7f;
            vertices[rowCounter + 5] = 0.7f;
            vertices[rowCounter + 6] = 1.0f;

            rowCounter += 9;
        }

        //texture coordinates
        for (int i = 7; i < vertices.length; i+=9) {
            vertices[i] = vertices[i-7];
            vertices[i+1] =  vertices[i-5];
        }
        return vertices;
    }


    public static Model[][] generateTriangleMap(int size) {
        Model[][] models = new Model[size][size];
        double height = 0;
        for (int i = 0; i < size; i++) {
            for (int i1 = 0; i1 < size; i1++) {
                height = Math.sin(i);
                models[i][i1] = getNewPart(new Vector3f(i, (float)height, i1));
            }

        }
        return models;
    }

    private static Model getNewPart(Vector3f position) {
        float[] vertices =  new float[]{
                -0.5f,  0.5f, -1.0f,   1.0f, 0.0f, 0.0f, 1.0f,   0.0f, 1.0f,   // top right
                -0.5f,  0.5f,  0.0f,   0.0f, 1.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom right
                0.5f,  0.5f,  0.0f,   0.0f, 0.0f, 1.0f, 1.0f,    1.0f, 0.0f,   // bottom left
                0.5f,  0.5f, -1.0f,   0.0f, 1.0f, 0.0f, 1.0f,    1.0f, 1.0f,   // top left
        };
        int[] indices = {
                0, 1, 2,
                3, 2, 0
        };
        return new Model(
                vertices,
                indices,
                position,
                0.0f,
                new Vector3f(1.0f, 1.0f, 1.0f));
    }

    public static int[][][] generateMap() {
        Random random = new Random();
        int[][][] map = getFilledMap();
        for (int x = 0; x < map.length; x++) {
            for (int z = 0; z < map.length; z++) {

                int randomHeight = random.nextInt(2)+6 ;

                double height = Math.sin(x);
                double heightCos = 4*Math.cos(x/4.0f);
                height += heightCos+5;
                System.out.println((int)height);
                for (int y = 0; y < (int)height; y++) {
                    map[x][y][z] = 1;
                }
                double heightZ = (Math.sin(z*(Math.PI/20))) + randomHeight;
                System.out.println((int)Math.abs(heightZ));
                for (int y = 0; y < (int)Math.abs(heightZ); y++) {
                    map[x][y][z] = 1;
                }

            }
        }
        return map;
    }

    private static int[][][] getFilledMap() {
        int[][][] newMap = new int[50][50][50];
        for (int i = 0; i < newMap.length; i++) {
            for (int i1 = 0; i1 < newMap.length; i1++) {
                for (int i2 = 0; i2 < newMap.length; i2++) {
                    if (i1 == 0) {
                        newMap[i][i1][i2] = 1;
                    }else {
                        newMap[i][i1][i2] = 0;
                    }
                }
            }
        }
        return newMap;
    }
}
