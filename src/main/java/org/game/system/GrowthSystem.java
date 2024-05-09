package org.game.system;

import org.game.GameData;
import org.game.component.GrowthComponent;
import org.game.component.mesh.MeshComponent;
import org.game.component.PositionComponent;
import org.lwjgl.glfw.GLFW;

// TODO: 5/9/2024 Class to remove
public class GrowthSystem extends BaseSystem {


    public GrowthSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        updateTime();
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    private void updateTime() {
        double now = GLFW.glfwGetTime();
        getGameData().getEntities(GrowthComponent.class, MeshComponent.class, PositionComponent.class).forEach((id, entity)-> {
            GrowthComponent growth = entity.getComponent(GrowthComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            if (growth.getStage() >= growth.getNumberOfStages()) {
                growth.setGrowthComplete(true);
            }

//            if (growth.isGrowthComplete()) {
//                double elapsedTime = now - growth.getLastUpdate();
//                double passedTime = growth.getPassedTime() + elapsedTime;
//                if (passedTime >= growth.getTimeBetweenStages()) {
//                    growth.setPassedTime(0.0f);
//                    growth.setLastUpdate(now);
//                    Vector3f scale = positionComponent.getScale();
//                    scale.x += 0.5f;
//                    scale.y += 0.5f;
//                    scale.z += 0.5f;
//                    positionComponent.setScale(scale);
//                } else {
//                    growth.setPassedTime(growth.getPassedTime() + elapsedTime);
//                }
//            }

            if (!growth.isGrowthComplete()) {
                double elapsedTime = now - growth.getLastUpdate();
                double passedTime = growth.getPassedTime() + elapsedTime;
                if (passedTime >= growth.getTimeBetweenStages()) {
                    growth.setPassedTime(0.0f);
                    growth.setStage(growth.getStage() + 1);
                    growth.setLastUpdate(now);
                    Long meshId = growth.getMeshId(growth.getStage());
                    MeshComponent meshComponent =null; //getGameData().getMeshManager().getMeshComponent(meshId);
                    entity.changeComponent(meshComponent, MeshComponent.class);
//                    Vector3f scale = positionComponent.getScale();
//                    scale.x += 0.5f;
//                    scale.y += 0.5f;
//                    scale.z += 0.5f;
//                    positionComponent.setScale(scale);
                } else {
                    growth.setPassedTime(growth.getPassedTime() + elapsedTime);
                }
            }
        });
    }
}
