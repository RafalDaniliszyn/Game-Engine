package org.game;

import org.game.component.MeshComponent;
import org.game.renderer.TextureEnum;
import org.game.renderer.TextureManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshManager {
    private final Map<Long, MeshData> meshDataMap;
    private final TextureManager textureManager;

    public MeshManager(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.meshDataMap = new HashMap<>();
        loadAll();
    }

    public MeshComponent getMeshComponent(long meshId) {
        MeshData meshData = meshDataMap.get(meshId);
        return new MeshComponent(meshData.getVertices(), meshData.getIndices(), meshData.getPosition(), meshData.getScale(), meshData.getTextureID());
    }

    public MeshData getMeshData(long meshId) {
        return meshDataMap.get(meshId);
    }

    private void loadAll() {
        Map<TextureEnum, Integer> textures = textureManager.getTextures();
        List<MeshData> meshData = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tower.txt");
        meshData.forEach(mesh -> {
             mesh.setTextureID(textures.get(TextureEnum.TOWER));
        });
        meshDataMap.put(1L, meshData.get(0));

        List<MeshData> tree1 = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree1.txt");
        tree1.forEach(mesh -> {
            mesh.setTextureID(textures.get(TextureEnum.TREE_COLORS));
        });
        meshDataMap.put(2L, tree1.get(0));

        List<MeshData> tree2 = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree2.txt");
        tree2.forEach(mesh -> {
            mesh.setTextureID(textures.get(TextureEnum.TREE_COLORS));
        });
        meshDataMap.put(3L, tree2.get(0));

        List<MeshData> tree3 = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree3.txt");
        tree3.forEach(mesh -> {
            mesh.setTextureID(textures.get(TextureEnum.TREE_COLORS));
        });
        meshDataMap.put(4L, tree3.get(0));

        List<MeshData> tree4 = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree4.txt");
        tree4.forEach(mesh -> {
            mesh.setTextureID(textures.get(TextureEnum.TREE_COLORS));
        });
        meshDataMap.put(5L, tree4.get(0));

        List<MeshData> tree5 = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree5.txt");
        tree5.forEach(mesh -> {
            mesh.setTextureID(textures.get(TextureEnum.TREE_COLORS));
        });
        meshDataMap.put(6L, tree5.get(0));

        List<MeshData> player = MeshLoader.loadMeshData("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\player.txt");
        player.forEach(mesh -> {
            mesh.setTextureID(textures.get(TextureEnum.PLAYER));
        });
        meshDataMap.put(7L, player.get(0));
    }
}
