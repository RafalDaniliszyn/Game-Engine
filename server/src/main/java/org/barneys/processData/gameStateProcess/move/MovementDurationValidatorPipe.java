package org.barneys.processData.gameStateProcess.move;

import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.WorldState;
import org.barneys.model.GameStateModel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementDurationValidatorPipe implements Pipe<GameStateModel, GameStateModel> {

    private final Map<UUID, Long> lastMoveTime;

    public MovementDurationValidatorPipe() {
        this.lastMoveTime = new HashMap<>();
    }

    @Override
    public GameStateModel process(GameStateModel gameStateModel) {
        if (gameStateModel == null) {
            return null;
        }
        User user = WorldState.getByUuid(gameStateModel.getUserUuid());
        if (user == null) {
            return null;
        }

        PlayerEntityModel playerEntityModel = user.getPlayerEntityModel();
        int clientX = gameStateModel.getTileX();
        int clientY = gameStateModel.getTileY();
        int currentPositionX = playerEntityModel.getPositionX();
        int currentPositionY = playerEntityModel.getPositionY();

        if (clientX != currentPositionX || clientY != currentPositionY) {
            int distance = Math.abs(currentPositionX - clientX) + Math.abs(currentPositionY - clientY);
            if (isValidMovementDuration(user.getUuid(), 100000, distance)) {
                return gameStateModel;
            }
        }
        return null;
    }

    private boolean isValidMovementDuration(UUID userUuid, long standardMoveDuration, int distance) {
        long now = System.currentTimeMillis();
        User user = WorldState.getByUuid(userUuid);
        if (user == null || user.getPlayerEntityModel() == null) {
            return false;
        }
        if (!lastMoveTime.containsKey(userUuid)) {
            lastMoveTime.put(userUuid, now);
        } else {
            Long lastMove = lastMoveTime.get(userUuid);
            long duration = now - lastMove;
            long speed = user.getPlayerEntityModel().getMovementSpeed();
            long desiredDuration = ((standardMoveDuration * distance) / speed - 50);
            lastMoveTime.put(userUuid, now);
            System.out.println("MTS:" + duration + " | " + desiredDuration);
            if (duration < desiredDuration) {
                //player move too fast
                System.out.println("Player move too fast {\ndistance: " + distance + "\ndesired duration: " + desiredDuration + "\nactual duration: " + duration);
                return false;
            }
        }
        return true;
    }
}
