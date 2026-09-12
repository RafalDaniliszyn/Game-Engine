package org.game.ui.component;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

public abstract class Container implements MouseCallback {
    private int x;
    private int y;
    private float width;
    private float height;
    private final Layout layout;
    private RawUiModel containerBackground;
    private final List<RawUiModel> childrenRawUiModelList;
    private final List<? extends AbstractUiElement> elements;
    private float depth = 1.0f;

    public Container(List<? extends AbstractUiElement> elements, Layout layout) {
        this.elements = new ArrayList<>(elements);
        this.childrenRawUiModelList = new ArrayList<>();
        this.layout = layout;
        this.layout.align(this);
        setContainerBackground();
    }

    public Container(List<? extends AbstractUiElement> elements, int x, int y, Layout layout) {
        this.elements = new ArrayList<>(elements);
        this.childrenRawUiModelList = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.layout = layout;
        this.layout.align(this);
        setContainerBackground();
    }

    public List<? extends AbstractUiElement> getElements() {
        return elements;
    }

    private void setContainerBackground() {
        Vector2f containerSize = new Vector2f(layout.getSize());
        int margin = layout.getMargin();
        float rawModelY = y - (containerSize.y + margin);
        float rawModelX = x - margin;
        width = containerSize.x + margin;
        height = containerSize.y + margin;
        if (containerBackground == null) {
            containerBackground = new RawUiModel(
                    new Vector3f(1.0f, 1.0f, 0.0f),
                    new Vector3f(rawModelX, rawModelY, depth), width, height);
        } else {
            containerBackground.setPosition(new Vector3f(rawModelX, rawModelY, depth));
        }
    }

    public Vector2i getRelativeBackgroundPos() {
        Vector2f containerSize = new Vector2f(layout.getSize());
        int margin = layout.getMargin();
        float rawModelX = x - margin;
        float rawModelY = y - (containerSize.y + margin);
        return new Vector2i((int) rawModelX, (int) rawModelY);
    }

    public void addChildrenUiModel(List<RawUiModel> rawUiModels) {
        childrenRawUiModelList.addAll(rawUiModels);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        layout.align(this);
        setContainerBackground();
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<RawUiModel> getRawUiModelList() {
        List<RawUiModel> result = new ArrayList<>(childrenRawUiModelList);
        result.add(containerBackground);
        return result;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Layout getLayout() {
        return layout;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
