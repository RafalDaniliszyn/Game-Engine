package org.barneys.server;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigManager {
    public static Config config;

    public ConfigManager() {
        createConfigFile();
        config = load();
    }

    private Config load() {
        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(Config.class, loaderOptions));
        try (InputStream in = new FileInputStream("config/config.yml")) {
            return yaml.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createConfigFile() {
        Path basePath = Path.of(System.getProperty("user.dir"), "config");
        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path serverConfig = basePath.resolve("config.yml");
        File file = serverConfig.toFile();
        if (!file.exists()) {
            Map<String, Object> defaultConfig = Map.of(
                    "JDBC_PORT", 5432,
                    "JDBC_HOST", "localhost",
                    "HIBERNATE_USERNAME", "postgres",
                    "HIBERNATE_PASSWORD", "rafal",
                    "WORLD_SIZE", 2
            );

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            try (FileWriter writer = new FileWriter(file)) {
                yaml.dump(defaultConfig, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
