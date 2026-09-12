package org.game.isometric.system;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.utils.EntityUtils;
import org.game.isometric.utils.TileUtils;
import org.game.isometric.worldMap.WorldMapData;

import java.util.*;

import static org.game.isometric.blockLoader.Side.DOWN;
import static org.game.isometric.blockLoader.Side.DOWN_LEFT;
import static org.game.isometric.blockLoader.Side.DOWN_RIGHT;
import static org.game.isometric.blockLoader.Side.LEFT;
import static org.game.isometric.blockLoader.Side.LEFT_DOWN_RIGHT;
import static org.game.isometric.blockLoader.Side.LEFT_RIGHT;
import static org.game.isometric.blockLoader.Side.LEFT_UP_DOWN;
import static org.game.isometric.blockLoader.Side.LEFT_UP_RIGHT;
import static org.game.isometric.blockLoader.Side.RIGHT;
import static org.game.isometric.blockLoader.Side.RIGHT_UP_DOWN;
import static org.game.isometric.blockLoader.Side.UP;
import static org.game.isometric.blockLoader.Side.UP_DOWN;
import static org.game.isometric.blockLoader.Side.UP_LEFT;
import static org.game.isometric.blockLoader.Side.UP_RIGHT;

public class SwapEdgeHelper {

    private static final int[][] L =
            {
                    {0, 1, 0},
                    {0, 0, 1},
                    {0, 1, 0}
            };

    private static final int[][] R =
            {
                    {0, 1, 0},
                    {1, 0, 0},
                    {0, 1, 0}
            };

    private static final int[][] U =
            {
                    {0, 0, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };

    private static final int[][] D =
            {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 0, 0}
            };

    private static final int[][] L_U =
            {
                    {0, 0, 0},
                    {0, 0, 1},
                    {0, 1, 0}
            };

    private static final int[][] R_U =
            {
                    {0, 0, 0},
                    {1, 0, 0},
                    {0, 1, 0}
            };

    private static final int[][] L_D =
            {
                    {0, 1, 0},
                    {0, 0, 1},
                    {0, 0, 0}
            };

    private static final int[][] R_D =
            {
                    {0, 1, 0},
                    {1, 0, 0},
                    {0, 0, 0}
            };

    private static final int[][] L_U_D =
            {
                    {0, 0, 0},
                    {0, 0, 1},
                    {0, 0, 0}
            };

    private static final int[][] R_U_D =
            {
                    {0, 0, 0},
                    {1, 0, 0},
                    {0, 0, 0}
            };

