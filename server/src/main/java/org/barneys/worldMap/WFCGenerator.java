package org.barneys.worldMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.barneys.debug.Panel;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static game.isometric.WorldSettings.CHUNK_SIZE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class WFCGenerator {
    private static final String path = "/map/WFCRules.json";
    private static final Map<String, WFCRule.Rule> RULES;
    private static final Map<Integer, Set<String>> TILES;
    private static JFrame frame;

    static {
        TILES = new HashMap<>();
        RULES = loadRules();
//        frame = new JFrame();
//        frame.setVisible(true);
//        frame.setSize(1600, 900);
//        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        frame.add(Panel.getInstance());
//        frame.addKeyListener(Panel.getInstance());
    }

    public static Deque<String>[][] runWFC(Integer floor) {
        List<String>[][] map = new ArrayList[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                map[i][j] = new ArrayList<>();
            }
        }
        initTiles(map, floor);
        List<String> tiles = TILES.get(floor).stream().toList();
        Random random = new Random();
        String randomLabel = tiles.get(random.nextInt(tiles.size()));
        ArrayList<String> randomLabelList = new ArrayList<>();
        randomLabelList.add(randomLabel);
        int randomX = random.nextInt(map.length);
        int randomY = random.nextInt(map.length);
        map[randomX][randomY] = randomLabelList;

        try {
            map = updateAdjacentTiles(map, randomLabel, randomX, randomY, randomX, randomY, 1);
            Optional<Tile> tileOptional;
            int iteration = 0;
            while ((tileOptional = findTileInSuperPosition(map)).isPresent() && iteration < 500) {
                Tile tile = tileOptional.get();
                map = updateAdjacentTiles(map, randomLabel, tile.x, tile.y, tile.x, tile.y, 1);
                iteration += 1;
            }
        } catch (StackOverflowError e) {
            System.out.println(e);
        }

        Deque<String>[][] result = new ArrayDeque[CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                Deque<String> labelDeque = new ArrayDeque<>();
                if (map[x][y].size() != 0) {
                    labelDeque.push(map[x][y].get(0));
                    Panel.map[x][y] = map[x][y].get(0);
                } else {
                    labelDeque.push("GRASS_2D");
                }
                result[x][y] = labelDeque;
            }
        }
        Deque<String> labelDeque = new ArrayDeque<>();
        labelDeque.push("WATER_2D");
        result[2][2] = labelDeque;
        Panel.map[2][2] = "WATER_2D";
        return result;
    }

    private static void initTiles(List<String>[][] map, Integer floor) {
        Set<String> labels = TILES.get(floor);
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                List<String> possibleTiles = new ArrayList<>(labels);
                map[x][y] = possibleTiles;
            }
        }
    }

    private static Optional<Tile> findTileInSuperPosition(List<String>[][] map) {
        List<Tile> tileList = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                List<String> labels = map[x][y];
                if (labels.size() > 1) {
                    tileList.add(new Tile(x, y, labels));
                }
            }
        }
        if (tileList.size() == 0) {
            return Optional.empty();
        }
