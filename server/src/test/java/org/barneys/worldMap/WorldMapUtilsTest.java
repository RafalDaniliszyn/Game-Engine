package org.barneys.worldMap;

import org.junit.jupiter.api.Test;
import testWFC.PixelWFC;

import java.io.*;
import java.util.Deque;

import static game.isometric.WorldSettings.CHUNK_SIZE;
import static org.junit.jupiter.api.Assertions.*;

class WorldMapUtilsTest {


    @Test
    void saveMap() throws IOException {
        Chunk chunk = new Chunk(1, 2, 1);
        PixelWFC pixelWFC = new PixelWFC();
        Deque<String>[][] deques = pixelWFC.generateAll(1);
        Deque<Long>[][] entitiesQueue = chunk.getEntitiesQueue();
        for (int i = 0; i < entitiesQueue.length; i++) {
            for (int i1 = 0; i1 < entitiesQueue[0].length; i1++) {
                entitiesQueue[i][i1].add((long) deques[i][i1].size());
            }
        }

        entitiesQueue[0][0].add(42L);
        entitiesQueue[0][0].add(100L);

        File tempFile = new File("src/test/java/org/barneys/worldMap/chunk_0_0_0.bin");
        //tempFile.deleteOnExit();

        // when
        WorldMapUtils.saveMap(chunk, tempFile);

        // then
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(tempFile)))) {

            assertEquals(0x43484E4B, in.readInt()); // magic
            assertEquals(1, in.readShort());       // version

            assertEquals(1, in.readInt()); // x
            assertEquals(2, in.readInt()); // y
            assertEquals(1, in.readInt()); // floor
            assertEquals(CHUNK_SIZE, in.readShort());

            // tile 0,0
//            assertEquals(2, in.readShort());
//            assertEquals(42L, in.readLong());
//            assertEquals(100L, in.readLong());
        }

    }
}