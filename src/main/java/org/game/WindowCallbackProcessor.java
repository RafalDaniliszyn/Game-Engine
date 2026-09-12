package org.game;

import org.game.key.Key;
import org.game.ui.component.MouseCallback;
import org.game.ui.newUiConcept.UiKeyCallback;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;

public class WindowCallbackProcessor {
    private final List<GLFWCursorPosCallbackI> cursorPosCallbackListeners;
    private final List<GLFWWindowSizeCallbackI> windowSizeCallbackListeners;
    private final List<GLFWMouseButtonCallbackI> mouseButtonCallbackListeners;
    private final List<MouseCallback> mouseCallbacks;
    private final List<UiKeyCallback> uiKeyCallbacks;
    private static WindowCallbackProcessor windowCallbackProcessor;

    public static WindowCallbackProcessor getInstance() {
        if (windowCallbackProcessor == null) {
            windowCallbackProcessor = new WindowCallbackProcessor();
        }
        return windowCallbackProcessor;
    }

    private WindowCallbackProcessor() {
        this.cursorPosCallbackListeners = Collections.synchronizedList(new ArrayList<>());
        this.windowSizeCallbackListeners = Collections.synchronizedList(new ArrayList<>());
        this.mouseButtonCallbackListeners = Collections.synchronizedList(new ArrayList<>());
        this.mouseCallbacks = Collections.synchronizedList(new ArrayList<>());
        this.uiKeyCallbacks = Collections.synchronizedList(new ArrayList<>());

        GLFWCursorPosCallbackI cursorPosCallbackI = (window, xpos, ypos) -> {
            this.cursorPosCallbackListeners.forEach(listener -> {
                listener.invoke(window, xpos, ypos);
            });
            mouseCallbacks.forEach(mouseCallback -> mouseCallback.cursorPosCallbackI(window, xpos, ypos));
        };
        glfwSetCursorPosCallback(GraphicsDisplay.getDisplayId(), cursorPosCallbackI);

        GLFWWindowSizeCallbackI windowSizeCallbackI = (window, width, height) -> {
            this.windowSizeCallbackListeners.forEach(listener -> {
                listener.invoke(window, width, height);
            });
            mouseCallbacks.forEach(mouseCallback -> mouseCallback.windowSizeCallbackI(window, width, height));
        };
        glfwSetWindowSizeCallback(GraphicsDisplay.getDisplayId(), windowSizeCallbackI);

        GLFWMouseButtonCallbackI mouseButtonCallbackI = (window, button, action, mods) -> {
            this.mouseButtonCallbackListeners.forEach(listener -> {
                listener.invoke(window, button, action, mods);
            });
            mouseCallbacks.forEach(mouseCallback -> mouseCallback.mouseButtonCallbackI(window, button, action, mods));
        };
        glfwSetMouseButtonCallback(GraphicsDisplay.getDisplayId(), mouseButtonCallbackI);

        GLFWKeyCallbackI glfwKeyCallbackI = (window, key, scancode, action, mods) -> {
            Key.update(key, action);
            uiKeyCallbacks.forEach(uiKeyCallback -> uiKeyCallback.invoke(window, key, scancode, action, mods));
        };
        glfwSetKeyCallback(GraphicsDisplay.getDisplayId(), glfwKeyCallbackI);
    }

    public void addCursorPosCallback(GLFWCursorPosCallbackI cursorPosCallbackI) {
        this.cursorPosCallbackListeners.add(cursorPosCallbackI);
    }

    public void addWindowSizeCallback(GLFWWindowSizeCallbackI windowSizeCallbackI) {
        this.windowSizeCallbackListeners.add(windowSizeCallbackI);
    }

    public void addMouseButtonCallback(GLFWMouseButtonCallbackI mouseButtonCallbackI) {
        this.mouseButtonCallbackListeners.add(mouseButtonCallbackI);
    }

    public void addMouseCallback(MouseCallback mouseCallback) {
        this.mouseCallbacks.add(mouseCallback);
    }

    public void addUiKeyCallback(UiKeyCallback uiKeyCallback) {
        this.uiKeyCallbacks.add(uiKeyCallback);
    }
}
