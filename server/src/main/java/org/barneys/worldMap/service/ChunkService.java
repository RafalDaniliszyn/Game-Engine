package org.barneys.worldMap.service;

import game.isometric.entity.Entity;
import org.barneys.worldMap.Chunk;
import java.util.Deque;
import java.util.List;

public interface ChunkService {
    List<Entity> fillChunk(Chunk chunk, Deque<String>[][] labelDeque);
}
