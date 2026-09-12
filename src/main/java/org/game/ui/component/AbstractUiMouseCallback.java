package org.game.ui.component;

import org.game.WindowCallbackProcessor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public abstract class AbstractUiMouseCallback<T extends Container> implements MouseCallback {
    private T t;
    private boolean cursorWithinBounds;
    private boolean dragContainerActive;

    public AbstractUiMouseCallback() {
    }

    public AbstractUiMouseCallback(T t) {
        WindowCallbackProcessor.getInstance().addMouseCallback(this);
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS && cursorWithinBounds) {
            dragContainerActive = true;
        }
        if (action == GLFW_RELEASE) {
            dragContainerActive = false;
        }
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        cursorWithinBounds = UiMouseUtils.isCursorWithinBounds(xpos, ypos, t.getWidth(), t.getHeight(), t.getX(), t.getY());
    }

    @Override
    public void windowSizeCallbackI(long window, int width, int height) {

    }

    public boolean isCursorWithinBounds() {
        return cursorWithinBounds;
    }

    public void setCursorWithinBounds(boolean cursorWithinBounds) {
        this.cursorWithinBounds = cursorWithinBounds;
    }

    public boolean isDragContainerActive() {
        return dragContainerActive;
    }

    public void setDragContainerActive(boolean dragContainerActive) {
        this.dragContainerActive = dragContainerActive;
    }
}
