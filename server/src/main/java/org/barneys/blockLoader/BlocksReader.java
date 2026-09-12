package org.barneys.blockLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.isometric.entity.Entity;
import game.isometric.entity.EntityProperties;
import org.barneys.WorldState;
import org.barneys.model.ChannelActiveModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class BlocksReader {
    private static final String path = "/blocks/block.json";

    public static ChannelActiveModel readBlocks() {
        try (InputStream resourceAsStream = BlocksReader.class.getResourceAsStream(path)) {
            if (resourceAsStream == null) {
                throw new RuntimeException("Not found: " + path);
            }
            byte[] bytes = resourceAsStream.readAllBytes();

            ObjectMapper objectMapper = new ObjectMapper();
            ChannelActiveModel channelActiveModel = objectMapper.readValue(bytes, ChannelActiveModel.class);
            List<EntityDto> entityDtoList = channelActiveModel.getEntityDto();
            entityDtoList.forEach(entityDto -> {
                WorldState.entityDtoMap.put(entityDto.getLabel(), entityDto);
                List<Entity> entities = EntityMapper.toEntityList(entityDto);
                entities.forEach(entity -> {
                    EntityProperties properties = entity.getProperties();
                    WorldState.entityMap.put(properties.getLabel(), entity);
                });
            });
            return channelActiveModel;
        } catch (IOException e) {
            throw new RuntimeException("Failed: " + path, e);
        }
    }
}
