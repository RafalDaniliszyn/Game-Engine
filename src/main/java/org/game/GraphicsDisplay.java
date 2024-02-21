package org.game;

import org.game.renderer.*;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class GraphicsDisplay {
    private static long displayID;
    private static GraphicsDisplay instance = null;
    private ShaderProgram shaderProgram;
    //private int vaoID;
    private Mouse mouse;
    private float deltaTimeGlobal = 0.0f;
    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;


    public GraphicsDisplay() {

    }

    public static  GraphicsDisplay get() {
        if (instance == null) {
            instance = new GraphicsDisplay();
        }
        return instance;
    }

    public void createDisplay() throws InterruptedException {
        init();
        loop();
    }

    private void init() {
        glfwInit();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        displayID = glfwCreateWindow(WIDTH, HEIGHT, "Barney's Studio", 0, 0);

        glfwSetKeyCallback(displayID, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                //update static Key
                Key.window = window;
                Key.key = key;
                Key.scancode = scancode;
                Key.action = action;

                float offset = 25.0f * (deltaTimeGlobal);
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }

                if (key == GLFW_KEY_LEFT_SHIFT) {
                    Camera.movePosition(0.0f, -offset, 0.0f);
                }
            }
        });

        mouse = new Mouse();
        mouse.init(displayID);

        glfwMakeContextCurrent(displayID);
        GL.createCapabilities();

        shaderProgram = new ShaderProgram();
        shaderProgram.create();

        glfwShowWindow(displayID);
    }

    private void loop() {
        GameData gameData = new GameData(shaderProgram);
        gameData.init();

        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, 1920, 1080);
        glBlendFunc(GL_SRC0_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);

        double fpsLimit = 1.0 / 25.0;
        double lastUpdateTime = 0;
        double lastFrameTime = 0;
        while (!glfwWindowShouldClose(displayID)) {
            double now = glfwGetTime();
            double deltaTime = now - lastUpdateTime;
            deltaTimeGlobal = (float) (now - lastFrameTime);

            if ((now - lastFrameTime) >= fpsLimit) {
                glClearColor(1, 1, 1, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                mouse.update();
                gameData.update(deltaTimeGlobal);

                glfwSwapBuffers(displayID);
                glfwPollEvents();

                lastFrameTime = now;
            }

            lastUpdateTime = now;
        }
        gameData.delete();
    }
}
