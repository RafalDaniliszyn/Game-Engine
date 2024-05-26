package org.game;

import org.game.component.Component;
import org.game.component.PositionComponent;
import org.game.editWindow.Frame;
import org.game.entity.Entity;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.entity.PlayerEntity2D;
import org.game.isometric.renderer.Renderer2D;
import org.game.isometric.system.AnimationSystem2D;
import org.game.isometric.system.CollisionSystem2D;
import org.game.isometric.system.DestroySystem2D;
import org.game.isometric.system.DragSystem;
import org.game.isometric.system.GameStateSystem;
import org.game.isometric.system.MoveSystem2D;
import org.game.isometric.system.StateChangedSystem2D;
import org.game.isometric.system.TileActionSystem;
import org.game.isometric.texture2D.TextureEnum2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.isometric.utils.EntityUtils;
import org.game.isometric.worldMap.MapEditor;
import org.game.isometric.worldMap.WorldMapData;
import org.game.system.shader.ShaderManager;
import org.game.system.BaseSystem;
import org.game.event.EventManager;
import org.game.event.EventObserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * This class is used for testing functionality during development.
 */
public class GameData {
    private final Map<Component, String> components = new HashMap<>();
    private final Map<Long, Entity> entities = new LinkedHashMap<>();

    //This must be a LinkedHashMap due to rendering order.
    private final Map<String, BaseSystem> systems = new LinkedHashMap<>();
    private final Map<String, EventManager> eventManagers = new HashMap<>();
 //   private MapData mapData;
    private WorldMapData worldMapData;
//    private final MeshManager meshManager;
//    private final TextureManager textureManager;
    private float[] mapVert;
    private long playerId;
    private ShaderManager shaderManager;
    private float[][] heightMap;
    private Long skyId;
    private boolean active;

    public GameData() {
        active = false;
        //textureManager = new TextureManager();
        //textureManager2D = new TextureManager2D();
        //meshManager = new MeshManager(textureManager);
        shaderManager = new ShaderManager();
        //test3d();
        TextureManager2D.loadTextures();
        BlocksReader.readBlocks();
        Frame.addTexturesToComboBox();
        test2d();
    }

    public void init() {
        systems.forEach((s, systems) -> {
            systems.init();
        });
    }

    Map<String, Long> executionTime = new HashMap<>();
    long times = 0;

    public void update(float deltaTime) {
        if (!active) {
            return;
        }
        eventManagers.forEach((s, eventManager) -> {
            eventManager.notifyObservers();
        });
        systems.forEach((s, system) -> {
            ExecutionTimeChecker executionTimeChecker = new ExecutionTimeChecker();
            executionTimeChecker.start();
            system.update(deltaTime);
            long stop = executionTimeChecker.stop(s);

//            if (executionTime.containsKey(s)) {
//                Long time = executionTime.get(s);
//                executionTime.put(s, time + stop);
//            } else {
//                executionTime.put(s, stop);
//            }
        });
        times += 1;
//        if (times >= 100) {
//            times = 0;
//            List<Map.Entry<String, Long>> list = new ArrayList<>(executionTime.entrySet());
//            list.sort(Map.Entry.comparingByValue());
//            Map<String, Long> sortedMap = new LinkedHashMap<>();
//            for (Map.Entry<String, Long> entry : list) {
//                sortedMap.put(entry.getKey(), entry.getValue());
//            }
//            sortedMap.forEach((key, value) -> System.out.println(key + " : " + value));
//        }


    }

    public void delete() {
        systems.forEach((s, systems) -> {
            systems.delete();
        });
    }

    public Map<Component, String> getComponents() {
        return components;
    }



    /**
     * Method for 3D module.
     */
    @SafeVarargs
    public final Map<Long, Entity> getEntities(Class<? extends Component>... componentClass) {
        Map<Long, Entity> result = new HashMap<>();
        entities.forEach((id, entity) -> {
            boolean anyMatch = Arrays.stream(componentClass)
                    .allMatch(component -> entity.getComponentList().stream()
                            .anyMatch(component1 -> component.isAssignableFrom(component1.getClass())));
            if (anyMatch) {
                 result.put(entity.getId(), entity);
            }
        });
        return result;
    }

    public final Map<Long, Entity> getEntities(ComponentEnum... componentEnum) {
        Map<Long, Entity> result = new HashMap<>();
        entities.forEach((id, entity) -> {
            boolean containsAllComponents = EntityUtils.containsComponents(entity, componentEnum);
            if (containsAllComponents) {
                result.put(entity.getId(), entity);
            }
        });
        return result;
    }

