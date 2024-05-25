package org.game.isometric.entity;

import org.game.entity.Entity;
import org.game.entity.EntityProperties;
import org.game.isometric.WorldSettings;
import org.game.isometric.blockLoader.Side;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.CollisionComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.MoveComponent2D;
import org.game.isometric.component.PlayerComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.joml.Vector2f;
import java.util.HashMap;
import java.util.Map;

public class PlayerEntity2D extends Entity {

    public PlayerEntity2D(int textureID, int positionX, int positionY) {
        super(new EntityProperties.EntityPropertiesBuilder()
                .setCollidable(true)
                .setDraggable(false)
                .setLabel("player")
                .setStackable(false)
                .setQuantity(1)
                .setDepth(-0.1f)
                .build());

        float tileSize = WorldSettings.TILE_SIZE - WorldSettings.TILE_OVERLAP_LENGTH;
        int x = (int) (positionX * tileSize);
        int y = (int) (positionY * tileSize);

        Vector2f position = new Vector2f(x, y);

        PlayerComponent2D playerComponent2D = new PlayerComponent2D();
        PositionComponent2D positionComponent2D = new PositionComponent2D(position);
        MeshComponent2D meshComponent2D = new MeshComponent2D(textureID, new Vector2f(1.0f, 1.0f));
        MoveComponent2D moveComponent2D = new MoveComponent2D(250.0f);
        CollisionComponent2D collisionComponent2D = new CollisionComponent2D();

        EntityProperties properties = this.getProperties();
        Integer playerUp = TextureManager2D.getTextureIdByLabel("TRACTOR_UP");
        Integer playerDown = TextureManager2D.getTextureIdByLabel("TRACTOR_DOWN");
        Integer playerLeft = TextureManager2D.getTextureIdByLabel("TRACTOR_LEFT");
        Integer playerRight = TextureManager2D.getTextureIdByLabel("TRACTOR_RIGHT");
        Map<Side, Integer> sideTextureMap = new HashMap<>();
        sideTextureMap.put(Side.UP, playerUp);
        sideTextureMap.put(Side.DOWN, playerDown);
        sideTextureMap.put(Side.LEFT, playerLeft);
        sideTextureMap.put(Side.RIGHT, playerRight);
        properties.setReplaceableTextureIdMap(sideTextureMap);

        Integer digUp1 = TextureManager2D.getTextureIdByLabel("TRACTOR_UP_1_ANIMATION");
        Integer digUp2 = TextureManager2D.getTextureIdByLabel("TRACTOR_UP_2_ANIMATION");
        Integer digUp3 = TextureManager2D.getTextureIdByLabel("TRACTOR_UP_3_ANIMATION");

        Integer digDown1 = TextureManager2D.getTextureIdByLabel("TRACTOR_DOWN_1_ANIMATION");
        Integer digDown2 = TextureManager2D.getTextureIdByLabel("TRACTOR_DOWN_2_ANIMATION");
        Integer digDown3 = TextureManager2D.getTextureIdByLabel("TRACTOR_DOWN_3_ANIMATION");

        Integer digLeft1 = TextureManager2D.getTextureIdByLabel("TRACTOR_LEFT_1_ANIMATION");
        Integer digLeft2 = TextureManager2D.getTextureIdByLabel("TRACTOR_LEFT_2_ANIMATION");
        Integer digLeft3 = TextureManager2D.getTextureIdByLabel("TRACTOR_LEFT_3_ANIMATION");

        Integer digRight1 = TextureManager2D.getTextureIdByLabel("TRACTOR_RIGHT_1_ANIMATION");
        Integer digRight2 = TextureManager2D.getTextureIdByLabel("TRACTOR_RIGHT_2_ANIMATION");
        Integer digRight3 = TextureManager2D.getTextureIdByLabel("TRACTOR_RIGHT_3_ANIMATION");

        Map<Integer, Integer> animationUp = new HashMap<>();
        animationUp.put(1, digUp1);
        animationUp.put(2, digUp2);
        animationUp.put(3, digUp3);

        Map<Integer, Integer> animationDown = new HashMap<>();
        animationDown.put(1, digDown1);
        animationDown.put(2, digDown2);
        animationDown.put(3, digDown3);

        Map<Integer, Integer> animationLeft = new HashMap<>();
        animationLeft.put(1, digLeft1);
        animationLeft.put(2, digLeft2);
        animationLeft.put(3, digLeft3);

        Map<Integer, Integer> animationRight = new HashMap<>();
        animationRight.put(1, digRight1);
        animationRight.put(2, digRight2);
        animationRight.put(3, digRight3);

        AnimationComponent2D animationComponentUp = new AnimationComponent2D(animationUp, 500, "UP");
        addComponent(animationComponentUp);
        AnimationComponent2D animationComponentDown = new AnimationComponent2D(animationDown, 500, "DOWN");
        addComponent(animationComponentDown);
        AnimationComponent2D animationComponentLeft = new AnimationComponent2D(animationLeft, 500, "LEFT");
        addComponent(animationComponentLeft);
        AnimationComponent2D animationComponentRight = new AnimationComponent2D(animationRight, 500, "RIGHT");
        addComponent(animationComponentRight);

//        Integer player1 = TextureManager2D.getTextureIdByLabel("player1");
//        Integer player2 = TextureManager2D.getTextureIdByLabel("player2");
//        Integer player3 = TextureManager2D.getTextureIdByLabel("player3");
//        Integer player4 = TextureManager2D.getTextureIdByLabel("player4");
//        Integer player5 = TextureManager2D.getTextureIdByLabel("player5");
//        Integer player6 = TextureManager2D.getTextureIdByLabel("player6");
//        Map<Integer, Integer> animation = new HashMap<>();
//        animation.put(1, player1);
//        animation.put(2, player2);
//        animation.put(3, player3);
//        animation.put(4, player4);
//        animation.put(5, player5);
//        animation.put(6, player6);
//        AnimationComponent2D animationComponent2D = new AnimationComponent2D(animation, 300, "");
        addComponent(playerComponent2D);
        addComponent(positionComponent2D);
        addComponent(meshComponent2D);
        addComponent(moveComponent2D);
        addComponent(collisionComponent2D);
  //      addComponent(animationComponent2D);
    }
}
