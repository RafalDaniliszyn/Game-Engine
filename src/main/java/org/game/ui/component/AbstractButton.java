package org.game.ui.component;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public abstract class AbstractButton extends AbstractUiElement {

    private final int mouseButton;

    public AbstractButton(float width, float height, int mouseButton) {
        super(width, height);
        this.mouseButton = mouseButton;
    }

    public AbstractButton(float x, float y, float width, float height, int textureId, int mouseButton) {
        super(x, y, width, height, textureId);
        this.mouseButton = mouseButton;
    }

    public abstract void onClick();

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {
        super.mouseButtonCallbackI(window, button, action, mods);
        if (button == mouseButton && action == GLFW_PRESS && isCursorEntered()) {
            onClick();
        }
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        super.cursorPosCallbackI(window, xpos, ypos);
    }

}