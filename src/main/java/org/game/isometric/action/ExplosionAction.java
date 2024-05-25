package org.game.isometric.action;

import org.game.GameData;
import org.game.entity.Entity;
import org.game.isometric.component.AnimationComponent2D;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.DestroyComponent2D;
import org.game.isometric.component.MeshComponent2D;
import org.game.isometric.component.PositionComponent2D;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.isometric.worldMap.WorldMapData;
import org.joml.Vector2f;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.game.entity.Entity.State.DESTROYED;
import static org.game.isometric.utils.PositionUtils.AbsoluteTilePosition;
import static org.game.isometric.utils.PositionUtils.getAbsoluteTilePositionFromWorldSpace;

public class ExplosionAction extends Action {

    private final int explosionRange;
    private final AnimationComponent2D animationComponent;
    private boolean animationStarted;
    private final AnimationComponent2D explosionAnimation;
    private boolean explosionAnimationStarted;

    public ExplosionAction(int explosionRange, boolean removeEntityAfter, boolean removeActionAfter, Invoke invoke) {
        super(removeEntityAfter, removeActionAfter, invoke);
        this.explosionRange = explosionRange;

        // TODO: 5/24/2024 to refactor
        Map<Integer, Integer> textures = new HashMap<>();
        Integer dynamite_animation_1 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_1");
        Integer dynamite_animation_2 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_2");
        Integer dynamite_animation_3 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_3");
        Integer dynamite_animation_4 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_4");
        Integer dynamite_animation_5 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_5");
        Integer dynamite_animation_6 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_6");
        Integer dynamite_animation_7 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_7");
        Integer dynamite_animation_8 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_8");
        Integer dynamite_animation_9 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_9");
        Integer dynamite_animation_10 = TextureManager2D.getTextureIdByLabel("DYNAMITE_ANIMATION_10");
        textures.put(1, dynamite_animation_1);
        textures.put(2, dynamite_animation_2);
        textures.put(3, dynamite_animation_1);
        textures.put(4, dynamite_animation_2);
        textures.put(5, dynamite_animation_3);
        textures.put(6, dynamite_animation_2);
        textures.put(7, dynamite_animation_3);
        textures.put(8, dynamite_animation_4);
        textures.put(9, dynamite_animation_3);
        textures.put(10, dynamite_animation_4);
        textures.put(11, dynamite_animation_5);
        textures.put(12, dynamite_animation_4);
        textures.put(13, dynamite_animation_5);
        textures.put(14, dynamite_animation_6);
        textures.put(15, dynamite_animation_5);
        textures.put(16, dynamite_animation_6);
        textures.put(17, dynamite_animation_7);
        textures.put(18, dynamite_animation_6);
        textures.put(19, dynamite_animation_7);
        textures.put(20, dynamite_animation_8);
        textures.put(21, dynamite_animation_7);
        textures.put(22, dynamite_animation_8);
        textures.put(23, dynamite_animation_9);
        textures.put(24, dynamite_animation_10);

        this.animationComponent = new AnimationComponent2D(textures , 3000, "DYNAMITE");
        this.animationStarted = false;
        this.explosionAnimation = getExplosionAnimation();
        this.explosionAnimationStarted = false;
    }

    @Override
    public void processAction(Entity entity) {

    }

    @Override
    public void processActions(Entity entity, GameData gameData) {
        if (!animationStarted) {
            animationStarted = true;
            entity.addComponent(animationComponent);
            animationComponent.setActive(true);
        }
        if (animationComponent.isActive()) {
            return;
        } else if (!explosionAnimationStarted){
            explosionAnimationStarted = true;
            entity.addComponent(explosionAnimation);
            MeshComponent2D mesh = entity.getComponent(MeshComponent2D.class);
            mesh.setScale(new Vector2f(2.0f, 2.0f));
            explosionAnimation.setActive(true);
        }

        PositionComponent2D positionComponent = entity.getComponent(PositionComponent2D.class);
        Vector2f position = positionComponent.getPosition();
        AbsoluteTilePosition tilePosition = getAbsoluteTilePositionFromWorldSpace(position);
        int floor = positionComponent.getFloor();
        WorldMapData worldMapData = gameData.getWorldMapData();
        destroyInRange(gameData, tilePosition, floor, worldMapData);

        if (explosionAnimation.isActive()) {
            return;
        }

        this.setRemoveActionAfter(true);
        this.setRemoveEntityAfter(true);
    }

    private void destroyInRange(GameData gameData, AbsoluteTilePosition tilePosition, int floor, WorldMapData worldMapData) {
        Random random = new Random();
        int rangeOnSide = explosionRange / 2;
        for (int i = -(rangeOnSide); i < rangeOnSide; i++) {
            for (int j = -(rangeOnSide); j < rangeOnSide; j++) {
                double distanceX = rangeOnSide - Math.abs(i);
                double distanceY = rangeOnSide - Math.abs(j);
                if (distanceX < explosionRange / 2.0 || distanceY < explosionRange / 3.0) {
                    int randomNumber = random.nextInt(100);
                    if (randomNumber > 30) {
                        continue;
                    }
                }
                Long entityId = worldMapData.getBottomEntityIdFromTile(floor, tilePosition.x() + i, tilePosition.y() + j);
                Entity entityToDestroy = gameData.getEntity(entityId);
                addDestroyComponent(entityToDestroy);
            }
        }
    }

    private void addDestroyComponent(Entity entityToDestroy) {
        if (entityToDestroy != null && !DESTROYED.equals(entityToDestroy.getState())) {
            entityToDestroy.addComponent(new DestroyComponent2D(0, true));
        }
    }

    private AnimationComponent2D getExplosionAnimation() {
        Map<Integer, Integer> explosionAnimation = new HashMap<>();
        Integer explosionAnimation1 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_1");
        Integer explosionAnimation2 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_2");
        Integer explosionAnimation3 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_3");
        Integer explosionAnimation4 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_4");
        Integer explosionAnimation5 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_5");
        Integer explosionAnimation6 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_6");
        Integer explosionAnimation7 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_7");
        Integer explosionAnimation8 = TextureManager2D.getTextureIdByLabel("EXPLOSION_ANIMATION_8");
        explosionAnimation.put(1, explosionAnimation1);
        explosionAnimation.put(2, explosionAnimation2);
        explosionAnimation.put(3, explosionAnimation3);
        explosionAnimation.put(4, explosionAnimation4);
        explosionAnimation.put(5, explosionAnimation5);
        explosionAnimation.put(6, explosionAnimation6);
        explosionAnimation.put(7, explosionAnimation7);
        explosionAnimation.put(8, explosionAnimation8);
        return new AnimationComponent2D(explosionAnimation, 1000, "EXPLOSION_ANIMATION");
    }

    @Override
    public ComponentEnum getType() {
        return null;
    }
}
