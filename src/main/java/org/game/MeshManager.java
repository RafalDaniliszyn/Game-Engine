package org.game;

import org.game.component.MeshComponent;
import org.game.component.PositionComponent;
import org.game.renderer.TextureEnum;
import org.game.renderer.TextureManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL11.*;

public class MeshManager {
    private final Map<Long, MeshData> meshDataMap;
    private final Map<String, List<MeshData>> meshDataMapByName;
    private final TextureManager textureManager;

    public MeshManager(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.meshDataMap = new HashMap<>();
        this.meshDataMapByName = new HashMap<>();
        load();
    }

    public MeshComponent getMeshComponent(Long meshId) {
        MeshData meshData = meshDataMap.get(meshId);
        return new MeshComponent(meshData.getVertices(), meshData.getIndices(), meshData.getPosition(), meshData.getScale(), meshData.getTextureID());
    }

    public List<MeshComponent> getMeshComponent(String name) {
        List<MeshData> meshData = meshDataMapByName.get(name);
        List<MeshComponent> meshComponents = new ArrayList<>();
        meshData.forEach(mesh -> {
            meshComponents.add(new MeshComponent(mesh.getVertices(), mesh.getIndices(), mesh.getPosition(), mesh.getScale(), mesh.getTextureID()));
        });
        return meshComponents;
    }

    public MeshComponent getCollisionLines(float[] vert, PositionComponent positionComponent) {
        float[] shape = getShapeLength(vert);
        float aMinX = shape[0];
        float aMinY = shape[1];
        float aMinZ = shape[2];
        float aMaxX = shape[3];
        float aMaxY = shape[4];
        float aMaxZ = shape[5];
        float sc = 0.7f;
        float[] vertices = {
                aMinX, aMinY, aMinZ, sc, sc, sc, 1.0f,   0.0f, 1.0f,
                aMaxX, aMinY, aMinZ, sc, sc, sc, 1.0f,   0.0f, 0.0f,
                aMinX, aMaxY, aMinZ, sc, sc, sc, 1.0f,   1.0f, 0.0f,
                aMaxX, aMaxY, aMinZ, sc, sc, sc, 1.0f,   1.0f, 1.0f,
                aMinX, aMinY, aMaxZ, sc, sc, sc, 1.0f,   0.0f, 1.0f,
                aMaxX, aMinY, aMaxZ, sc, sc, sc, 1.0f,   0.0f, 0.0f,
                aMinX, aMaxY, aMaxZ, sc, sc, sc, 1.0f,   1.0f, 0.0f,
                aMaxX, aMaxY, aMaxZ, sc, sc, sc, 1.0f,   1.0f, 1.0f
        };
        int[] indices = {
            0, 1, 3,
            0, 3, 2,
            4, 6, 7,
            4, 7, 5,
            1, 5, 7,
            1, 7, 3,
            0, 6, 4,
            0, 2, 6
        };
        Integer textureId = textureManager.getTextures().get(TextureEnum.FLOWER);
        MeshComponent meshComponent = new MeshComponent(vertices, indices, positionComponent.getPosition(), positionComponent.getScale(), textureId);
        meshComponent.setRenderMode(GL_LINE_STRIP);
        return meshComponent;
    }

    public MeshData getMeshData(long meshId) {
        return meshDataMap.get(meshId);
    }

    public MeshData getMeshData(String name) {
        return meshDataMapByName.get(name).get(0);
    }

    public static float[] getShapeLength(float[] vertices) {
        float minX = vertices[0];
        float minY = vertices[1];
        float minZ = vertices[2];
        float maxX = vertices[0];
        float maxY = vertices[1];
        float maxZ = vertices[2];
        for (int i = 0; i < vertices.length; i+=9) {
            //min
            if (vertices[i] < minX) {
                minX = vertices[i];
            }
            if (vertices[i+1] < minY) {
                minY = vertices[i+1];
            }
            if (vertices[i+2] < minZ) {
                minZ = vertices[i+2];
            }
            //max
            if (vertices[i] > maxX) {
                maxX = vertices[i];
            }
            if (vertices[i+1] > maxY) {
                maxY = vertices[i+1];
            }
            if (vertices[i+2] > maxZ) {
                maxZ = vertices[i+2];
            }
        }

        return new float[]{
                minX, minY, minZ,
                maxX, maxY, maxZ
        };
    }

    private List<String> getModelsPath() {
        String root = String.valueOf(Objects.requireNonNull(this.getClass().getClassLoader().getResource("models")).getPath());
        List<String> pathList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\test"))) {
            paths.filter(path -> path.toString().contains("test")).forEach(
                    path -> {
                        if (path.getFileName().toString().equals("test")) {
                            return;
                        }
                        pathList.add(path.toString());
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    private void load() {
        List<String> modelsPath = getModelsPath();
        Map<TextureEnum, Integer> textures = textureManager.getTextures();
        modelsPath.forEach(path -> {
            List<MeshData> mesh = MeshLoader.loadMeshData(path);
            int nameStart = path.lastIndexOf("\\")+1;
            int nameEnd = path.length()-4;
            String name = path.substring(nameStart, nameEnd);
            mesh.forEach(meshData -> {
                meshData.setName(name);
                meshData.setTextureID(textures.get(meshData.getTexture()));
            });
            meshDataMapByName.put(mesh.get(0).getName(), mesh);
        });
    }
}
