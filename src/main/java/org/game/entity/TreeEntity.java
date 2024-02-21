package org.game.entity;

import org.game.MeshManager;
import org.game.component.*;
import org.joml.Vector3f;
import java.util.Random;

public class TreeEntity extends Entity {

    private StaticObjectEntity tree;

    public TreeEntity(MeshManager meshManager, int textureID, float x, float y, float z) {
        super();
        Random random = new Random();

        this.tree = new StaticObjectEntity(meshManager, "tree2", new Vector3f(x, y, z),
                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);

        GrowthComponent growthComponent = new GrowthComponent(1, random.nextInt(900)+700, 5);
        this.tree.addComponent(growthComponent);
    }

    public StaticObjectEntity getTree() {
        return tree;
    }
}
