package org.game.network.client.incomingDataHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.entity.Entity;
import org.game.isometric.component.DestroyComponent2D;
import org.game.network.client.model.DestroyModel;

import static org.game.GameData.gameData;
import static org.game.isometric.component.ComponentSource.SERVER;

public class DestroyHandler extends SimpleChannelInboundHandler<DestroyModel> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DestroyModel model) {
        System.out.println("Client received DestroyModel: " + model);
        if (gameData != null) {
            Long bottomEntityIdFromTile = gameData.getWorldMapData().getBottomEntityIdFromTile(model.getFloor(), model.getTileX(), model.getTileY());
            Entity entity = gameData.getEntity(bottomEntityIdFromTile);
            if (entity != null && !Entity.State.DESTROYED.equals(entity.getState())) {
                entity.addComponent(new DestroyComponent2D(0, true, SERVER, model.getTileX(), model.getTileY(), model.getFloor()));
            }
        }
    }
}
