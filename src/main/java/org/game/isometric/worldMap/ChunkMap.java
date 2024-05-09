package org.game.isometric.worldMap;

import org.game.GameData;

import static org.game.isometric.WorldSettings.WORLD_SIZE;

public class ChunkMap {

    private final Chunk[][] chunks;

    public ChunkMap(GameData gameData) {
        this.chunks = new Chunk[WORLD_SIZE][WORLD_SIZE];
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                Chunk chunk = new Chunk(gameData, i, j);
                this.chunks[i][j] = chunk;
            }
        }
    }

    public Chunk[][] getChunks() {
        return chunks;
    }
}
