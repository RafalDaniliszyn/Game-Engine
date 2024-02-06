package org.game.system;

import org.game.GameData;
import org.game.component.GrowthComponent;
import org.game.component.MeshComponent;
import org.game.component.PositionComponent;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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
            //System.out.println("stage: " + growth.getStage() + " elapsed time: " + growth.getPassedTime());
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
                    long meshId = growth.getMeshId(growth.getStage());
                    MeshComponent meshComponent = getGameData().getMeshManager().getMeshComponent(meshId);
                    entity.changeComponent(meshComponent, MeshComponent.class);
                    Vector3f scale = positionComponent.getScale();
                    scale.x += 0.5f;
                    scale.y += 0.5f;
                    scale.z += 0.5f;
                    positionComponent.setScale(scale);
                } else {
                    growth.setPassedTime(growth.getPassedTime() + elapsedTime);
                }
            }
        });
    }
}
