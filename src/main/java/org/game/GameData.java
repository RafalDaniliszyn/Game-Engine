package org.game;

import org.game.component.Component;
import org.game.entity.Entity;
import org.game.entity.PlayerEntity;
import org.game.entity.StaticObjectEntity;
import org.game.entity.TreeEntity;
import org.game.renderer.ShaderProgram;
import org.game.renderer.TextureEnum;
import org.game.renderer.TextureManager;
import org.game.system.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameData {

    Map<Component, String> components = new HashMap<>();
    Map<Long, Entity> entities = new HashMap<>();
    Map<String, BaseSystem> systems = new HashMap<>();
    MeshManager meshManager;
    TextureManager textureManager;
    ShaderProgram shaderProgram;
    Matrix4f projection;
    float deltaTime;


    public GameData(ShaderProgram shaderProgram, Matrix4f projection) {
        this.shaderProgram = shaderProgram;
        this.projection = projection;
        textureManager = new TextureManager();
        int treeTextureID = textureManager.getTextures().get(TextureEnum.TREE_COLORS);
        meshManager = new MeshManager(textureManager);
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                TreeEntity tree = new TreeEntity(treeTextureID, random.nextInt(1000)-500, random.nextInt(1000)-500);
                entities.put(tree.getId(), tree);
            }
        }

//        TreeEntity tree = new TreeEntity(IdGenerator.getNextId(), treeID, 0.0f, 0.0f);
//        tree.addComponent(new GrowthComponent(1, 1000.0f, 5));
//        entities.put(tree.getId(), tree);


            StaticObjectEntity tower = new StaticObjectEntity(meshManager, 6L, new Vector3f(0.0f, 0.0f, 0.0f),
                    new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
            entities.put(tower.getId(), tower);

//        for (int i = 0; i < 50; i++) {
//            StaticObjectEntity tower = new StaticObjectEntity(meshManager, 1L, new Vector3f(750.0f, 0.0f, i*15),
//                    new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
//            entities.put(tower.getId(), tower);
//        }
//        for (int i = 0; i < 50; i++) {
//            StaticObjectEntity tower = new StaticObjectEntity(meshManager, 1L, new Vector3f(0.0f, 0.0f, i*15),
//                    new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
//            entities.put(tower.getId(), tower);
//        }
//        for (int i = 0; i < 50; i++) {
//            StaticObjectEntity tower = new StaticObjectEntity(meshManager, 1L, new Vector3f(i*15, 0.0f, 750.0f),
//                    new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
//            entities.put(tower.getId(), tower);
//        }

        PlayerEntity player = new PlayerEntity(meshManager);
        entities.put(player.getId(), player);

        //Growth
        GrowthSystem growthSystem = new GrowthSystem(this);
        systems.put("growthSystem", growthSystem);
        //Move
        MoveSystem moveSystem = new MoveSystem(this);
        systems.put("moveSystem", moveSystem);
        //Collision
        CollisionSystem collisionSystem = new CollisionSystem(this);
        systems.put("collisionSystem", collisionSystem);
        //Render
        RenderSystem renderSystem = new RenderSystem(this);
        systems.put("renderSystem", renderSystem);
    }

    public void init() {
        systems.forEach((s, systems) -> {
            systems.init();
        });
    }
    public void update(float deltaTime) {
        systems.forEach((s, system) -> {
            system.update(deltaTime);
        });
    }

    public void delete() {
        systems.forEach((s, systems) -> {
            systems.delete();
        });
    }

    //    public GameData() {
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j++) {
//                float x = random.nextInt(1000)-500;
//                float z = random.nextInt(1000)-500;
//                while ((x > 100.0f && x < 200.0f) && (z > 100.0f && z < 200.0f)) {
//                    x = random.nextInt(1000)-500;
//                    z = random.nextInt(1000)-500;
//                }
//                TreeEntity treeEntity = new TreeEntity(1, new PositionComponent(new Vector3f(x, 0.0f, z),
//                        0.0f, (float)random.nextInt(180), 0.0f,
//                        new Vector3f(1.0f + random.nextFloat(), 1.0f + random.nextFloat(), 1.0f + random.nextFloat())));
//                components.put(treeEntity, "tree");
//            }
//        }
//    }

//    public void render(Renderer renderer, ShaderProgram shaderProgram, int textureUniformType, Matrix4f uView, Matrix4f uProjection) {
//        components.forEach(((component, s) -> {
//            component.render(gameModels.getModel(s), shaderProgram, renderer, textureUniformType, uView, uProjection);
//        }));
//    }

    public Map<Component, String> getComponents() {
        return components;
    }

    @SafeVarargs
    public final Map<Long, Entity> getEntities(Class<? extends Component>... componentClass) {
        Map<Long, Entity> result = new HashMap<>();
        entities.forEach((id, entity)-> {
            boolean anyMatch = Arrays.stream(componentClass)
                    .allMatch(component -> entity.getComponentList().stream()
                            .anyMatch(component1 -> component.isAssignableFrom(component1.getClass())));
            if (anyMatch) {
                 result.put(entity.getId(), entity);
            }
        });
        return result;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public MeshManager getMeshManager() {
        return meshManager;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }
}