    public final Map<Long, Entity> getEntities(Predicate<Entity> predicate, ComponentEnum... componentEnum) {
        Map<Long, Entity> result = new HashMap<>();
        entities.forEach((id, entity) -> {
            if (predicate.test(entity)) {
                boolean containsAllComponents = EntityUtils.containsComponents(entity, componentEnum);
                if (containsAllComponents) {
                    result.put(entity.getId(), entity);
                }
            }
        });
        return result;
    }

    public final Map<Long, Entity> getEntitiesWithPredicate(Predicate<Entity> predicate) {
        Map<Long, Entity> result = new HashMap<>();
        entities.forEach((id, entity) -> {
            if (predicate.test(entity)) {
                result.put(entity.getId(), entity);
            }
        });
        return result;
    }


    public void addEventManagerObserver(Class<? extends EventManager> eventManager, EventObserver eventObserver) {
        eventManagers.forEach((s, manager) -> {
            if (manager.getClass().isAssignableFrom(eventManager)) {
                manager.addObserver(eventObserver);
            }
        });
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
        List<ComponentEnum> componentEnumList = entity.getComponentEnumList();
        systems.forEach((name, system) -> {
            List<ComponentEnum> requiredComponents = system.getRequiredComponents();
            if (new HashSet<>(componentEnumList).containsAll(requiredComponents)) {
                system.addEntityToProcess(entity.getId());
            }
        });
    }

    public void removeEntity(Long entityId) {
        entities.remove(entityId);
        systems.forEach((systemName, system) -> {
            system.removeEntity(entityId);
        });
    }

    public Entity getEntity(Long id) {
        return entities.get(id);
    }

    public Map<Long, Entity> getEntities(Predicate<Entity> predicate, Set<Long> entityIdSet) {
        Map<Long, Entity> entityList = new HashMap<>();
        entityIdSet.forEach(id -> {
            Entity entity = entities.get(id);
            if (entity != null && predicate.test(entity)) {
                entityList.put(id, entity);
            } else if (entity == null) {
                entityIdSet.remove(id);
            }
        });
        return entityList;
    }


//    public MeshManager getMeshManager() {
//        return meshManager;
//    }
//
//    public TextureManager getTextureManager() {
//        return textureManager;
//    }


    public float[] getMapVert() {
        return mapVert;
    }

