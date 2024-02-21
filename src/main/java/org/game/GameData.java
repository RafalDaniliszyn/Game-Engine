package org.game;

import org.game.component.CollisionComponent;
import org.game.component.Component;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.game.entity.PlayerEntity;
import org.game.entity.StaticObjectEntity;
import org.game.renderer.ShaderProgram;
import org.game.renderer.TextureManager;
import org.game.settingsWindow.Panel;
import org.game.system.*;
import org.joml.Vector3f;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameData {

    private Map<Component, String> components = new HashMap<>();
    private Map<Long, Entity> entities = new HashMap<>();
    private Map<String, BaseSystem> systems = new HashMap<>();
    private MeshManager meshManager;
    private TextureManager textureManager;
    private ShaderProgram shaderProgram;
    private float deltaTime;
    private float[] mapVert;
    private long playerId;

    public GameData(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        textureManager = new TextureManager();
        meshManager = new MeshManager(textureManager);

        prepareTestData();
        StaticObjectEntity groundMap = new StaticObjectEntity(meshManager, "baseMap", new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
        entities.put(groundMap.getId(), groundMap);

        StaticObjectEntity sky = new StaticObjectEntity(meshManager, "background", new Vector3f(0.0f, 0.0f, -140.0f),
                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(10.0f, 10.0f, 10.0f), false);
        sky.removeComponent(CollisionComponent.class);
        entities.put(sky.getId(), sky);

        PlayerEntity player = new PlayerEntity(meshManager, true);
        playerId = player.getId();
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
        updateSettings();
        systems.forEach((s, system) -> {
            system.update(deltaTime);
        });
    }

    public void delete() {
        systems.forEach((s, systems) -> {
            systems.delete();
        });
    }

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

    public Entity getEntity(Long id) {
        return entities.get(id);
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
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

    public float[] getMapVert() {
        return mapVert;
    }

    private void updateSettings() {
        Panel.ROTATION = getEntity(playerId).getComponent(PositionComponent.class).getRotationY();
    }

    private void prepareTestData() {
        MeshData groundMeshData = meshManager.getMeshData("baseMap");
        mapVert = groundMeshData.getVertices();
        StaticObjectEntity houseTest = new StaticObjectEntity(meshManager, "housetest", new Vector3f(0, 0, -140),
                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);
        entities.put(houseTest.getId(), houseTest);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                float scale = random.nextFloat();
                float rotationY = random.nextInt(360);
                int offset = random.nextInt(60);
                StaticObjectEntity grass = new StaticObjectEntity(meshManager, "grass1", new Vector3f(((i*20)-100)+offset, 0, -((j*20))+offset),
                        new Vector3f(0.0f, rotationY, 0.0f), new Vector3f(0.9f, 0.6f+scale, 0.9f), false);
                grass.removeComponent(CollisionComponent.class);
                entities.put(grass.getId(), grass);
            }
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                float scale = random.nextFloat();
                float rotationY = random.nextInt(360);
                int offset = random.nextInt(60);
                StaticObjectEntity oak = new StaticObjectEntity(meshManager, "oak", new Vector3f(((i*30)-100)+offset, 0, -((j*30))+offset),
                        new Vector3f(0.0f, rotationY, 0.0f), new Vector3f(0.8f+scale, 0.8f+scale, 0.8f+scale), false);
                entities.put(oak.getId(), oak);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float x = random.nextInt(1300)-1150;
                float z = random.nextInt(1300)-1300;
                float y = 0.0f;
                StaticObjectEntity tree2 = new StaticObjectEntity(meshManager, "tree2", new Vector3f(x, y, z),
                        new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);
                entities.put(tree2.getId(), tree2);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float x = random.nextInt(100)-50;
                float z = random.nextInt(100)-50;
                float y = MeshLoader.getPositionY(groundMeshData.getVertices(), x, z);
                StaticObjectEntity stone = new StaticObjectEntity(meshManager, "stones", new Vector3f(x, y, z),
                        new Vector3f(0.0f, random.nextInt(360), 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);
                entities.put(stone.getId(), stone);
            }
        }

        StaticObjectEntity fence = new StaticObjectEntity(meshManager, "fence", new Vector3f(2.0f, 0.0f, -4.0f),
                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);
        entities.put(fence.getId(), fence);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                float x = random.nextInt(1000)-500;
                float z = random.nextInt(1000)-500;
                float y = MeshLoader.getPositionY(groundMeshData.getVertices(), x, -z);
                StaticObjectEntity flower = new StaticObjectEntity(meshManager, "flower", new Vector3f(x, y, z),
                        new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);
                entities.put(flower.getId(), flower);
            }
        }
        StaticObjectEntity tower = new StaticObjectEntity(meshManager, "tower", new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), true);
        entities.put(tower.getId(), tower);
    }
}
