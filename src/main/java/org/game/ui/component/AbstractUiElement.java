package org.game.ui.component;

import org.game.WindowCallbackProcessor;
import org.joml.Vector3f;

public abstract class AbstractUiElement extends RawUiModel implements MouseCallback {
    private float x;
    private float y;
    private final float width;
    private final float height;
    private boolean cursorEntered;

    public AbstractUiElement(float width, float height) {
        super(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), width, height);
        this.width = width;
        this.height = height;
    }

    protected AbstractUiElement(float x, float y, float width, float height, int textureId) {
        super(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(x, y, 1.0f), width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        WindowCallbackProcessor.getInstance().addMouseCallback(this);
        super.setTextureID(textureId);
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {}

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        cursorEntered = UiMouseUtils.isCursorWithinBounds(xpos, ypos, width, height, x, y);
    }

    @Override
    public void windowSizeCallbackI(long window, int width, int height) {}

    public boolean isCursorEntered() {
        return cursorEntered;
    }

    public void setX(float x) {
        this.x = x;
        super.getPosition().x = x;
    }

    public void setY(float y) {
        this.y = y;
        super.getPosition().y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
