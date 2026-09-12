package org.game.network.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.isometric.GameState;
import org.game.isometric.Input;
import org.game.network.model.BaseModel;
import org.game.network.model.InitModel;
import org.game.network.processData.*;

import java.util.List;

public class GameClientHandler extends SimpleChannelInboundHandler<BaseModel> {

    private final List<ProcessData> processDataStrategyList;

    public GameClientHandler() {
        processDataStrategyList = List.of(
                new PlayerStateStrategy(),
                new ChannelActiveStrategy(),
                new WorldMapStrategy());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //Send user input keys map to the server.
        InitModel initModel = new InitModel(Input.getInputMap());
        System.out.println(initModel);
        ctx.writeAndFlush(initModel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseModel baseModel) {
        System.out.println("Client received model type: " + baseModel.getDataType());
        getStrategy(baseModel).onChannelReadProcess(ctx.channel(), baseModel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        GameState.setOffline();
        ctx.close();
    }

    private ProcessData getStrategy(BaseModel model) {
        for (ProcessData strategy : processDataStrategyList) {
            if (strategy.getDataType().equals(model.getDataType())) {
                return strategy;
            }
        }
        return new DefaultProcessDataStrategy();
    }
}
