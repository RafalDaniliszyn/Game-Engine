package org.game.ui.newUiConcept;

import org.game.WindowCallbackProcessor;
import org.game.ui.component.MouseCallback;
import org.game.ui.component.RawUiModel;
import java.util.List;

public abstract class UiComponent implements MouseCallback {
    private float x;
    private float y;
    private float width;
    private float height;
    private UiKeyCallback uiKeyCallback;

    public UiComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        WindowCallbackProcessor.getInstance().addMouseCallback(this);
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {

    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {

    }

    @Override
    public void windowSizeCallbackI(long window, int width, int height) {

    }

    public void setUiKeyCallback(UiKeyCallback uiKeyCallback) {
        this.uiKeyCallback = uiKeyCallback;
        WindowCallbackProcessor.getInstance().addUiKeyCallback(uiKeyCallback);
    }

    public abstract List<RawUiModel> generateRawUiModels();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

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
