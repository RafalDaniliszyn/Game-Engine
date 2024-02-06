package org.game.component;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class GrowthComponent extends Component {

    private int stage;
    private int numberOfStages;
    private double timeBetweenStages;
    private double lastUpdate;
    private double passedTime;
    private boolean growthComplete;
    private Map<Integer, Long> meshIds;

    public GrowthComponent(int stage, float timeBetweenStages, int numberOfStages) {
        this.stage = stage;
        this.timeBetweenStages = timeBetweenStages;
        this.passedTime = 0.0f;
        this.lastUpdate = GLFW.glfwGetTime();
        this.growthComplete = false;
        this.numberOfStages = numberOfStages;
        this.meshIds = new HashMap<>();
        this.meshIds.put(1, 2L);
        this.meshIds.put(2, 3L);
        this.meshIds.put(5, 4L);
        this.meshIds.put(4, 5L);
        this.meshIds.put(3, 6L);
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public double getTimeBetweenStages() {
        return timeBetweenStages;
    }

    public void setTimeBetweenStages(double timeBetweenStages) {
        this.timeBetweenStages = timeBetweenStages;
    }

    public double getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(double passedTime) {
        this.passedTime = passedTime;
    }

    public double getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(double lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isGrowthComplete() {
        return growthComplete;
    }

    public void setGrowthComplete(boolean growthComplete) {
        this.growthComplete = growthComplete;
    }

    public int getNumberOfStages() {
        return numberOfStages;
    }

    public void setNumberOfStages(int numberOfStages) {
        this.numberOfStages = numberOfStages;
    }

    public Long getMeshId(int stage) {
        return meshIds.get(stage);
    }
}
