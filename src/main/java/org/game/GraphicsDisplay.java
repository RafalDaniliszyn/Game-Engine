package org.game;

import org.game.mouse.Mouse;
import org.game.system.shader.ShaderProgram;
import org.joml.Vector2f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_CULL_FACE;
import static org.lwjgl.opengl.GL30.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL30.glClear;
import static org.lwjgl.opengl.GL30.glEnable;
import static org.lwjgl.opengl.GL30.glViewport;

public class GraphicsDisplay {
    private static long displayID;
    private static GraphicsDisplay instance = null;
    private ShaderProgram shaderProgram;
    private Mouse mouse;
    private float deltaTimeGlobal = 0.0f;
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    public static final int BASE_WIDTH = 1280;
    public static final int BASE_HEIGHT = 720;
    public static final float LEFT_SHIFT = 0.00f;
    public static String host;
    public static int port;

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

        mouse = new Mouse();
        mouse.init(displayID);

        glfwMakeContextCurrent(displayID);
        glfwSetWindowAspectRatio(displayID, 16, 9);
        GL.createCapabilities();
        glfwShowWindow(displayID);
    }

    private void loop() throws InterruptedException {
        GameData gameData = new GameData();
        gameData.init();
        gameData.setActive(true);
        glEnable(GL_DEPTH_TEST);
        glViewport((int) (WIDTH * LEFT_SHIFT), 0, WIDTH, HEIGHT);
        //glViewport(0, 0, 1920, 1080);
        glEnable(GL_CULL_FACE);
//        glFrontFace(GL_CCW);
//        glCullFace(GL_BACK);

        double fpsLimit = 1.0 / 100.0;
        double lastUpdateTime = 0;
        double lastFrameTime = 0;

        int fps = 0;
        double fpsCounter = 0.0;
        while (!glfwWindowShouldClose(displayID)) {
            double now = glfwGetTime();
            double deltaTime = now - lastUpdateTime;
            deltaTimeGlobal = (float) (now - lastFrameTime);

            if ((now - lastFrameTime) >= fpsLimit) {
                glClearColor(0.1f, 0.1f, 0.1f, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                mouse.update();
                gameData.update(deltaTimeGlobal);

                glfwSwapBuffers(displayID);
                glfwPollEvents();

                fpsCounter += now - lastFrameTime;
                fps += 1;
                if (fpsCounter >= 1.0) {
                    //System.out.println(fps);
                    fps = 0;
                    fpsCounter = 0.0;
                }
                lastFrameTime = now;
            }

            lastUpdateTime = now;
        }
        gameData.delete();
    }

    public static long getDisplayId() {
        return displayID;
    }


    private static int LAST_WINDOWED_WIDTH;
    private static int LAST_WINDOWED_HEIGHT;
    private static int LAST_WINDOWED_X;
    private static int LAST_WINDOWED_Y;

    public static void setMode(boolean fullScreen) {
        long monitor = GraphicsDisplay.getCurrentMonitor();
        GLFWVidMode videoMode = glfwGetVideoMode(monitor);
        if (videoMode == null) {
            throw new RuntimeException("Unable to get video mode for monitor");
        }

        if (!fullScreen) {
            System.out.println("set to window mode" + LAST_WINDOWED_WIDTH + " " + LAST_WINDOWED_HEIGHT);
            WIDTH = LAST_WINDOWED_WIDTH;
            HEIGHT = LAST_WINDOWED_HEIGHT;
            glfwSetWindowMonitor(GraphicsDisplay.getDisplayId(), 0L, LAST_WINDOWED_X, LAST_WINDOWED_Y, LAST_WINDOWED_WIDTH, LAST_WINDOWED_HEIGHT, videoMode.refreshRate());
        } else {
            LAST_WINDOWED_WIDTH = WIDTH;
            LAST_WINDOWED_HEIGHT = HEIGHT;
            int[] windowX = new int[1];
            int[] windowY = new int[1];
            glfwGetWindowPos(GraphicsDisplay.getDisplayId(), windowX, windowY);
            LAST_WINDOWED_X = windowX[0];
            LAST_WINDOWED_Y = windowY[0];
            WIDTH = videoMode.width();
            HEIGHT = videoMode.height();
            System.out.println("set to video mode");
            glfwSetWindowMonitor(GraphicsDisplay.getDisplayId(), monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());
        }
    }

    public static Vector2f getWindowSize() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(GraphicsDisplay.getDisplayId(), width, height);
        return new Vector2f(width[0], height[0]);
    }

    public static long getCurrentMonitor() {
        long result = 0L;
        int[] windowX = new int[1];
        int[] windowY = new int[1];
        glfwGetWindowPos(GraphicsDisplay.getDisplayId(), windowX, windowY);
        PointerBuffer pointerBuffer = glfwGetMonitors();
        assert pointerBuffer != null;
        int capacity = pointerBuffer.capacity();
        long[] monitors = new long[capacity];
        pointerBuffer.get(monitors);

        for (long monitor : monitors) {
            int[] monitorX = new int[1];
            int[] monitorY = new int[1];
            int[] monitorWidth = new int[1];
            int[] monitorHeight = new int[1];
            glfwGetMonitorWorkarea(monitor, monitorX, monitorY, monitorWidth, monitorHeight);

            if (windowX[0] >= monitorX[0] && windowX[0] < monitorX[0] + monitorWidth[0]) {
                result = monitor;
            }
        }
        return result;
    }
}
