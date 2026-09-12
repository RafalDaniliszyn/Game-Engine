package org.barneys.server.modelHandler;

import game.isometric.entity.Entity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.barneys.WorldState;
import org.barneys.blockLoader.EntityDto;
import org.barneys.blockLoader.EntityMapper;
import org.barneys.blockLoader.action.Action;
import org.barneys.blockLoader.action.ActionEnum;
import org.barneys.blockLoader.action.ExplosionAction;
import org.barneys.game.itemSpawn.ItemSpawnMessage;
import org.barneys.server.destroy.DestroyDto;
import org.barneys.server.destroy.DestroyStrategy;
import org.barneys.server.handler.SimpleServerHandler;
import org.barneys.worldMap.WorldMap;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.barneys.blockLoader.action.Action.Invoke.*;

/**
 * Server handler for incoming PutModel.
 */
public class PutModelHandler extends SimpleChannelInboundHandler<PutModel> {

    private static final String TERRAIN = "terrain";
    private static final String ITEM = "item";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PutModel model) throws IOException {
        System.out.println("PutModelHandler: " + model);
        processPutModel(model);
    }

    private void processPutModel(PutModel model) throws IOException {
        Entity entity = WorldState.entityMap.get(model.getLabel());
        if (entity == null) {
            return;
        }
        String type = entity.getProperties().getType();
        if (TERRAIN.equals(type)) {
            addToWorldMap(model, entity);
            SimpleServerHandler.send(model);
        } else if (ITEM.equals(type)) {
            EntityDto entityDto = WorldState.entityDtoMap.get(entity.getProperties().getLabel());
            Optional<Entity> newItemEntity = EntityMapper.newItemEntity(entityDto);
            newItemEntity.ifPresent(e -> {
                ItemSpawnMessage itemSpawnMessage =
                        new ItemSpawnMessage(e.getProperties().getLabel(), e.getId(), model.getTileX(), model.getTileY(), model.getFloor());
                SimpleServerHandler.send(itemSpawnMessage);
            });

            //SimpleServerHandler.send(model);

            boolean addToMap = true;
            for (Action action : entity.getProperties().getActionList()) {
                System.out.println(action.getActionType() + " Invoke: " + action.getInvoke());
                if (ON_PUT.equals(action.getInvoke())) {
                    if (ActionEnum.ExplosionAction.equals(action.getActionType())) {
                        ExplosionAction explosionAction = (ExplosionAction) action;
                        DestroyStrategy destroyStrategy = new DestroyStrategy();
                        destroyStrategy.runDestroyPipeline(new DestroyDto(
                                model.getUserUuid(), model.getTileX(), model.getTileY(), model.getFloor(),
                                model.getLabel(), model.getLabel(), explosionAction.getDuration()
                        ));
                    }
                    if (action.isRemoveEntityAfter()) {
                        addToMap = false;
                    }
                }
            }
            if (addToMap) {
                //addToWorldMap(model, entity);
                addToWorldMap(model, newItemEntity.orElse(entity));
            }
        }
    }

    private void addToWorldMap(PutModel model, Entity entity) {
        WorldMap worldMap = WorldState.getWorldMap();
        if (entity != null) {
            // TODO: 10/15/2024 check if player has this item
            worldMap.addEntityToTile(model.getFloor(), model.getTileX(), model.getTileY(), entity, TERRAIN.equals(entity.getProperties().getType()));
        }
    }
}
