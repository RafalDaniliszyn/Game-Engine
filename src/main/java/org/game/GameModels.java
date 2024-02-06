package org.game;

import org.game.renderer.Model;
import org.game.renderer.TextureEnum;
import org.game.renderer.TextureManager;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModels {
    private Map<String, List<Model>> modelMap = new HashMap<>();

    public GameModels() {
        TextureManager textureManager = new TextureManager();
        List<Model> tree = MeshLoader
                .load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree5.txt",
                        new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(10.0f, 0.0f, 20.0f));
        int treeID = textureManager.loadTexture(TextureEnum.TREE_COLORS, 1, 1);
        tree.forEach(element -> {
            element.textureID = treeID;
        });
        modelMap.put("tree", tree);
    }

    public List<Model> getModel(String type) {
        return modelMap.get(type);
    }

    public void deleteModels() {
        modelMap.forEach((s, models) -> {
            models.forEach(Model::delete);
        });
    }
}
