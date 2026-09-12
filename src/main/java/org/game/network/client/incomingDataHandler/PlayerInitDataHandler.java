package org.game.network.client.incomingDataHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.game.GameData;
import org.game.entity.Entity;
import org.game.entity.EntityType;
import org.game.isometric.Camera2D;
import org.game.isometric.GameLoadingState;
import org.game.isometric.GameState;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.entity.PlayerEntity2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.network.client.model.PlayerInitDataModel;
import org.joml.Vector2f;

import static org.game.isometric.utils.PositionUtils.AbsoluteTilePosition;
import static org.game.isometric.utils.PositionUtils.getScreenPosition;

public class PlayerInitDataHandler extends SimpleChannelInboundHandler<PlayerInitDataModel> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PlayerInitDataModel model) throws Exception {
        System.out.println(model);
        //todo: tu zrobic obiekt zalogowanego gracza


        HandlerAction.add(() -> {
            if (!GameLoadingState.CREATE_PLAYER_COMPLETE) {
                createPlayer(model);
                System.out.println("CREATE_PLAYER_COMPLETE");
                Entity playerEntity = GameData.gameData.getEntity(GameState.getPlayerId());
                if (playerEntity != null) {
                    Vector2f screenPosition = getScreenPosition(new AbsoluteTilePosition(model.getX(), model.getY()));
                    PositionComponent2D positionComponent = playerEntity.getComponent(PositionComponent2D.class);
                    positionComponent.setPosition(screenPosition);
                    positionComponent.setFloor(model.getFloor());
                    GameState.setCurrentFloor(model.getFloor());
                    Camera2D.setCameraPosition(new Vector2f(screenPosition));
                    GameLoadingState.PLAYER_SERVER_DATA_RECEIVED = true;
                    return true;
                }
            }
            return false;
        });
    }

    private void createPlayer(PlayerInitDataModel model) {
        Integer playerTexture = TextureManager2D.getTextureIdByLabel("TRACTOR_UP");
        PlayerEntity2D playerEntity2D = new PlayerEntity2D(model.getSessionEntityId(), playerTexture, 38, 3, 0, EntityType.LOCAL);
        Camera2D.setCameraPosition(new Vector2f(playerEntity2D.getComponent(PositionComponent2D.class).getPosition()));
        playerEntity2D.setUserUuid(GameState.getUserUuid());
        GameData.gameData.putEntity(playerEntity2D);
        GameState.setPlayerId(playerEntity2D.getId());
        System.out.println(playerEntity2D);
        GameLoadingState.CREATE_PLAYER_COMPLETE = true;
    }
}
