package org.game.entity;

import org.game.MeshLoader;
import org.game.component.*;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

public class TreeEntity extends Entity {



    public TreeEntity(int textureID, float x, float z) {
        super();
        Random random = new Random();
        addComponent(new PositionComponent(
                new Vector3f(x, 0.0f, z), 0.0f, random.nextInt(360), 0.0f, new Vector3f(1.0f, 1.0f, 1.0f)));
        List<MeshComponent> meshComponent = MeshLoader
                .loadMesh("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree1.txt",
                        new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 0.0f, 0.0f));

        meshComponent.forEach(element -> {
            element.setTexture(textureID);
        });
        addComponent(meshComponent);
        addComponent(new GrowthComponent(1, random.nextInt(900)+700, 5));
    }

}
