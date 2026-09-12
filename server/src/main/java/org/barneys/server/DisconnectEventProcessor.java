package org.barneys.server;

import database.DatabaseConnector;
import game.isometric.entity.Entity;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.barneys.WorldState;
import org.barneys.worldMap.Chunk;
import org.barneys.worldMap.WorldMap;
import org.barneys.worldMap.WorldMapUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DisconnectEventProcessor implements GenericFutureListener<ChannelFuture> {


    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        DatabaseConnector.save(future.channel().id());
        //testowo:
        Chunk[] chunks = WorldState.getWorldMap().getChunks();
        for (int i = 0; i < chunks.length; i++) {
            int x = chunks[i].getX();
            int y = chunks[i].getY();
            int floor = chunks[i].getFloor();

            String fileName = "chunk_" + x + "_" + y + "_" + floor + ".bin";
            WorldMapUtils.saveMap(chunks[i], new File("data", fileName));
        }
        Map<String, Entity> entityMap = WorldState.entityMap;
        Map<Long, String> idLabelMap = new HashMap<>();
        entityMap.forEach((label, entity) -> {
            idLabelMap.put(entity.getId(), label);
        });
        Map<Long, Entity> entities = WorldState.getWorldMap().getEntities();
        entities.forEach((id, entity) -> {
            idLabelMap.put(id, entity.getProperties().getLabel());
        });
        WorldMapUtils.saveEntities(idLabelMap);

    }
}
