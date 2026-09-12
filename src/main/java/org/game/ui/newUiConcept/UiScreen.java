package org.game.ui.newUiConcept;

import java.util.List;

public abstract class UiScreen {
    private float width;
    private float height;

    public abstract List<UiContainer> getContainers();

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
