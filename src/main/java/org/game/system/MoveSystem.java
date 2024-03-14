package org.game.system;

import org.game.GameData;
import org.game.Key;
import org.game.mouse.MouseInput;
import org.game.component.MoveComponent;
import org.game.component.PositionComponent;
import org.game.helper.PositionHelper;
import org.game.Camera;
import org.joml.Vector3f;
import java.lang.Math;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MoveSystem extends BaseSystem {

    private boolean KEY_W;
    private boolean KEY_A;
    private boolean KEY_S;
    private boolean KEY_D;
    private boolean KEY_SPACE;
    private boolean WHEEL_UP;
    private boolean WHEEL_DOWN;

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
            up(dt, meshPos);
            MoveComponent move = entity.getComponent(MoveComponent.class);
            if (move.isJump()) {
                jump(dt, meshPos, move);
            }
            Vector3f moveVector = move.getMoveVector();
            float speed = move.getSpeed();
            Vector3f newPos = new Vector3f(meshPos.getPosition());

            Vector3f camRotation = Camera.getCameraRotation();
            Vector3f meshPosition = meshPos.getPosition();

            if (KEY_W) {
                moveVector.x = (float) Math.sin(Math.toRadians(camRotation.y));
                moveVector.z = (float) Math.cos(Math.toRadians(camRotation.y));
                meshPos.setLastMoveVector(moveVector);
            } else {
                moveVector.z = 0.0f;
                moveVector.x = 0.0f;
            }
            if (moveVector.x != 0.0f || moveVector.z != 0.0f) {
                newPos.x -= moveVector.x * speed * dt;
                newPos.z -= moveVector.z * speed * dt;
                if (newPos.x <= 0.0f || newPos.z <= 0.0f) {
                    return;
                }
                try {
                    newPos.y = PositionHelper.getPositionY(getGameData().getHeightMap(), newPos.x, newPos.z, 7.8125005f, 7.8125005f);
                } catch (IndexOutOfBoundsException exception){
                    return;
                }

                float x = moveVector.x * speed * dt;
                float z = moveVector.z * speed * dt;
                getGameData().updateSkyPos(x, z);
            }


            float horizontalDist = (float) (Camera.distance * Math.cos(Math.toRadians(camRotation.x)));
            float verticalDist = (float) (Camera.distance * Math.sin(Math.toRadians(camRotation.x)));

            float verticalHeight = 2.0f;
            Camera.cameraPosition.x = meshPosition.x + (float) (horizontalDist * Math.sin(Math.toRadians(camRotation.y)));
            Camera.cameraPosition.z = meshPosition.z + (float) (horizontalDist * Math.cos(Math.toRadians(camRotation.y)));
            Camera.cameraPosition.y = verticalHeight + meshPosition.y + (float) (verticalDist * Math.sin(Math.toRadians(camRotation.x)));

            meshPos.setRotationY(camRotation.y);
            meshPos.rotateY(-90.0f);
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
        }
        if (MouseInput.WHEEL_UP) {
            Camera.distance -= 0.1f;
        }
        if (MouseInput.WHEEL_DOWN) {
            Camera.distance += 0.1f;
        }
        if (Key.action == GLFW_RELEASE) {
            KEY_W = false;
            KEY_S = false;
            KEY_A = false;
            KEY_D = false;
            KEY_SPACE = false;
            MouseInput.WHEEL_UP = false;
            MouseInput.WHEEL_DOWN = false;
        }
    }

    private void up(float dt, PositionComponent position) {
        if (KEY_SPACE) {
            Vector3f newPos = position.getPosition();
            newPos.y += 5 * dt;
            position.setPosition(newPos);
        }
    }

    float forceUp = 0.0f;
    private void jump(float dt, PositionComponent position, MoveComponent move) {
            Vector3f newPos = position.getPosition();
            if (newPos.y < 0.0f) {
                newPos.y = 0.0f;
                position.setPosition(newPos);
                return;
            }
            if (move.isJump()) {
                if (forceUp < 1.0f) {
                    newPos.y += 5 * dt;
                } else {
                    newPos.y -= 5 * dt;
                }

                if (forceUp < 2.0f ) {
                    forceUp += 0.1f;
                }else {
                    forceUp = 0.0f;
                    move.setJump(false);
                }
                if (newPos.y > 0.0f) {
                    position.setPosition(newPos);
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
