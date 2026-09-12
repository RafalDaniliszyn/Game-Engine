package testWFC;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.barneys.debug.Panel;

import javax.swing.JFrame;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static game.isometric.WorldSettings.CHUNK_SIZE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static testWFC.Rules.*;
import static testWFC.Rules.TileRule;


/**
 * Main WFC generator
 */
public class PixelWFC {

    private final String pathTiles = "/map/WFCRules.json";

    private final Map<String, TileRule> RULES;

    private final Map<Integer, Set<String>> TILES;

    private final Random random = new Random();

    private static final int MAX_ATTEMPTS = 20;

    private JFrame frame;

    public PixelWFC() {
        TILES = new HashMap<>();
        RULES = loadRules(pathTiles, TILES);
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.add(org.barneys.debug.Panel.getInstance());
        frame.addKeyListener(Panel.getInstance());
    }

    public Deque<String>[][] generateAll(int floor) {
        Deque<String>[][] tiles = generate(floor, TILES, RULES, CHUNK_SIZE);
        updateScreen(tiles);
        extractLabels(tiles);
        return tiles;
    }

    public Deque<String>[][] generateAll(int floor, Integer chunkSize) {
        Deque<String>[][] tiles = generate(floor, TILES, RULES, chunkSize);
        updateScreen(tiles);
        extractLabels(tiles);
        return tiles;
    }

    private void extractLabels(Deque<String>[][] tiles) {
        for (Deque<String>[] tile : tiles) {
            for (int i = 0; i < tile.length; i++) {
                tile[i] = tile[i].stream().map(label -> {
                    if (label.contains("BORDER")) {
                        return label.split("_")[0] + "_2D";
                    }
                    if (label.charAt(label.length() - 1) != 'D') {
                        return label.substring(0, label.length()-1);
                    }
                    return label;
                }).collect(Collectors.toCollection(ArrayDeque::new));
            }
        }
    }

