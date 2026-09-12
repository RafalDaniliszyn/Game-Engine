package org.barneys.debug;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static game.isometric.WorldSettings.CHUNK_SIZE;

public class Panel extends JPanel implements KeyListener {
    private static Map<String, String> pathMap;
    public static Map<String, BufferedImage> imageMap;
    public static String[][] map;
    public static List<String>[][] mapList;

    private static Panel instance;

    public static final String ROOT_PATH = "C:\\Users\\danil\\Desktop\\programowanie\\projekt-20260828T224622Z-1-001\\projekt\\lwjglApp\\lwjglApp";

    static {
        instance = new Panel();
        map = new String[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                map[i][j] = "";
            }
        }
        mapList = new ArrayList[CHUNK_SIZE][CHUNK_SIZE];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                mapList[i][j] = new ArrayList<>();
            }
        }

        try {
            pathMap = new HashMap<>();
            pathMap.put("WFC_DOWN", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\down.png");
            pathMap.put("WFC_LEFT", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\left.png");
            pathMap.put("WFC_LEFT_DOWN", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\leftDown.png");
            pathMap.put("WFC_LEFT_UP", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\leftUp.png");
            pathMap.put("WFC_RIGHT", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\right.png");
            pathMap.put("WFC_RIGHT_DOWN", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\rightDown.png");
            pathMap.put("WFC_RIGHT_UP", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\rightUp.png");
            pathMap.put("WFC_UP", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\wfcTest\\up.png");
            pathMap.put("GRASS_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\grass\\grass.png");
            pathMap.put("GRASS_BORDER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\grass\\grass.png");
            pathMap.put("SAND_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\sand2D.png");
            pathMap.put("SAND_BORDER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\sand2D.png");
            pathMap.put("SAND_END", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\sand2D.png");

            pathMap.put("WATER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\water\\water.png");
            pathMap.put("WATER_2D1", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\water\\water.png");
            pathMap.put("WATER_2D2", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\water\\water.png");
            pathMap.put("WATER_2D3", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\water\\water.png");

            pathMap.put("WATER_BORDER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\water\\water.png");
            pathMap.put("FENCE_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fence_1.png");
            pathMap.put("FENCE_SIDE_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fenceSide.png");
            pathMap.put("FENCE_RIGHT_DOWN_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fenceRightDown.png");
            pathMap.put("FENCE_LEFT_DOWN_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fenceLeftDown.png");
            pathMap.put("FENCE_LEFT_UP_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fenceLeftUp.png");
            pathMap.put("FENCE_RIGHT_UP_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fenceRightUp.png");
            pathMap.put("FENCE_BLANK_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\fence\\fenceBlank.png");
            pathMap.put("WATER_WELL", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\waterWell\\waterWell.png");
            pathMap.put("BRICK_WALL_H", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\brickWall\\horizontal.png");
            pathMap.put("BRICK_WALL_VL", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\brickWall\\verticalLeft.png");
            pathMap.put("BRICK_WALL_VR", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\brickWall\\verticalRight.png");
            pathMap.put("BRICK_WALL_RE", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\brickWall\\rightEnd.png");
            pathMap.put("BRICK_WALL_LE", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\brickWall\\leftEnd.png");
            pathMap.put("WOODEN_WALL", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\woodenWall\\woodenWallDown.png");
            pathMap.put("WOODEN_WALL_BORDER", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\woodenWall\\woodenWallDown.png");
            pathMap.put("MINE_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\underground\\mine.png");
            pathMap.put("MINE_BORDER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\underground\\mine.png");
            pathMap.put("DIRT_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\dirt.png");
            pathMap.put("DIRT_BORDER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\dirt.png");
            pathMap.put("PATH_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\dirt\\path.png");
            pathMap.put("PATH_BORDER_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\dirt\\path.png");
            pathMap.put("TREE_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\greenTree2D.png");
            pathMap.put("GROUND_2D", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\ground2D.png");


            //PIPE
            pathMap.put("BOARD", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\board.png");
            pathMap.put("TERMINAL", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\terminal.png");
            pathMap.put("PIPE", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\pipe.png");
            pathMap.put("JOINT", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\joint.png");
            pathMap.put("INTERSECTION", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\intersection.png");
            pathMap.put("CONNECTOR", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\connector.png");
            pathMap.put("BRIDGE", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\bridge.png");
            pathMap.put("BLANK", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\blank.png");
            pathMap.put("END", ROOT_PATH + "\\src\\main\\resources\\textures\\2D\\test\\end.png");
            pathMap.put("UPDATE_TEST_2D",           "C:\\Users\\danil\\Desktop\\programowanie\\projekt-20260828T224622Z-1-001\\projekt\\server\\src\\main\\resources\\images\\UPDATE_TEST_2D.png");


            imageMap = new HashMap<>();
            for (String key : pathMap.keySet()) {
                imageMap.put(key, ImageIO.read(new File(pathMap.get(key))));
            }
            for (Map.Entry<String, String> entry : pathMap.entrySet()) {
                imageMap.put(entry.getKey(), ImageIO.read(new File(entry.getValue())));
            }
            loadImages();

        } catch (IOException e) {
            e.printStackTrace();
            pathMap = null;
        }
    }

    private static void loadImages() {
        File folder = new File("C:\\Users\\danil\\Desktop\\programowanie\\projekt-20260828T224622Z-1-001\\projekt\\server\\src\\main\\resources\\images");
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        System.out.println("Wczytano obraz: " + file.getName());
                        String label = file.getName().substring(0, file.getName().lastIndexOf('.'));
                        imageMap.put(label, img);
                    } else {
                        System.out.println("Nie udało się wczytać: " + file.getName());
                    }
                } catch (IOException e) {
                    System.err.println("Błąd przy wczytywaniu pliku " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    private static int moveY = 0;
    private static int moveX = 0;
    int tileSize = 50;
 //   @Override
//    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        //Graphics2D g2d = (Graphics2D) g;
//        if (mapList != null) {
//            int yOffset = mapList.length * tileSize;
//            for (int x = 0; x < mapList.length; x++) {
//                for (int y = 0; y < mapList.length; y++) {
//                    float alpha = 1.0f / mapList[x][y].size();
//                    if (alpha < 0.0f || alpha > 1.0f) {
//                        alpha = 1.0f;
//                    }
//                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
//                    for (int i = 0; i < mapList[x][y].size(); i++) {
//
//                        if (i < mapList[x][y].size() && !imageMap.containsKey(mapList[x][y].get(i))) {
//                            String tileLabel = mapList[x][y].get(i);
//                            String substring = tileLabel.substring(0, tileLabel.length()-1);
//                            int rotation = Integer.parseInt(tileLabel.substring(tileLabel.length() - 1));
//                            BufferedImage src = imageMap.get(substring);
//                            try {
//                                if (src != null) {
//                                    rotateImage(src, rotation, tileLabel);
//                                }
//                            } catch (IOException e) {
//                                System.out.println(e.getMessage());
//                            }
//                        }
//
//                        if (i < mapList[x][y].size()) {
//                            BufferedImage image = imageMap.get(mapList[x][y].get(i));
//                            g2d.drawImage(image, (x+moveX) * tileSize, yOffset - ((y+moveY) * tileSize), tileSize, tileSize, null);
//                        }
//
//                    }
//                }
//            }
//        }
        //repaint();
 //   }

    private static void rotateImage(BufferedImage src, int rotation, String tileName) throws IOException {
        double degrees = switch (rotation) {
            case 1 -> 90;
            case 2 -> 180;
            case 3 -> 270;
            default -> 0;
        };
        BufferedImage rotatedImage = rotateImage(src, degrees);
        imageMap.put(tileName, rotatedImage);
        saveImage(rotatedImage, tileName);
    }

    private static void saveImage(BufferedImage image, String fileName) {
        String path = "C:\\Users\\danil\\Desktop\\programowanie\\projekt-20260828T224622Z-1-001\\projekt\\server\\src\\main\\resources\\images\\" + fileName + ".png";
        File outputFile = new File(path);
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            System.out.println("Save Image Failed");
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage rotateImage(BufferedImage originalImage, double degrees) {
        // Wyliczenie wymiarów nowego obrazu
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Utworzenie pustego obrazu o takich samych wymiarach
        BufferedImage rotatedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        // Przesunięcie środka obrotu na środek obrazu i wykonanie obrotu
        g2d.rotate(Math.toRadians(degrees), width / 2.0, height / 2.0);

        // Rysowanie oryginalnego obrazu na nowym
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    public static Panel getInstance() {
        if (instance == null) {
            instance = new Panel();
        }
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            moveY -= 1;
        }
        if (e.getKeyChar() == 's') {
            moveY += 1;
        }
        if (e.getKeyChar() == 'a') {
            moveX += 1;
        }
        if (e.getKeyChar() == 'd') {
            moveX -= 1;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
