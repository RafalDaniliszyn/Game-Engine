package org.game;

import org.game.component.Component;
import org.game.component.PositionComponent;
import org.game.entity.Entity;
import org.game.entity.EntityType;
import org.game.isometric.Camera2D;
import org.game.isometric.GameLoadingState;
import org.game.isometric.GameState;
import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.entity.PlayerEntity2D;
import org.game.isometric.event.EventPublisher;
import org.game.isometric.renderer.Renderer2D;
import org.game.isometric.system.*;
import org.game.isometric.system.destroySystem.DestroySystem2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.isometric.utils.EntityUtils;
import org.game.isometric.worldMap.MapEditor;
import org.game.isometric.worldMap.WorldMapData;
import org.game.network.client.GameClient;
import org.game.network.client.serverMap.MapLoader;
import org.game.system.shader.ShaderManager;
import org.game.system.BaseSystem;
import org.game.event.EventManager;
import org.game.event.EventObserver;
import org.game.ui.component.ItemUiContainer;
import org.game.ui.component.SelectItemButton;
import org.game.ui.newUiConcept.UiMainMenuScreen;
import org.game.ui.system.UiRendererSystem;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class is used for testing functionality during development.
 */
public class GameData {
    private static final Logger logger = LoggerFactory.getLogger(GameData.class);
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
    private GameClient gameClient;

    public static GameData gameData;

    public GameData() throws InterruptedException {
        active = false;
        shaderManager = new ShaderManager();
        TextureManager2D.loadTextures();
        gameClient = GameClient.getInstance();
        gameData = this;
        BlocksReader.readBlocks();
        waitForGameClient();
        test2d();
    }

    public void init() throws InterruptedException {
        systems.forEach((s, systems) -> {
            systems.init();
        });
    }

    public void update(float deltaTime) {
        if (!active) {
            return;
        }
        eventManagers.forEach((s, eventManager) -> {
            eventManager.notifyObservers();
        });
        EventPublisher.getInstance().fireGlContextTasks();
        systems.forEach((s, system) -> {
            system.update(deltaTime);
        });
    }

    public void delete() {
        systems.forEach((s, systems) -> {
            systems.delete();
        });
        gameClient.shutdown();
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
        Set<ComponentEnum> componentEnumList = entity.getComponentEnumSet();
        systems.forEach((name, system) -> {
            List<ComponentEnum> requiredComponents = system.getRequiredComponents();
            if (componentEnumList.containsAll(requiredComponents)) {
                system.addEntityToProcess(entity.getId());
            }
        });
    }