    public float[][] getHeightMap() {
        return heightMap;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ShaderManager getShaderManager() {
        return shaderManager;
    }

    public WorldMapData getWorldMapData() {
        return worldMapData;
    }

    public Map<String, BaseSystem> getSystems() {
        return systems;
    }

    public void updateSkyPos(float x, float z) {
        Entity sky = getEntity(skyId);
        sky.getComponent(PositionComponent.class).getPosition().x -= x;
        sky.getComponent(PositionComponent.class).getPosition().z -= z;
    }


    private void updateSystemsProcessList() {
        systems.forEach((name, system) -> {
            List<ComponentEnum> requiredComponents = system.getRequiredComponents();
            entities.forEach((id, entity) -> {
                Set<Long> entitiesToProcess = system.getEntitiesToProcess();
                List<ComponentEnum> componentEnumList = entity.getComponentEnumList();
                if (new HashSet<>(componentEnumList).containsAll(requiredComponents)) {
                    system.addEntityToProcess(id);
                } else {
                    entitiesToProcess.remove(id);
                }
            });
        });
    }

    private void test2d() {
        Integer hamTextureId = TextureManager2D.getTextureId(TextureEnum2D.HAM_2D);
        PlayerEntity2D playerEntity2D = new PlayerEntity2D(hamTextureId, 38, 3);

        entities.put(playerEntity2D.getId(), playerEntity2D);
        this.worldMapData = new WorldMapData(this);

        StateChangedSystem2D stateChangedSystem2D = new StateChangedSystem2D(this);
        systems.put("stateChangedSystem2D", stateChangedSystem2D);
        GameStateSystem gameStateSystem = new GameStateSystem(this);
        systems.put("gameStateSystem", gameStateSystem);
        MapEditor mapEditor = new MapEditor(this, worldMapData);
        systems.put("mapEditor", mapEditor);
        DestroySystem2D destroySystem2D = new DestroySystem2D(this);
        systems.put("destroySystem2D", destroySystem2D);
        DragSystem dragSystem = new DragSystem(this);
        systems.put("dragSystem", dragSystem);
        CollisionSystem2D collisionSystem2D = new CollisionSystem2D(this);
        systems.put("collisionSystem2D", collisionSystem2D);
        AnimationSystem2D animationSystem2D = new AnimationSystem2D(this);
        systems.put("animationSystem2D", animationSystem2D);
        MoveSystem2D moveSystem2D = new MoveSystem2D(this);
        systems.put("moveSystem2D", moveSystem2D);
        TileActionSystem tileActionSystem = new TileActionSystem(this);
        systems.put("tileActionSystem", tileActionSystem);
        Renderer2D renderer2D = new Renderer2D(this);
        systems.put("renderer2d", renderer2D);

        updateSystemsProcessList();
    }

//    private void test3d() {
//        //prepareTestData();
//        StaticObjectEntity groundMap = new StaticObjectEntity(meshManager, "tileTest", new Vector3f(0.0f, 0.0f, 0.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//        entities.put(groundMap.getId(), groundMap);
//        groundMap.removeComponent(CollisionComponent.class);
//
//        MeshData groundMeshData = meshManager.getMeshData("tileTest");
//        mapVert = groundMeshData.getVertices();
//
//        heightMap = MapHelper.getHeightMap(mapVert);
//
//        StaticObjectEntity sky = new StaticObjectEntity(meshManager, "background", new Vector3f(0.0f, 0.0f, 0.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//        sky.removeComponent(CollisionComponent.class);
//        skyId = sky.getId();
//        entities.put(sky.getId(), sky);
//
//        StaticObjectEntity oldHouse = new StaticObjectEntity(meshManager, "oldhouse", new Vector3f(2.0f, 0.01f, 4.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//        entities.put(oldHouse.getId(), oldHouse);
//
//        StaticObjectEntity palace = new StaticObjectEntity(meshManager, "palace", new Vector3f(15.0f, 0.0f, 14.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//        entities.put(palace.getId(), palace);
//
//        StaticObjectEntity water = new StaticObjectEntity(meshManager, "water", new Vector3f(0.0f, 0.0f, 0.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false, new EntityProperties(ShaderEnum.WATER));
//        entities.put(water.getId(), water);
//
//        StaticObjectEntity cube = new StaticObjectEntity(meshManager, "cube", new Vector3f(700.0f, 1.0f, 15.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.5f, 0.5f, 0.5f), false, new EntityProperties(ShaderEnum.DEFAULT));
//        entities.put(cube.getId(), cube);
//
//        StaticObjectEntity building = new StaticObjectEntity(meshManager, "building", new Vector3f(600.0f, 0.01f, 40.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false, new EntityProperties(ShaderEnum.DEFAULT));
//        entities.put(building.getId(), building);
//
//        MultipleObjectsEntity grass = new MultipleObjectsEntity(mapVert, meshManager, "grass2",
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 250, false, false);
//        entities.put(grass.getId(), grass);
//
//        MultipleObjectsEntity tree9 = new MultipleObjectsEntity(mapVert, meshManager, "tree9",
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 250, false, false);
//        entities.put(tree9.getId(), tree9);
//
//
////        MultipleObjectsEntity oak = new MultipleObjectsEntity(mapVert, meshManager, "tree2",
////                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 700, false, true);
////        entities.put(oak.getId(), oak);
////
////        MultipleObjectsEntity stones = new MultipleObjectsEntity(mapVert, meshManager, "stones",
////                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 30, false, true);
////        entities.put(stones.getId(), stones);
//
//        LightSourceEntity sun = new LightSourceEntity(meshManager, "sun", new Vector3f(-30.0f, 400.0f, 30.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false, new Vector3f(1.0f, 1.0f, 1.0f));
//        entities.put(sun.getId(), sun);
//
//
//        PlayerEntity player = new PlayerEntity(meshManager, false);
//        playerId = player.getId();
//        entities.put(player.getId(), player);
//
//
//        UiSystem uiSystem = new UiSystem(this);
//        systems.put("interfaceSystem", uiSystem);
//
//        //Growth
//        GrowthSystem growthSystem = new GrowthSystem(this);
//        systems.put("growthSystem", growthSystem);
//        //Move
//        MoveSystem moveSystem = new MoveSystem(this);
//        systems.put("moveSystem", moveSystem);
//        //Collision
//        CollisionSystem collisionSystem = new CollisionSystem(this);
//        systems.put("collisionSystem", collisionSystem);
//
//        //Render System - Must be before UI Render System.
//        RenderSystem renderSystem = new RenderSystem(this);
//        systems.put("renderSystem", renderSystem);
//
//        //UI Render System
//        Integer buttonTextureID = textureManager.getTextures().get(TextureEnum.BUTTON);
//        RawUiModel rawUiModel1 = new RawUiModel(new Vector3f(0.13f, 0.25f, 0.0f), new Vector3f(0.7f, 0.5f, 0.0f), buttonTextureID);
//        RawUiModel rawUiModel2 = new RawUiModel(new Vector3f(0.13f, 0.25f, 0.0f), new Vector3f(0.3f, 0.5f, 0.0f), buttonTextureID);
//
//        UiEntity uiEntity1 = new UiEntity(1, rawUiModel1);
//        UiEntity uiEntity2 = new UiEntity(1, rawUiModel2);
//        entities.put(uiEntity1.getId(), uiEntity1);
//        entities.put(uiEntity2.getId(), uiEntity2);
//        eventManagers.put("uiEntity1", uiEntity1.getEventManager());
//        eventManagers.put("uiEntity2", uiEntity2.getEventManager());
//        addEventManagerObserver(EquipmentEventManager.class, player);
//        UiRendererSystem uiRendererSystem = new UiRendererSystem(this);
//        uiRendererSystem.addGui(rawUiModel1);
//        uiRendererSystem.addGui(rawUiModel2);
//        systems.put("uiRendererSystem", uiRendererSystem);
//    }
//
//    private void prepareTestData() {
//        MeshData groundMeshData = meshManager.getMeshData("baseMap3");
//        mapVert = groundMeshData.getVertices();
//
//        Random random = new Random();
//        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 20; j++) {
//                float scale = random.nextFloat();
//                float rotationY = random.nextInt(360);
//                int offset = random.nextInt(60);
//                StaticObjectEntity grass = new StaticObjectEntity(mapVert, meshManager, "grass2", new Vector3f(((i))+offset, 0, ((j))+offset),
//                        new Vector3f(0.0f, rotationY, 0.0f), new Vector3f(0.9f, 0.6f+scale, 0.9f), false);
//                grass.getComponents(MeshComponent.class).forEach(mesh -> {
//                    mesh.setCullFace(false);
//                });
//                grass.removeComponent(CollisionComponent.class);
//                entities.put(grass.getId(), grass);
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 5; j++) {
//                float scale = random.nextFloat();
//                float rotationY = random.nextInt(360);
//                int offset = random.nextInt(60);
//                StaticObjectEntity oak = new StaticObjectEntity(mapVert, meshManager, "oak", new Vector3f(((i*50))+offset, 0, ((j*40))+offset),
//                        new Vector3f(0.0f, rotationY, 0.0f), new Vector3f(0.8f+scale, 0.8f+scale, 0.8f+scale), false);
//                oak.getComponents(MeshComponent.class).forEach(mesh -> {
//                    mesh.setCullFace(false);
//                });
//                entities.put(oak.getId(), oak);
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 5; j++) {
//                float x = random.nextInt(500);
//                float z = random.nextInt(500);
//                float y = 0.0f;
//                StaticObjectEntity tree2 = new StaticObjectEntity(mapVert, meshManager, "tree2", new Vector3f(x, y, z),
//                        new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//                entities.put(tree2.getId(), tree2);
//            }
//        }
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                float x = random.nextInt(500);
//                float z = random.nextInt(500);
//                float y = PositionHelper.getPositionY(mapVert, x, z);
//                StaticObjectEntity stone = new StaticObjectEntity(mapVert, meshManager, "stones", new Vector3f(x, y, z),
//                        new Vector3f(0.0f, random.nextInt(360), 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//                entities.put(stone.getId(), stone);
//            }
//        }
//
//        StaticObjectEntity fence = new StaticObjectEntity(mapVert, meshManager, "fence", new Vector3f(2.0f, 0.0f, -4.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//        entities.put(fence.getId(), fence);
//
//        for (int i = 0; i < 15; i++) {
//            for (int j = 0; j < 15; j++) {
//                float x = random.nextInt(1000)-500;
//                float z = random.nextInt(1000)-500;
//                float y = PositionHelper.getPositionY(mapVert, x, -z);
//                StaticObjectEntity flower = new StaticObjectEntity(mapVert, meshManager, "flower", new Vector3f(x, y, z),
//                        new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//                entities.put(flower.getId(), flower);
//            }
//        }
//    }
}
