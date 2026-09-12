package org.barneys.server.destroy;

import org.barneys.processData.inputPipeline.Pipe;

public class DestroyRangePipe implements Pipe<DestroyDto, DestroyDto> {
    @Override
    public DestroyDto process(DestroyDto destroyDto) {
        destroyDto.setRange(8);
        return destroyDto;
    }
}
