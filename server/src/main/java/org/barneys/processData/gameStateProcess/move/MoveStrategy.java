package org.barneys.processData.gameStateProcess.move;

import org.barneys.processData.inputPipeline.Pipeline;
import org.barneys.model.GameStateModel;
import org.barneys.processData.gameStateProcess.PlayerActionStrategy;

public class MoveStrategy implements PlayerActionStrategy {

    private final Pipeline<GameStateModel, GameStateModel> pipeline;

    public MoveStrategy() {
        this.pipeline = new Pipeline<>(
                        new MovementDurationValidatorPipe()
                        .andThen(new CollisionPipe())
                        .andThen(new UpdatePositionPipe())
                        .andThen(new EnterActionPipe())
                        .andThen(new SendPlayerStateModelPipe())
        );
    }

    @Override
    public void execute(GameStateModel gameStateModel) {
        pipeline.process(gameStateModel);
    }

}
