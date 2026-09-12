package org.barneys.worldMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.isometric.entity.Entity;
import org.barneys.WorldState;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static game.isometric.WorldSettings.CHUNK_SIZE;

public class WorldMapUtils {

    public static void removeTile(int x, int y, int floor) {
        WorldMap worldMap = WorldState.getWorldMap();
        Long entityId = worldMap.getBottomEntityIdFromTile(floor, x, y);
        Entity entity = WorldState.entityMapById.get(entityId);

        if (entity != null) {
            String afterDestroyLabel = entity.getProperties().getAfterDestroyLabel();
            if (afterDestroyLabel != null) {
                Entity destroyedEntity = WorldState.entityMap.get(afterDestroyLabel);
                worldMap.addEntityToTile(floor, x, y, destroyedEntity, true);
            }
        }
    }

    public static List<String> getFileTree(String path) {
        List<String> pathList = new ArrayList<>();
        Path basePath = Path.of(System.getProperty("user.dir"), path);
        try {
            Files.createDirectories(basePath);
            try (Stream<Path> paths = Files.walk(basePath)) {
                paths.forEach(nextPath -> {
                    if (nextPath.getFileName().toString().contains("chunk")) {
                        pathList.add(nextPath.toString());
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    public static Chunk loadChunk(String path) {
        try(DataInputStream in = new DataInputStream(new FileInputStream(path))) {
            // === HEADER ===
            int magic = in.readInt();
            short version = in.readShort();

            // === CHUNK INFO ===
            int x = in.readInt();
            int y = in.readInt();
            int floor = in.readInt();
            short chunkSize = in.readShort();

            // === TILE DATA ===
            Chunk chunk = new Chunk(x, y, floor);
            Deque<Long>[][] entitiesQueue = chunk.getEntitiesQueue();
            for (int i = 0; i < chunkSize; i++) {
                for (int j = 0; j < chunkSize; j++) {
                    short queueSize = in.readShort();

                    Deque<Long> idDeque = new ArrayDeque<>();
                    for (int k = 0; k < queueSize; k++) {
                        long entityId = in.readLong();
                        idDeque.add(entityId);
                    }
                    entitiesQueue[i][j] = idDeque;
                }
            }
            return chunk;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Long, String> loadEntities() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(
                    new File("data", "entityIdMap.json"),
                    new TypeReference<Map<Long, String>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveEntities(Map<Long, String> idLabelMap) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("data", "entityIdMap.json"), idLabelMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDir() {
        Path basePath = Path.of(System.getProperty("user.dir"), "data");
        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMap(Chunk[] chunks) throws IOException {
        for (int i = 0; i < chunks.length; i++) {
            int x = chunks[i].getX();
            int y = chunks[i].getY();
            int floor = chunks[i].getFloor();

            String fileName = "chunk_" + x + "_" + y + "_" + floor + ".bin";
            File data = new File("data", fileName);
            WorldMapUtils.saveMap(chunks[i], data);
        }
        Map<String, Entity> entityMap = WorldState.entityMap;
        Map<Long, String> idLabelMap = new HashMap<>();
        entityMap.forEach((label, entity) -> {
            idLabelMap.put(entity.getId(), label);
        });
        if (WorldState.getWorldMap() != null) {
            Map<Long, Entity> entities = WorldState.getWorldMap().getEntities();
            entities.forEach((id, entity) -> {
                idLabelMap.put(id, entity.getProperties().getLabel());
            });
        }
        WorldMapUtils.saveEntities(idLabelMap);
    }

    public static void saveMap(Chunk chunk, File file) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {

            // === HEADER ===
            out.writeInt(0x43484E4B); // "CHNK"
            out.writeShort(1);        // version

            // === CHUNK INFO ===
            out.writeInt(chunk.getX());
            out.writeInt(chunk.getY());
            out.writeInt(chunk.getFloor());
            out.writeShort(CHUNK_SIZE);

            // === TILE DATA ===
            Deque<Long>[][] tiles = chunk.getEntitiesQueue();

            for (int x = 0; x < CHUNK_SIZE; x++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    Deque<Long> queue = tiles[x][y];

                    out.writeShort(queue.size());

                    for (Long entityId : queue) {
                        out.writeLong(entityId);
                    }
                }
            }
        }
    }
}
