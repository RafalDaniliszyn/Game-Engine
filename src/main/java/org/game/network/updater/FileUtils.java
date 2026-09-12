package org.game.network.updater;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static void initFiles(String first, String other, String csq) {
        Path dir = Paths.get(first);
        Path resolved = dir.resolve(other);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Files.exists(resolved)) {
            try {
                if (csq == null) {
                    csq = "";
                }
                Files.writeString(resolved, csq);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String read(String first, String other) {
        Path dir = Paths.get(first);
        Path resolved = dir.resolve(other);
        String result = "";
        if (Files.exists(dir) && Files.exists(resolved)) {
            List<String> lines;
            try {
                lines = Files.readAllLines(resolved);
                StringBuilder builder = new StringBuilder();
                for (String line : lines) {
                    builder.append(line);
                }
                result =  builder.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