    public Deque<String>[][] generate(int floor, Map<Integer, Set<String>> tiles, Map<String, TileRule> rules, Integer chunkSize) {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                return generateOnce(floor, tiles, rules, chunkSize);
            } catch (ContradictionException e) {
                System.out.println("WFC restart (proba " + attempt + "/" + MAX_ATTEMPTS + "): " + e.getMessage());
            }
        }
        throw new IllegalStateException("WFC nie zbiegl po " + MAX_ATTEMPTS + " probach (pietro " + floor + ")");
    }

    private Deque<String>[][] generateOnce(int floor, Map<Integer, Set<String>> tiles, Map<String, TileRule> rules, Integer chunkSize) {
        Cell[][] map = initMap(chunkSize, tiles, floor);
        boolean start = true;
        while (true) {
            updateScreen(map);
            Cell next;
            if (start) {
                next = map[10][10];
                start = false;
            } else {
                next = next(map, rules);
                if (next == null) {
                    break;
                }
            }

            String collapsed = collapse(next.x(), next.y(), map);
            if (collapsed != null) {
                List<Cell> reducedCells = updateAround(map, next.x(), next.y(), rules).stream()
                        .sorted(Comparator.comparingDouble(tile -> getEntropy(tile, rules))).collect(Collectors.toList());

                updateProcess(rules, map, reducedCells);
            }
        }
        return toDeque(map);
    }

    private void updateProcess(Map<String, TileRule> rules, Cell[][] map, List<Cell> reducedCells) {
        boolean allCollapsed = false;
        while (!allCollapsed) {
            List<Cell> newReducedCell = new ArrayList<>();
            for (Cell cell : reducedCells) {
                if (!cell.labels().isEmpty()) {
                    newReducedCell.addAll(updateAround(map, cell.x(), cell.y(), rules).stream()
                            .sorted(Comparator.comparingDouble(tile -> getEntropy(tile, rules))).toList());
                }
            }
            if (newReducedCell.isEmpty()) {
                allCollapsed = true;
            } else {
                reducedCells = new ArrayList<>(newReducedCell);
            }
        }
    }


    private Deque<String>[][] toDeque(Cell[][] map) {
        Deque<String>[][] result = new ArrayDeque[map.length][map.length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = new ArrayDeque<>(map[i][j].labels());
            }
        }
        return result;
    }

    private void updateScreen(Cell[][] map) {
//        for (Cell[] cells : map) {
//            for (Cell cell : cells) {
//                if (cell != null) {
//                    List<String> labels = cell.labels();
//                    if (!labels.isEmpty() && Panel.map != null) {
//                        Panel.mapList[cell.x()][cell.y()] = labels;
//                    }
//                }
//            }
//        }
    }

    private void updateScreen(Deque<String>[][] map) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                List<String> labels = map[x][y].stream().toList();
                if (!labels.isEmpty() && Panel.map != null && Panel.imageMap.containsKey(labels.get(0))) {
                    Panel.mapList[x][y] = new ArrayList<>(labels);
                }
            }
        }
    }

    private Cell next(Cell[][] map, Map<String, TileRule> rules) {
        List<Cell> superPositionCells = new ArrayList<>();
        for (Cell[] cells : map) {
            for (Cell cell : cells) {
                if (cell.labels().size() > 1) {
                    superPositionCells.add(cell);
                }
            }
        }
        if (superPositionCells.isEmpty()) {
            return null;
        }
        Optional<Cell> min = superPositionCells.stream().min(Comparator.comparingDouble(cell -> getEntropy(cell, rules)));
        return min.orElse(null);
    }

    double getEntropy(Cell cell, Map<String, TileRule> rules) {
        double sum = 0.0;
        double logSum = 0.0;
        for (String label : cell.labels()) {
            double weight = rules.get(label).getChance();
            sum += weight;
        }
        for (String label : cell.labels()) {
            double weight = rules.get(label).getChance();
            logSum += weight * (Math.log(weight) / Math.log(2));
        }

        double result = (Math.log(sum) / Math.log(2)) - (logSum / sum);
        return result;
    }

    private List<Cell> updateAround(Cell[][] map, int x, int y, Map<String, TileRule> rules) {
        if (!(x < map.length && y < map.length && x >= 0 && y >= 0)) {
            return Collections.emptyList();
        }
        Cell centerCell = map[x][y];
        List<Cell> reducedList = new ArrayList<>();
        Map<Integer, Set<String>> allowedMap = new HashMap<>();
        allowedMap.put(0,  new HashSet<>());
        allowedMap.put(1,  new HashSet<>());
        allowedMap.put(2,  new HashSet<>());
        allowedMap.put(3,  new HashSet<>());
        for (String label : centerCell.labels()) {
            TileRule tileRule = rules.get(label);
            allowedMap.get(0).addAll(tileRule.connectorMap().get(0).sockets());
            allowedMap.get(1).addAll(tileRule.connectorMap().get(1).sockets());
            allowedMap.get(2).addAll(tileRule.connectorMap().get(2).sockets());
            allowedMap.get(3).addAll(tileRule.connectorMap().get(3).sockets());
        }

        reduce(map, x, y, rules, allowedMap, reducedList);

        return reducedList;
    }

    public void reduce(Cell[][] map, int x, int y, Map<String, TileRule> rules, Map<Integer, Set<String>> allowedMap, List<Cell> reducedList) {
        //LEFT
        if (x > 0 && map[x - 1][y].labels().size() != 1) {
            Cell cell = map[x - 1][y];
            List<String> cellPossibleLabels = cell.labels();
            boolean reduced = cellPossibleLabels.removeIf(possibleLabel -> {
                TileRule possibleTileRule = rules.get(possibleLabel);
                return !allowedMap.get(0).contains(possibleTileRule.connectorMap().get(2).plug());
            });

            if (reduced) {
                if (cell.labels().isEmpty()) {
                    throw new ContradictionException(cell.x(), cell.y());
                }
                reducedList.add(cell);
                updateScreen(map);
            }
        }

        //RIGHT
        if (x < map.length - 1 && map[x + 1][y].labels().size() != 1) {
            Cell cell = map[x + 1][y];
            List<String> cellPossibleLabels = cell.labels();
            boolean reduced = cellPossibleLabels.removeIf(possibleLabel -> {
                TileRule possibleTileRule = rules.get(possibleLabel);
                return !allowedMap.get(2).contains(possibleTileRule.connectorMap().get(0).plug());
            });

            if (reduced) {
                if (cell.labels().isEmpty()) {
                    throw new ContradictionException(cell.x(), cell.y());
                }
                reducedList.add(cell);
                updateScreen(map);
            }
        }

        //UP
        if (y < map.length - 1 && map[x][y + 1].labels().size() != 1) {
            Cell cell = map[x][y + 1];
            List<String> cellPossibleLabels = cell.labels();
            boolean reduced = cellPossibleLabels.removeIf(possibleLabel -> {
                TileRule possibleTileRule = rules.get(possibleLabel);
                return !allowedMap.get(1).contains(possibleTileRule.connectorMap().get(3).plug());
            });

            if (reduced) {
                if (cell.labels().isEmpty()) {
                    throw new ContradictionException(cell.x(), cell.y());
                }
                reducedList.add(cell);
                updateScreen(map);
            }
        }

        //DOWN
        if (y > 0 && map[x][y - 1].labels().size() != 1) {
            Cell cell = map[x][y - 1];
            List<String> cellPossibleLabels = cell.labels();
            boolean reduced = cellPossibleLabels.removeIf(possibleLabel -> {
                TileRule possibleTileRule = rules.get(possibleLabel);
                return !allowedMap.get(3).contains(possibleTileRule.connectorMap().get(1).plug());
            });

            if (reduced) {
                if (cell.labels().isEmpty()) {
                    throw new ContradictionException(cell.x(), cell.y());
                }
                reducedList.add(cell);
                updateScreen(map);
            }
        }
    }

    private String collapse(int x, int y, Cell[][] map) {
        List<String> labels = map[x][y].labels();
        int size = labels.size();
        if (size != 1 && size > 0) {
            String collapsedLabel = weightedPick(labels);
            labels.clear();
            labels.add(collapsedLabel);
            updateScreen(map);
            return collapsedLabel;
        }
        return null;
    }

    /**
     * Selects a label at random, weighted by each tile's {@code chance}.
     * Falls back to the last label to guard against floating-point rounding.
     */
    private String weightedPick(List<String> labels) {
        double total = 0.0;
        for (String label : labels) {
            total += RULES.get(label).getChance();
        }
        double r = random.nextDouble() * total;
        double acc = 0.0;
        for (String label : labels) {
            acc += RULES.get(label).getChance();
            if (r <= acc) {
                return label;
            }
        }
        return labels.get(labels.size() - 1);
    }

    private Cell[][] initMap(int size, Map<Integer, Set<String>> tiles, Integer floor) {
        Cell[][] map = new Cell[size][size];
        initTiles(map, tiles, floor);
        return map;
    }

    private void initTiles(Cell[][] map, Map<Integer, Set<String>> tiles, Integer floor) {
        Set<String> labels = tiles.get(floor);
        if (labels == null) {
            return;
        }
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                List<String> possibleTiles = new ArrayList<>(labels);
                map[x][y] = new Cell(possibleTiles, 0, x, y);
            }
        }
    }

    private Map<String, TileRule> loadRules(String path, Map<Integer, Set<String>> tileMap) {
        Map<String, TileRule> result = new HashMap<>();
        try (InputStream resourceAsStream = PixelWFC.class.getResourceAsStream(path)) {
            if (resourceAsStream == null) {
                throw new RuntimeException("Not found: " + path);
            }
            byte[] bytes = resourceAsStream.readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            Rules wfcRules = objectMapper.readValue(bytes, Rules.class);
            List<TileRule> rules = wfcRules.getTileRules();

            for (TileRule rule : rules) {
                if (!result.containsKey(rule.label())) {
                    result.put(rule.label(), rule);
                }

                Set<Integer> floors = rule.floors();
                for (Integer floor : floors) {
                    if (tileMap.containsKey(floor)) {
                        tileMap.get(floor).add(rule.label());
                    } else {
                        Set<String> tilesTmp = new HashSet<>();
                        tilesTmp.add(rule.label());
                        tileMap.put(floor, tilesTmp);
                    }
                }
            }
        } catch (IOException exception){
            System.out.println(exception.getMessage());
        }
        generateRotationRules(result, tileMap);
        return result;
    }

    private void generateRotationRules(Map<String, TileRule> ruleMap, Map<Integer, Set<String>> tileMap) {
        Map<String, TileRule> generated = new HashMap<>();
        for (Map.Entry<String, TileRule> entry : ruleMap.entrySet()) {
            TileRule rule = entry.getValue();
            String label = entry.getKey();

            // Kafle w pelni symetryczne maja identyczne 4 rotacje - generowanie
            // duplikatow zawyzaloby ich reprezentacje (wage) w superpozycji.
            if (isSymmetric(rule)) {
                continue;
            }

            //3 nowe kafelki dla rotacji 1, 2, 3
            for (int i = 1; i < 4; i++) {
                Map<Integer, Connector> connectorMapRotated = new HashMap<>();

                for (int indexSide = 0; indexSide < 4; indexSide++) {
                    Connector rotatedConnectors = rule.getRotatedConnector(indexSide, i);
                    Connector connector = new Connector(rotatedConnectors.plug(), new HashSet<>(rotatedConnectors.sockets()));
                    connectorMapRotated.put(indexSide, connector);
                }
                String rotLabel = label+i;

                AcceptedNeighbors rotAcceptedNeighbors = new AcceptedNeighbors(new HashMap<>());
                TileRule rotRule = new TileRule(rotLabel, rule.getChance(), rule.floors(), rotAcceptedNeighbors, connectorMapRotated);
                generated.put(rotLabel, rotRule);
            }
            generated.forEach((genLabel, genRule) -> {
                for (Integer floor : genRule.floors()) {
                    tileMap.get(floor).add(genLabel);
                }
            });
        }
        ruleMap.putAll(generated);
    }

    /**
     * A tile is symmetric when all four connectors are identical, meaning its
     * three rotations would be exact duplicates of the base tile.
     */
    private boolean isSymmetric(TileRule rule) {
        Map<Integer, Connector> connectors = rule.connectorMap();
        Connector first = connectors.get(0);
        return first.equals(connectors.get(1))
                && first.equals(connectors.get(2))
                && first.equals(connectors.get(3));
    }

    record Cell(List<String> labels, int rotation, int x, int y) {}

    /**
     * Thrown when a cell loses all possible labels during constraint
     * propagation. Caught by {@link #generate} to restart the chunk.
     */
    static class ContradictionException extends RuntimeException {
        ContradictionException(int x, int y) {
            super("brak mozliwych kafli w komorce " + x + "," + y);
        }
    }
}
