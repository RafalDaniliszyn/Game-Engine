package testWFC;

import game.isometric.WorldSettings;
import org.barneys.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.*;

import static testWFC.Rules.*;

public class WFCOverlayGenerator extends PixelWFC {
    private float[][] heightMap;

    public WFCOverlayGenerator() {
        super();
        loadHeightMap();
    }

    @Override
    public void reduce(Cell[][] map, int x, int y, Map<String, TileRule> rules, Map<Integer, Set<String>> allowedMap, List<Cell> reducedList) {
        int width = map.length;
        int height = map[0].length;

        // LEFT
        reduceNeighbor(map, x - 1, y, 2, 0, width, height, rules, allowedMap, reducedList);

        // RIGHT
        reduceNeighbor(map, x + 1, y, 0, 2, width, height, rules, allowedMap, reducedList);

        // UP
        reduceNeighbor(map, x, y + 1, 3, 1, width, height, rules, allowedMap, reducedList);

        // DOWN
        reduceNeighbor(map, x, y - 1, 1, 3, width, height, rules, allowedMap, reducedList);
    }

    private void reduceNeighbor(
            Cell[][] map,
            int nx,
            int ny,
            int neighborSide,
            int allowedDir,
            int width,
            int height,
            Map<String, TileRule> rules,
            Map<Integer, Set<String>> allowedMap,
            List<Cell> reducedSet
    ) {
        // bounds
        if (nx < 0 || ny < 0 || nx >= width || ny >= height) return;

        Cell cell = map[nx][ny];
        List<String> labels = cell.labels();

        // already resolved or invalid
        if (labels.size() <= 1) return;

        Set<String> allowed = allowedMap.get(allowedDir);
        if (allowed == null) return; // invalid solver state

        boolean reduced = labels.removeIf(label -> {
            TileRule rule = rules.get(label);
            return rule == null ||
                    !allowed.contains(
                            rule.connectorMap().get(neighborSide).plug()
                    );
        });

        if (reduced) {
            reducedSet.add(cell);

            // SAFETY: detect contradiction early
            if (labels.isEmpty()) {
               //throw new IllegalStateException("WFC contradiction: no labels left");
            }
        }
    }


    @Override
    double getEntropy(Cell cell, Map<String, TileRule> rules) {
        double sum = 0.0;
        double logSum = 0.0;
        int offset = heightMap.length / WorldSettings.CHUNK_SIZE;
        for (String label : cell.labels()) {
            int hx = cell.x() * offset;
            int hy = cell.y() * offset;
            float height = heightMap[hx][hy];
            double weight = rules.get(label).getChance();

            sum += weight;
        }
        for (String label : cell.labels()) {
            int hx = cell.x() * offset;
            int hy = cell.y() * offset;
            float height = heightMap[hx][hy];
            double weight = rules.get(label).getChance();

            logSum += weight * (Math.log(weight) / Math.log(2));
        }

        return (Math.log(sum) / Math.log(2)) - (logSum / sum);
    }

    private void loadHeightMap() {
        Optional<BufferedImage> bufferedImage = ImageLoader.loadImage(
                "C:\\Users\\Rafal\\Desktop\\lwjglApp\\lwjglApp\\src\\main\\resources\\textures\\2D\\heightMap2D.png"
        );

        if (bufferedImage.isPresent()) {
            BufferedImage img = bufferedImage.get();
            int width  = img.getWidth();
            int height = img.getHeight();
            heightMap = new float[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = img.getRGB(x, y); // ARGB
                    int r = (rgb >> 16) & 0xFF; // czerwony kanał
                    // zakładamy grayscale: R=G=B
                    float h = r / 255.0f;       // 0..1
                    heightMap[x][y] = h;
                }
            }
        }
    }
}