//        return tileList.stream().filter(tile -> tile.labels.size() != 0)
//                .min(Comparator.comparing(Tile::weight));
        return Optional.of(tileList.get(new Random().nextInt(tileList.size())));
    }

    private static boolean checkAdjacentCollapsedRule(List<String>[][] map, String labelToCheck, int parentX, int parentY, int x, int y) {
        //LEFT
        if (x > 0 && x < map.length && x - 1 != parentX && map[x - 1][y].size() == 1) {
            List<String> sideLabels = map[x - 1][y];
            WFCRule.Rule ruler = RULES.get(sideLabels.get(0));
            List<String> rulerAccept = ruler.sideAccepts(WFCRule.AcceptsSide.RIGHT).stream().toList();
            if (!rulerAccept.contains(labelToCheck)) {
                return false;
            }
        }

        //RIGHT
        if (x < map.length - 1 && x >= 0 && x + 1 != parentX && map[x + 1][y].size() == 1) {
            List<String> sideLabels = map[x + 1][y];
            WFCRule.Rule ruler = RULES.get(sideLabels.get(0));
            List<String> rulerAccept = ruler.sideAccepts(WFCRule.AcceptsSide.LEFT).stream().toList();
            if (!rulerAccept.contains(labelToCheck)) {
                return false;
            }
        }

        //UP
        if (y < map.length - 1 && y >= 0 && y + 1 != parentY && map[x][y + 1].size() == 1) {
            List<String> sideLabels = map[x][y + 1];
            WFCRule.Rule ruler = RULES.get(sideLabels.get(0));
            List<String> rulerAccept = ruler.sideAccepts(WFCRule.AcceptsSide.DOWN).stream().toList();
            if (!rulerAccept.contains(labelToCheck)) {
                return false;
            }
        }

        //DOWN
        if (y > 0 && y < map.length && y - 1 != parentY && map[x][y - 1].size() == 1) {
            List<String> sideLabels = map[x][y - 1];
            WFCRule.Rule ruler = RULES.get(sideLabels.get(0));
            List<String> rulerAccept = ruler.sideAccepts(WFCRule.AcceptsSide.UP).stream().toList();
            if (!rulerAccept.contains(labelToCheck)) {
                return false;
            }
        }
        return true;
    }

    private static List<String>[][] updateAdjacentTiles(List<String>[][] map, String collapsed, int lastX, int lastY, int x, int y, int entropyLimit) throws StackOverflowError {
        WFCRule.Rule ruler = RULES.get(collapsed);
        List<Tile> possibleTiles = new ArrayList<>();

        //LEFT
        if (x > 0 && x < map.length && x - 1 != lastX && map[x - 1][y].size() != 1) {
            List<String> sideLabels = map[x - 1][y];
            sideLabels.retainAll(ruler.sideAccepts(WFCRule.AcceptsSide.LEFT));
            List<String> possibleLabels = new ArrayList<>();
            for (String sideLabel : sideLabels) {
                WFCRule.Rule rule = RULES.get(sideLabel);
                if (rule.sideAccepts(WFCRule.AcceptsSide.RIGHT).contains(ruler.label())
                        && checkAdjacentCollapsedRule(map, sideLabel, lastX, lastY, x - 1, y)) {
                    possibleLabels.add(sideLabel);
                }
            }
            if (possibleLabels.size() != 0) {
                possibleTiles.add(new Tile(x - 1, y, possibleLabels));
            }
        }

        //RIGHT
        if (x < map.length - 1 && x >= 0 && lastX != x + 1 && map[x + 1][y].size() != 1) {
            List<String> sideLabels = map[x + 1][y];
            sideLabels.retainAll(ruler.sideAccepts(WFCRule.AcceptsSide.RIGHT));
            List<String> possibleLabels = new ArrayList<>();
            for (String sideLabel : sideLabels) {
                WFCRule.Rule rule = RULES.get(sideLabel);
                if (rule.sideAccepts(WFCRule.AcceptsSide.LEFT).contains(ruler.label())
                        && checkAdjacentCollapsedRule(map, sideLabel, lastX, lastY,x + 1, y)) {
                    possibleLabels.add(sideLabel);
                }
            }
            if (possibleLabels.size() != 0) {
                possibleTiles.add(new Tile(x + 1, y, possibleLabels));
            }
        }

        //UP
        if (y < map.length - 1 && y >= 0 && lastY != y + 1 && map[x][y + 1].size() != 1) {
            List<String> sideLabels = map[x][y + 1];
            sideLabels.retainAll(ruler.sideAccepts(WFCRule.AcceptsSide.UP));
            List<String> possibleLabels = new ArrayList<>();
            for (String sideLabel : sideLabels) {
                WFCRule.Rule rule = RULES.get(sideLabel);
                if (rule.sideAccepts(WFCRule.AcceptsSide.DOWN).contains(ruler.label())
                        && checkAdjacentCollapsedRule(map, sideLabel, lastX, lastY, x, y + 1)) {
                    possibleLabels.add(sideLabel);
                }
            }
            if (possibleLabels.size() != 0) {
                possibleTiles.add(new Tile(x, y + 1, possibleLabels));
            }
        }

        //DOWN
        if (y > 0 && y < map.length && lastY != y - 1 && map[x][y - 1].size() != 1) {
            List<String> sideLabels = map[x][y - 1];
            sideLabels.retainAll(ruler.sideAccepts(WFCRule.AcceptsSide.DOWN));
            List<String> possibleLabels = new ArrayList<>();
            for (String sideLabel : sideLabels) {
                WFCRule.Rule rule = RULES.get(sideLabel);
                if (rule.sideAccepts(WFCRule.AcceptsSide.UP).contains(ruler.label())
                        && checkAdjacentCollapsedRule(map, sideLabel, lastX, lastY, x, y - 1)) {
                    possibleLabels.add(sideLabel);
                }
            }
            if (possibleLabels.size() != 0) {
                possibleTiles.add(new Tile(x, y - 1, possibleLabels));
            }
        }

        Optional<Tile> lowestEntropy = possibleTiles.stream().filter(tile -> tile.labels.size() != 0)
                .min(Comparator.comparing(Tile::weight));
        if (lowestEntropy.isPresent()) {
            Tile tile = lowestEntropy.get();
            List<String> newLabelList = new ArrayList<>();

            //
            int chances = 0;
            for (String label : tile.labels) {
                chances += RULES.get(label).chance();
            }
            int random = new Random().nextInt(chances);
            int lastTileChance = 0;
            for (String label : tile.labels) {
                Integer currentTileChance = RULES.get(label).chance();
                if (random > lastTileChance && random <= lastTileChance + currentTileChance) {
                    newLabelList.add(label);
                    if (Panel.map != null) {
                        Panel.map[tile.x][tile.y] = label;
                    }
                    map[tile.x()][tile.y()] = newLabelList;
                    map = updateAdjacentTiles(map, label, x, y, tile.x(), tile.y(), entropyLimit);
                }
                lastTileChance += currentTileChance;
            }
            //
//            newLabelList.add(tile.labels.get(new Random().nextInt(tile.labels.size())));
//            map[tile.x()][tile.y()] = newLabelList;
//            map = updateAdjacentTiles(map, newLabelList.get(0), x, y, tile.x(), tile.y(), entropyLimit);
        }
        return map;
    }

    private record Tile(int x, int y, List<String> labels) {
        public int weight() {
            int sum = 0;
            for (String label : labels) {
                sum += RULES.get(label).chance();
            }
            return sum;
        }
    }

    private static Map<String, WFCRule.Rule> loadRules() {
        Map<String, WFCRule.Rule> result = new HashMap<>();
        try (InputStream resourceAsStream = WFCGenerator.class.getResourceAsStream(path)) {
            if (resourceAsStream == null) {
                throw new RuntimeException("Not found: " + path);
            }
            byte[] bytes = resourceAsStream.readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            WFCRule wfcRule = objectMapper.readValue(bytes, WFCRule.class);
            List<WFCRule.Rule> rules = wfcRule.getRules();

            for (WFCRule.Rule rule : rules) {
                if (!result.containsKey(rule.label())) {
                    result.put(rule.label(), rule);
                }

                Set<Integer> floors = rule.floors();
                for (Integer floor : floors) {
                    if (TILES.containsKey(floor)) {
                        TILES.get(floor).add(rule.label());
                    } else {
                        Set<String> tilesTmp = new HashSet<>();
                        tilesTmp.add(rule.label());
                        TILES.put(floor, tilesTmp);
                    }
                }
            }
        } catch (IOException exception){
            System.out.println(exception.getMessage());
        }
        return result;
    }
}