    private static final int[][] L_U_R =
            {
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 1, 0}
            };

    private static final int[][] L_D_R =
            {
                    {0, 1, 0},
                    {0, 0, 0},
                    {0, 0, 0}
            };


    private static final int[][] L_R =
            {
                    {0, 1, 0},
                    {0, 0, 0},
                    {0, 1, 0}
            };

    private static final int[][] U_D =
            {
                    {0, 0, 0},
                    {1, 0, 1},
                    {0, 0, 0}
            };

    private static final int[][] CENTER =
            {
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
            };

    private static final Map<int[][], Side> sideMap;

    static {
        sideMap = new HashMap<>();
        sideMap.put(L, LEFT);
        sideMap.put(R, RIGHT);
        sideMap.put(U, UP);
        sideMap.put(D, DOWN);

        sideMap.put(L_U, UP_LEFT);
        sideMap.put(R_U, UP_RIGHT);
        sideMap.put(L_D, DOWN_LEFT);
        sideMap.put(R_D, DOWN_RIGHT);

        sideMap.put(L_U_D, LEFT_UP_DOWN);
        sideMap.put(R_U_D, RIGHT_UP_DOWN);
        sideMap.put(L_U_R, LEFT_UP_RIGHT);
        sideMap.put(L_D_R, LEFT_DOWN_RIGHT);

        sideMap.put(L_R, LEFT_RIGHT);
        sideMap.put(U_D, UP_DOWN);
        sideMap.put(CENTER, Side.CENTER);
    }

    public static void changeAround(int floor, int tileX, int tileY) {
        swap(floor, tileX - 1, tileY);
        swap(floor, tileX, tileY + 1);
        swap(floor, tileX + 1, tileY);
        swap(floor, tileX, tileY - 1);
    }

    public static void changeAround(Deque<Long>[][] map, int tileX, int tileY) {
        swap(map, tileX - 1, tileY);
        swap(map, tileX, tileY + 1);
        swap(map, tileX + 1, tileY);
        swap(map, tileX, tileY - 1);
    }

    public static void changeEdges(Deque<Side>[][] edges, Deque<Long>[][] map, int tileX, int tileY) {
        swapEdges(edges, map, tileX - 1, tileY);
        swapEdges(edges, map, tileX, tileY + 1);
        swapEdges(edges, map, tileX + 1, tileY);
        swapEdges(edges, map, tileX, tileY - 1);
    }

    private static void swapEdges(Deque<Side>[][] edges, Deque<Long>[][] map, int tileX, int tileY) {
        GameData gameData = GameData.gameData;
        if (!(tileX >= 0 && tileY >= 0 && tileX < map.length && tileY < map.length)) {
            return;
        }
        Long centerEntityId = map[tileX][tileY].peekFirst();
        if (centerEntityId == null) {
            return;
        }
        Entity centerEntity = gameData.getEntity(centerEntityId);
        if (centerEntity != null) {
            //LEFT
            Entity entityLeft = null;
            if (tileX - 1 >= 0) {
                Long entityIdOnLeft = map[tileX - 1][tileY].peekFirst();
                entityLeft = gameData.getEntity(entityIdOnLeft);
            }

            //UP
            Entity entityUp = null;
            if (tileY + 1 < map.length) {
                Long entityIdOnUp = map[tileX][tileY + 1].peekFirst();
                entityUp = gameData.getEntity(entityIdOnUp);
            }

            //RIGHT
            Entity entityRight = null;
            if (tileX + 1 < map.length) {
                Long entityIdOnRight = map[tileX + 1][tileY].peekFirst();
                entityRight = gameData.getEntity(entityIdOnRight);
            }

            //DOWN
            Entity entityDown = null;
            if (tileY - 1 >= 0) {
                Long entityIdOnDown = map[tileX][tileY - 1].peekFirst();
                entityDown = gameData.getEntity(entityIdOnDown);
            }

            int left = EntityUtils.compareLabels(entityLeft, centerEntity) ? 1:0;
            int up = EntityUtils.compareLabels(entityUp, centerEntity) ? 1:0;
            int right = EntityUtils.compareLabels(entityRight, centerEntity) ? 1:0;
            int down = EntityUtils.compareLabels(entityDown, centerEntity) ? 1:0;

            int[][] edgeCompatibility = {
                    {0,     up,     0},
                    {left,   0, right},
                    {0,   down,     0},
            };

            Side matchingEdge = getMatchingEdge(edgeCompatibility);
            edges[tileX][tileY].pollFirst();
            edges[tileX][tileY].offerFirst(matchingEdge);
        }
    }

    private static void swap(Deque<Long>[][] map, int tileX, int tileY) {
        GameData gameData = GameData.gameData;
        if (!(tileX >= 0 && tileY >= 0 && tileX < map.length && tileY < map.length)) {
            return;
        }
        Long centerEntityId = map[tileX][tileY].peekFirst();
        if (centerEntityId == null) {
            return;
        }
        Entity centerEntity = gameData.getEntity(centerEntityId);
        if (centerEntity != null) {
            //LEFT
            Entity entityLeft = null;
            if (tileX - 1 >= 0) {
                Long entityIdOnLeft = map[tileX - 1][tileY].peekFirst();
                entityLeft = gameData.getEntity(entityIdOnLeft);
            }

            //UP
            Entity entityUp = null;
            if (tileY + 1 < map.length) {
                Long entityIdOnUp = map[tileX][tileY + 1].peekFirst();
                entityUp = gameData.getEntity(entityIdOnUp);
            }

            //RIGHT
            Entity entityRight = null;
            if (tileX + 1 < map.length) {
                Long entityIdOnRight = map[tileX + 1][tileY].peekFirst();
                entityRight = gameData.getEntity(entityIdOnRight);
            }


            //DOWN
            Entity entityDown = null;
            if (tileY - 1 >= 0) {
                Long entityIdOnDown = map[tileX][tileY - 1].peekFirst();
                entityDown = gameData.getEntity(entityIdOnDown);
            }

            int left = EntityUtils.compareLabels(entityLeft, centerEntity) ? 1:0;
            int up = EntityUtils.compareLabels(entityUp, centerEntity) ? 1:0;
            int right = EntityUtils.compareLabels(entityRight, centerEntity) ? 1:0;
            int down = EntityUtils.compareLabels(entityDown, centerEntity) ? 1:0;

            int[][] edgeCompatibility = {
                    {0,     up,     0},
                    {left,   0, right},
                    {0,   down,     0},
            };

            Optional<Long> entityIdToReplace = getEntityIdToReplace(edgeCompatibility, centerEntity);
            entityIdToReplace.ifPresent(id -> TileUtils.replaceEntityOnBottom(id, map[tileX][tileY]));
        }
    }

    private static void swap(int floor, int tileX, int tileY) {
        GameData gameData = GameData.gameData;
        WorldMapData worldMapData = gameData.getWorldMapData();
        Long centerEntityId = worldMapData.getBottomEntityIdFromTile(floor, tileX, tileY);
        if (centerEntityId == null) {
            return;
        }
        Entity centerEntity = gameData.getEntity(centerEntityId);
        if (centerEntity != null) {
            //LEFT
            Long entityIdOnLeft = worldMapData.getBottomEntityIdFromTile(floor, tileX - 1, tileY);
            Entity entityLeft = gameData.getEntity(entityIdOnLeft);

            //UP
            Long entityIdOnUp = worldMapData.getBottomEntityIdFromTile(floor, tileX, tileY + 1);
            Entity entityUp = gameData.getEntity(entityIdOnUp);

            //RIGHT
            Long entityIdOnRight = worldMapData.getBottomEntityIdFromTile(floor, tileX + 1, tileY);
            Entity entityRight = gameData.getEntity(entityIdOnRight);

            //DOWN
            Long entityIdOnDown = worldMapData.getBottomEntityIdFromTile(floor, tileX, tileY - 1);
            Entity entityDown = gameData.getEntity(entityIdOnDown);

            int left = EntityUtils.compareLabels(entityLeft, centerEntity) ? 1:0;
            int up = EntityUtils.compareLabels(entityUp, centerEntity) ? 1:0;
            int right = EntityUtils.compareLabels(entityRight, centerEntity) ? 1:0;
            int down = EntityUtils.compareLabels(entityDown, centerEntity) ? 1:0;

            int[][] edgeCompatibility = {
                    {0,     up,     0},
                    {left,   0, right},
                    {0,   down,     0},
            };


            Side matchingEdge = getMatchingEdge(edgeCompatibility);
            Optional<Deque<Side>> edgesQueue = worldMapData.getEdgesQueue(floor, tileX, tileY);
            if (edgesQueue.isPresent()) {
                Deque<Side> edges = edgesQueue.get();
                edges.pollFirst();
                edges.offerFirst(matchingEdge);
            }

//            Optional<Long> entityIdToReplace = getEntityIdToReplace(edgeCompatibility, centerEntity);
//            entityIdToReplace.ifPresent(id -> worldMapData.getEntitiesOnTile(floor, tileX, tileY).ifPresent(tileDeque -> {
//                TileUtils.replaceEntityOnBottom(id, tileDeque);
//            }));

//            Optional<Long> entityIdToReplace = getEntityIdToReplace(edgeCompatibility, centerEntity);
//            entityIdToReplace.ifPresent(id -> worldMapData.getEntitiesOnTile(floor, tileX, tileY).ifPresent(tileDeque -> {
//                TileUtils.replaceEntityOnBottom(id, tileDeque);
//            }));
        }
    }

    private static Optional<Long> getEntityIdToReplace(int[][] edgeCompatibility, Entity entity) {
        Side matchingEdge = getMatchingEdge(edgeCompatibility);
        Map<Side, Long> edgeEntityIdMap = entity.getProperties().getReplaceableEdgeEntityIdMap();
        if (edgeEntityIdMap != null) {
            return Optional.ofNullable(edgeEntityIdMap.get(matchingEdge));
        }
        return Optional.empty();
    }

    private static Side getMatchingEdge(int[][] edgeCompatibility) {
        for (Map.Entry<int[][], Side> entry : sideMap.entrySet()) {
            int[][] matrix = entry.getKey();
            if (Arrays.equals(edgeCompatibility[0], matrix[0])
                    && Arrays.equals(edgeCompatibility[1], matrix[1])
                    && Arrays.equals(edgeCompatibility[2], matrix[2])) {
                return entry.getValue();
            }
        }
        return Side.CENTER;
    }
}
