package org.game.entity;

import org.game.component.CollisionComponent;
import org.game.component.PositionComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.mesh.MeshManager;
import org.game.system.shader.ShaderEnum;
import org.joml.Vector3f;
import java.util.List;
import java.util.Random;

public class MultipleObjectsEntity extends Entity {

    public MultipleObjectsEntity(float[] mapVertices, MeshManager meshManager, String meshName,
                                 Vector3f rotation, Vector3f scale, int size, boolean lines, boolean collision) {
        super(new EntityProperties(ShaderEnum.WIND));
        PositionComponent[] positionComponent = new PositionComponent[size];
        Random random = new Random();
        for (int i = 0; i < positionComponent.length; i++) {
            positionComponent[i] = new PositionComponent(
                    mapVertices, new Vector3f(random.nextInt(500)+400, 0.0f,
                    random.nextInt(500)), rotation.x, rotation.y, rotation.z, new Vector3f(random.nextFloat() + 1, random.nextFloat() + 1, random.nextFloat() + 1));
            addComponent(positionComponent[i]);
        }

        CollisionComponent collisionComponent = new CollisionComponent();
        List<MeshComponent> meshComponent = meshManager.getMeshComponent(meshName, rotation.y);
        if (lines) {
            meshComponent.forEach(mesh -> {
                for (PositionComponent component : positionComponent) {
                    addComponent(meshManager.getCollisionLines(mesh.getVertices(), component));
                }
            });
        }
        addComponents(meshComponent);
        if (collision) {
            addComponent(collisionComponent);
        }
    }

}
