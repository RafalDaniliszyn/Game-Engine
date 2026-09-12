package org.barneys.worldMap.service;

import game.isometric.entity.Entity;
import game.isometric.entity.EntityProperties;
import game.isometric.utils.TileUtils;
import org.barneys.WorldState;
import org.barneys.blockLoader.EntityDto;
import org.barneys.blockLoader.EntityMapper;
import org.barneys.worldMap.Chunk;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class ChunkServiceImpl implements ChunkService {

    public List<Entity> fillChunk(Chunk chunk, Deque<String>[][] labelDeque) {
        Deque<Long>[][] entitiesQueue = chunk.getEntitiesQueue();
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < labelDeque.length; i++) {
            for (int j = 0; j < labelDeque.length; j++) {
                for (String label : labelDeque[i][j]) {
                    if (!WorldState.entityMap.containsKey(label)) {
                        generateRotatedEntity(label);
                    }
                    Entity entity = WorldState.entityMap.get(label);
                    if (entity == null) {
                        return Collections.emptyList();
                    }
                    if ("terrain".equals(entity.getProperties().getType())) {
                        TileUtils.addEntityOnBottom(entity.getId(), entitiesQueue[i][j]);

                    } else if ("item".equals(entity.getProperties().getType())) {
                        TileUtils.addEntity(entity.getId(), entitiesQueue[i][j]);
                    }
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    // TODO: 12/17/2025 generowanie bloków wyciągnać z silnika
    private void generateRotatedEntity(String label) {
        if (label.charAt(label.length() - 1) != 'D') {
            EntityDto rotatedEntityDto = WorldState.entityDtoMap.get(label.substring(0, label.length()-1));
            List<Entity> rotatedEntities = EntityMapper.toEntityList(rotatedEntityDto);
            rotatedEntities.forEach(rotatedEntity -> {
                EntityProperties properties = rotatedEntity.getProperties();
                properties.setLabel(label);
                WorldState.entityMap.put(label, rotatedEntity);
            });
            WorldState.updateEntityMapById();
        }
    }
}