    public void putEntity(Entity entity) {
        entities.put(entity.getId(), entity);
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

    private void waitForGameClient() {
        Thread thread = new Thread(() -> {
            while (!MapLoader.mapReceived) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread.interrupt();
    }

    private void updateSystemsProcessList() {
        Set<ComponentEnum> added = new HashSet<>();
        Map<String, Set<ComponentEnum>> addedEntityMap = new HashMap<>();
        systems.forEach((name, system) -> {
            addedEntityMap.put(name, new HashSet<>());
        });

        systems.forEach((name, system) -> {
            List<ComponentEnum> requiredComponents = system.getRequiredComponents();
            entities.forEach((id, entity) -> {
                if (id == 126) {
                    System.out.println(entity);
                }
                Set<Long> entitiesToProcess = system.getEntitiesToProcess();
                Set<ComponentEnum> componentEnumList = entity.getComponentEnumSet();
                if (componentEnumList.containsAll(requiredComponents)) {
                    system.addEntityToProcess(id);
                    added.addAll(new HashSet<>(componentEnumList));
                } else {
                    entitiesToProcess.remove(id);
                }
                addedEntityMap.put(name, added);
                if (id == 126) {
                    System.out.println(entity);
                }
            });
        });
    }

    private void test2d() {
        //createPlayer();
        this.worldMapData = new WorldMapData(this);

        System.out.println("Systems initialize");
        StateChangedSystem2D stateChangedSystem2D = new StateChangedSystem2D(this);
        systems.put("stateChangedSystem2D", stateChangedSystem2D);
        GameStateSystem gameStateSystem = new GameStateSystem(this);
        systems.put("gameStateSystem", gameStateSystem);
        MapEditor mapEditor = new MapEditor(this);
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

        //UI Render System

        UiRendererSystem uiRendererSystem = new UiRendererSystem(this);

        UiMainMenuScreen uiMainMenuScreen = new UiMainMenuScreen(300, 400);
        uiRendererSystem.addUiContainers(uiMainMenuScreen.getContainers());


//        uiRendererSystem.addGui(new SelectItemButton(10, 600, 50, 50, "DYNAMITE"    , BlocksReader.getTextureIdByItemLabel("DYNAMITE"    ), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 540, 50, 50, "HOLE_UP_2D"  , BlocksReader.getTextureIdByItemLabel("HOLE_UP_2D"  ), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 480, 50, 50, "HOLE_DOWN_2D", BlocksReader.getTextureIdByItemLabel("HOLE_DOWN_2D"), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 420, 50, 50, "IRON_ORE"    , BlocksReader.getTextureIdByItemLabel("IRON_ORE"    ), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 360, 50, 50, "SAND_2D"     , BlocksReader.getTextureIdByItemLabel("SAND_2D"     ), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 300, 50, 50, "DIRT_2D"     , BlocksReader.getTextureIdByItemLabel("DIRT_2D"     ), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 240, 50, 50, "WATER_2D"    , BlocksReader.getTextureIdByItemLabel("WATER_2D"    ), GLFW_MOUSE_BUTTON_1));
//        uiRendererSystem.addGui(new SelectItemButton(10, 180, 50, 50, "GRASS_2D"    , BlocksReader.getTextureIdByItemLabel("GRASS_2D"    ), GLFW_MOUSE_BUTTON_1));

        List<SelectItemButton> uiElementList = List.of(
            new SelectItemButton(20, 600, 50, 50, "DYNAMITE"    , BlocksReader.getTextureIdByItemLabel("DYNAMITE"    ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(20, 540, 50, 50, "HOLE_UP_2D"  , BlocksReader.getTextureIdByItemLabel("HOLE_UP_2D"  ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(20, 480, 50, 50, "HOLE_DOWN_2D", BlocksReader.getTextureIdByItemLabel("HOLE_DOWN_2D"), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(20, 420, 50, 50, "IRON_ORE"    , BlocksReader.getTextureIdByItemLabel("IRON_ORE"    ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(20, 360, 50, 50, "SAND_2D"     , BlocksReader.getTextureIdByItemLabel("SAND_2D"     ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 300, 50, 50, "DIRT_2D"     , BlocksReader.getTextureIdByItemLabel("DIRT_2D"     ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 240, 50, 50, "WATER_2D"    , BlocksReader.getTextureIdByItemLabel("WATER_2D"    ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "GRASS_2D"    , BlocksReader.getTextureIdByItemLabel("GRASS_2D"    ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "DIAMOND_ORE" , BlocksReader.getTextureIdByItemLabel("DIAMOND_ORE" ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "GOLD_ORE"    , BlocksReader.getTextureIdByItemLabel("GOLD_ORE"    ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "GOLD_2D"     , BlocksReader.getTextureIdByItemLabel("GOLD_2D"     ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "CUT_TREE"    , BlocksReader.getTextureIdByItemLabel("CUT_TREE"    ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "TREE_2D"     , BlocksReader.getTextureIdByItemLabel("TREE_2D"     ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "CEREAL"      , BlocksReader.getTextureIdByItemLabel("CEREAL"   ), GLFW_MOUSE_BUTTON_1),
            new SelectItemButton(10, 180, 50, 50, "BRICK_2D"    , BlocksReader.getTextureIdByItemLabel("BRICK_2D"   ), GLFW_MOUSE_BUTTON_1)
        );
        ItemUiContainer itemUiContainer = new ItemUiContainer(uiElementList, 500, 600, 5, 3, 10);
        uiRendererSystem.addGuiContainer(itemUiContainer);

//        List<AbstractUiElement> menuElementList = List.of(
//                new MenuButton(228, 45, GLFW_MOUSE_BUTTON_1),
//                new MenuButton(228, 45, GLFW_MOUSE_BUTTON_1),
//                new MenuButton(228, 45, GLFW_MOUSE_BUTTON_1),
//                new MenuButton(228, 45, GLFW_MOUSE_BUTTON_1),
//                new MenuButton(228, 45, GLFW_MOUSE_BUTTON_1)
//        );
//        MenuContainer menuContainer = new MenuContainer(menuElementList, new MenuLayout());
//        uiRendererSystem.addUiContainer(menuContainer);


        //

//        Renderer2DV1 renderer2DV1 = new Renderer2DV1(this);
//        systems.put("renderer2dv1", renderer2DV1);

        Renderer2D renderer2D = new Renderer2D(this);
        systems.put("renderer2d", renderer2D);
        systems.put("uiRendererSystem", uiRendererSystem);
        System.out.println("Systems initialize END");
        updateSystemsProcessList();
    }

    private void test3d() {
//        prepareTestData();
//        StaticObjectEntity groundMap = new StaticObjectEntity(meshManager, "baseMap3", new Vector3f(0.0f, 0.0f, 0.0f),
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), false);
//        entities.put(groundMap.getId(), groundMap);
//        groundMap.removeComponent(CollisionComponent.class);
//
//        MeshData groundMeshData = meshManager.getMeshData("baseMap3");
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
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 25, false, false);
//        entities.put(grass.getId(), grass);
//
//        MultipleObjectsEntity tree9 = new MultipleObjectsEntity(mapVert, meshManager, "tree9",
//                new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 25, false, false);
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

        //UI Render System
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
      //  systems.put("uiRendererSystem", uiRendererSystem);
    }

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
