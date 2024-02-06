package org.game;

import org.game.renderer.*;
import org.joml.Vector3f;

import java.util.List;

public class Background {
    private List<Model> models;


    public Background() {
        models =  MeshLoader.load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\sphere.txt",
                new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(26.0f, -13.0f, 19.0f), MeshType.HEMISPHERE);
        TextureManager textureManager = new TextureManager();
        int textureID = textureManager.loadTexture(TextureEnum.SKY_FRONT, 0, 1);
        models.forEach(model -> {
            model.textureID = textureID;
        });
    }

    public List<Model> getModels() {
        return models;
    }

    public void delete() {
        models.forEach(Model::delete);
    }

}
