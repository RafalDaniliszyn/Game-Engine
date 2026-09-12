package org.game.network.updater;

import org.game.network.client.DataSync;
import org.game.network.client.model.DownloadUpdateModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class UpdateService {
    public void checkForUpdate() {
        Version version = getVersion();
        System.out.println("GameClient send checkForUpdate: " + version);
        DownloadUpdateModel model = new DownloadUpdateModel(version.getMajor(), version.getMinor(), version.getRelease());
        DataSync.sendVersionUpdateRequest(model);
    }

    public void updateVersion(int major, int minor, int release) {
        Version current = getVersion();
        if (current.getMajor() != major || current.getMinor() != minor || current.getRelease() != release) {
            updateVersion(new Version(major, minor, release));
        }
    }

    public boolean isUpToDate(int major, int minor, int release) {
        Version current = getVersion();
        return current.getMajor() == major && current.getMinor() == minor && current.getRelease() == release;
    }

    private Version getVersion() {
        Path propertiesDir = Paths.get("data/properties");
        Path properties = propertiesDir.resolve("settings.txt");
        if (Files.exists(propertiesDir) && Files.exists(properties)) {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(properties);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (String line : lines) {
                if (line.startsWith("version=")) {
                    String[] split = line.substring(8).split("\\.");
                    if (split.length == 3) {
                        return new Version(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                    }
                }
            }
        } else {
            FileUtils.initFiles("data/properties", "settings.txt", "version=1.0.0\nlanguage=en\n");
        }
        return new Version(0, 0, 0);
    }

    private void updateVersion(Version version) {
        Path propertiesDir = Paths.get("data/properties");
        Path properties = propertiesDir.resolve("settings.txt");
        if (!Files.exists(propertiesDir)) {
            return;
        }
        if (Files.exists(properties)) {
            List<String> lines;
            try {
                lines = Files.readAllLines(properties);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("version=")) {
                    updatedLines.add("version=" + version.getMajor() + "." + version.getMinor() + "." + version.getRelease());
                } else {
                    updatedLines.add(line);
                }
            }
            try {
                Files.write(properties, updatedLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
