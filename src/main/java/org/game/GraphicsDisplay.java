package org.game;

import org.game.renderer.*;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class GraphicsDisplay {
    private static long displayID;
    private static GraphicsDisplay instance = null;

    private ShaderProgram shaderProgram;
    private ShaderProgram shaderProgramAlpha;
    private int vaoID;
    private Mouse mouse;
    private float deltaTimeGlobal = 0.0f;

    public static int width = 1920;
    public static int height = 1080;



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
        displayID = glfwCreateWindow(width, height, "Barney's Studio", 0, 0);

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

        //program for textures with alpha
        shaderProgramAlpha = new ShaderProgram();
        String vertexShader = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\vertex.glsl";
        String fragmentShader = "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\alphaFragment.glsl";
        shaderProgramAlpha.create(vertexShader, fragmentShader);

        glfwShowWindow(displayID);
    }

    private void loop() throws InterruptedException {
        System.out.println("loop start");

        Renderer renderer = new Renderer();

        TextureManager textureManager = new TextureManager();
        java.util.Map<TextureEnum, Integer> textureIDS = textureManager.getTextures();

        //List<Model> tileModels = Map.getQuadsMapModels(1000, textureIDS);
//        Model nonTileModels = Map.getQuadsMap(300);
//        nonTileModels.textureID = textureIDS[0];

        List<Model> mapModel = MeshLoader
                .load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\map.txt",
                        new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 0.0f, 0.0f));
        mapModel.forEach(model -> {
            model.textureID = textureIDS.get(TextureEnum.GRASS);
        });

        List<Model> tree = MeshLoader
                .load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\tree\\tree5.txt",
                        new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(20.0f, 0.0f, 10.0f));
        int treeID = textureIDS.get(TextureEnum.TREE_COLORS);
        tree.forEach(element -> {
            element.textureID = treeID;
        });

        GameData gameData = new GameData(shaderProgram, projectionMatrix());
        gameData.init();

        Background background = new Background();

        List<Model> cylinder = MeshLoader.load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\cylinder.txt",
                new Vector3f(140.0f, 10.0f, 140.0f), new Vector3f(0.0f, 10.0f, 0.0f), MeshType.CYLINDER);
        cylinder.forEach(model -> {
            model.textureID =  textureIDS.get(TextureEnum.TREES);
        });

        Model skyCube = Cube.getCube(new Vector3f(1.0f, 1.0f, 1.0f), 0.7f, new Vector3f(300.0f, 300.0f, 300.0f), 1);
        skyCube.textureID = textureManager.loadCubeMap(TextureCubeMap.SKY);

        List<Model> grass = Map.getGrassMap(textureManager);

        //Building init
        List<Model> mediumHouse = MeshLoader.load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\mediumhouse.txt",
                new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(130.0f, 0.0f, 130.0f));
        mediumHouse.forEach(model -> {
            model.textureID = textureIDS.get(TextureEnum.MEDIUM_HOUSE);
            model.setRotationY(180.0f);
        });
        List<Model> mediumHouse2 = MeshLoader.load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\mediumhouse2.txt",
                new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(110.0f, 0.0f, 110.0f));
        mediumHouse2.forEach(model -> {
            model.textureID = textureIDS.get(TextureEnum.MEDIUM_HOUSE_2);
            model.setRotationY(150.0f);
        });
        List<Model> bigHouse = MeshLoader.load("C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\models\\bighouse.txt",
                new Vector3f(0.4f, 0.4f, 0.4f), new Vector3f(190.0f, 0.0f, 190.0f));
        bigHouse.forEach(model -> {
            model.textureID = textureIDS.get(TextureEnum.BIG_HOUSE);
            model.setRotationY(0.0f);
        });

        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, 1920, 1080);

        glBlendFunc(GL_SRC0_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        shaderProgram.use();
        shaderProgramAlpha.use();

        int textureTypeUniformLocation = GL20.glGetUniformLocation(shaderProgram.getProgramID(), "textureType");
        glUniform1i(textureTypeUniformLocation, 0);

        int textureTypeUniformLocationAlpha = GL20.glGetUniformLocation(shaderProgramAlpha.getProgramID(), "textureType");
        glUniform1i(textureTypeUniformLocationAlpha, 0);

        double fpsLimit = 1.0 / 30.0;
        double lastUpdateTime = 0;  // number of seconds since the last loop
        double lastFrameTime = 0;   // number of seconds since the last frame

        glEnable(GL_CULL_FACE);
//        glFrontFace(GL_CCW);
//        glCullFace(GL_FRONT);

        while (!glfwWindowShouldClose(displayID)) {
            double now = glfwGetTime();
            double deltaTime = now - lastUpdateTime;
            deltaTimeGlobal = (float) (now - lastFrameTime);

            if ((now - lastFrameTime) >= fpsLimit) {
                glClearColor(1, 1, 1, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                mouse.update();

                mapModel.forEach(model -> {
                    model.setMvp(projectionMatrix(), shaderProgram);
                    model.setTextureType(textureTypeUniformLocation, 0);
                    renderer.render(model);
                });

                gameData.update(deltaTimeGlobal);

//                skyCube.setLocation(shaderProgram);
//                skyCube.setTextureType(textureTypeUniformLocation, 1);
//                renderer.render(skyCube);

                //buildings
                mediumHouse.forEach(model -> {
                    model.setMvp(projectionMatrix(), shaderProgram);
                    model.setTextureType(textureTypeUniformLocation, 0);
                    renderer.render(model);
                });
                mediumHouse2.forEach(model -> {
                    model.setMvp(projectionMatrix(), shaderProgram);
                    model.setTextureType(textureTypeUniformLocation, 0);
                    renderer.render(model);
                });
                bigHouse.forEach(model -> {
                    model.setMvp(projectionMatrix(), shaderProgram);
                    model.setTextureType(textureTypeUniformLocation, 0);
                    renderer.render(model);
                });

                glEnable(GL_BLEND);
                grass.forEach(model -> {
                    model.setMvp(projectionMatrix(), shaderProgramAlpha);
                    model.setTextureType(textureTypeUniformLocationAlpha, 0);
                    renderer.render(model);
                });
                glDisable(GL_BLEND);

                glfwSwapBuffers(displayID);
                glfwPollEvents();

                // only set lastFrameTime when you actually draw something
                lastFrameTime = now;
            }

            // set lastUpdateTime every iteration
            lastUpdateTime = now;
        }

        shaderProgram.stop();
        shaderProgram.delete();
        shaderProgramAlpha.stop();
        shaderProgramAlpha.delete();
 //       tileModels.forEach(Model::delete);
   //     nonTileModels.delete();
        mapModel.forEach(Model::delete);
        background.delete();
        grass.forEach(Model::delete);
        cylinder.forEach(Model::delete);
        skyCube.delete();
        mediumHouse.forEach(Model::delete);
        mediumHouse2.forEach(Model::delete);
        bigHouse.forEach(Model::delete);
        gameData.delete();
    }

    private Matrix4f projectionMatrix() {
        float windowAspect = (float)width / (float)height;
        float near = 0.1f;
        float far = 1000.0f;
        Matrix4f projection = new Matrix4f();
        projection.perspective(Math.toRadians(80.0f), windowAspect, near, far);
        return projection;
    }
}
