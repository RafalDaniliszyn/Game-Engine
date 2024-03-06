package org.game.renderer;

import org.game.MouseInput;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {

    private Vector2d previousPos;

    private Vector2d currentPos;

    private Vector2f displVec;

    private boolean inWindow = false;

    private boolean LEFT = false;
    private boolean RIGHT = false;


    public Mouse() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f(0, 0);
    }

    public void init(long displayID) {
        glfwSetCursorPosCallback(displayID, (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
            MouseInput.x = xpos;
            MouseInput.y = ypos;
        });
        glfwSetCursorEnterCallback(displayID, (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(displayID, (windowHandle, button, action, mode) -> {
            LEFT = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            RIGHT = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
        glfwSetScrollCallback(displayID, (windowHandle, xoffset, yoffset) -> {
            MouseInput.WHEEL_UP = yoffset > 0.0;
            MouseInput.WHEEL_DOWN = yoffset < 0.0;
        });
    }
    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public void update() {
        input();
        if (RIGHT) {
            Vector2f rotVec = displVec;
            Camera.moveRotation(rotVec.x * 0.1f, rotVec.y * -0.1f, 0);
        }
    }
}
