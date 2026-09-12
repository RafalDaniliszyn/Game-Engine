package org.game.ui.newUiConcept.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Font {
    private final Map<Integer, Letter> letterMap;

    public Font(String dataPath) {
        this.letterMap = loadFont(dataPath);
    }

    public Letter getLetter(int charId) {
        return letterMap.get(charId);
    }

    private Map<Integer, Letter> loadFont(String dataPath) {
        Map<Integer, Letter> letterMap = new HashMap<>();
        String fontData = readFile(dataPath);
        Scanner scanner = new Scanner(fontData);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("\\s+");
            Letter letter = new Letter(
                    Float.parseFloat(split[1].split("=")[1]),
                    Float.parseFloat(split[2].split("=")[1]),
                    Float.parseFloat(split[3].split("=")[1]),
                    Float.parseFloat(split[4].split("=")[1]),
                    Float.parseFloat(split[5].split("=")[1]),
                    Float.parseFloat(split[6].split("=")[1]),
                    Float.parseFloat(split[7].split("=")[1])
            );
            letterMap.put(Integer.parseInt(split[0].split("=")[1]), letter);
        }
        return letterMap;
    }

    private String readFile(String dataPath) {
        InputStream resourceStream = Font.class.getResourceAsStream(dataPath);
        if (resourceStream == null) {
            throw new RuntimeException("Resource not found: " + dataPath);
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Font loading completed.");
        return builder.toString();
    }
}
