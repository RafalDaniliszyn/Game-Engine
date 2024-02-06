package org.game.system;

import org.game.GameData;
import org.game.Key;
import org.game.component.MoveComponent;
import org.game.component.PositionComponent;
import org.game.renderer.Camera;
import org.joml.*;

import java.lang.Math;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class MoveSystem extends BaseSystem {

    private boolean KEY_W;
    private boolean KEY_A;
    private boolean KEY_S;
    private boolean KEY_D;
    private boolean KEY_SPACE;

    public MoveSystem(GameData gameData) {
        super(gameData);
    }

    @Override
    public void update(float deltaTime) {
        getInputKeys();
        move(deltaTime);
    }

    @Override
    public void delete() {

    }

    @Override
    public void init() {

    }

    private void move(float dt) {
        getGameData().getEntities(PositionComponent.class, MoveComponent.class).forEach((id, entity)-> {
            PositionComponent meshPos = entity.getComponent(PositionComponent.class);
            jump(dt, meshPos);
            MoveComponent move = entity.getComponent(MoveComponent.class);
            Vector3f moveVector = move.getMoveVector();
            float speed = move.getSpeed();
            Vector3f newPos = meshPos.getPosition();


            Vector3f camRotation = Camera.getCameraRotation();
            Vector3f meshPosition = meshPos.getPosition();

            if (KEY_W) {
                moveVector.x = (float) Math.sin(Math.toRadians(camRotation.y));
                moveVector.z = (float) Math.cos(Math.toRadians(camRotation.y));
            } else {
                moveVector.z = 0.0f;
                moveVector.x = 0.0f;
            }

            if (moveVector.x != 0.0f || moveVector.z != 0.0f) {
                newPos.x -= moveVector.x * speed * dt;
                newPos.z -= moveVector.z * speed * dt;
            }

            float horizontalDist = (float) (Camera.distance * Math.cos(Math.toRadians(camRotation.x)));
            float verticalDist = (float) (Camera.distance * Math.sin(Math.toRadians(camRotation.x)));

            Camera.cameraPosition.x = meshPosition.x + (float) (horizontalDist * Math.sin(Math.toRadians(camRotation.y)));
            Camera.cameraPosition.z = meshPosition.z + (float) (horizontalDist * Math.cos(Math.toRadians(camRotation.y)));
            Camera.cameraPosition.y = 10.0f + meshPosition.y + (float) (verticalDist * Math.sin(Math.toRadians(camRotation.x)));


            meshPos.setLastPosition(meshPos.getPosition());
            meshPos.setPosition(newPos);
        });
    }

    private void getInputKeys() {
        if (Key.key == GLFW_KEY_W) {
           KEY_W = true;
        }
        if (Key.key == GLFW_KEY_S) {
           KEY_S = true;
        }
        if (Key.key == GLFW_KEY_A) {
           KEY_A = true;
        }
        if (Key.key == GLFW_KEY_D) {
            KEY_D = true;
        }
        if (Key.key == GLFW_KEY_SPACE) {
           KEY_SPACE = true;
           jump = true;
        }
        if (Key.action == GLFW_RELEASE) {
            KEY_W = false;
            KEY_S = false;
            KEY_A = false;
            KEY_D = false;
            KEY_SPACE = false;
        }
    }

    float forceUp = 0.0f;
    boolean jump;
    private void jump(float dt, PositionComponent position) {
        if (jump) {
            Vector3f newPos = position.getPosition();
            if (newPos.y < 0.0f) {
                newPos.y = 0.0f;
                position.setPosition(newPos);
                jump = false;
                return;
            }
            if (forceUp < 1.0f) {
                newPos.y += 5 * dt;
            } else {
                newPos.y -= 5 * dt;
            }

            if (forceUp < 2.0f) {
                forceUp += 0.1f;
            }else {
                forceUp = 0.0f;
                jump = false;
            }
            if (newPos.y > 0.0f) {
                position.setPosition(newPos);
            } else {
                jump = false;
            }
        }

    }

    private void randomMove(MoveComponent move) {
        Random random = new Random();
        int randomMove = random.nextInt(4);
        int randomChange = random.nextInt(100);
        if (randomChange > 2) {
            return;
        }
        switch (randomMove) {
            case 0 -> move.getMoveVector().x = 1.0f;
            case 1 -> move.getMoveVector().z = 1.0f;
            case 2 -> move.getMoveVector().z = -1.0f;
            case 3 -> move.getMoveVector().x = -1.0f;

        }
    }


}
