package org.barneys.server.destroy;

import org.barneys.processData.inputPipeline.Pipeline;

public class DestroyStrategy {
    private final Pipeline<DestroyDto, DestroyDto> pipeline;

    public DestroyStrategy() {
        this.pipeline = new Pipeline<>(
                new DestroyRangePipe()
                        .andThen(new RemoveTilesPipe())
                        .andThen(new DropPipe())
                        .andThen(new SendDestroyModelPipe())
                        .andThen(new SendDropItemPipe())
        );
    }

    public void runDestroyPipeline(DestroyDto destroyDto) {
        pipeline.process(destroyDto);
    }

}
