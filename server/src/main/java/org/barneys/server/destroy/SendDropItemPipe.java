package org.barneys.server.destroy;

import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.server.handler.SimpleServerHandler;
import org.barneys.server.modelHandler.DropModel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SendDropItemPipe implements Pipe<DestroyDto, DestroyDto> {
    @Override
    public DestroyDto process(DestroyDto destroyDto) {
        List<DropModel> drop = destroyDto.getDrop();
        Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                drop.forEach(SimpleServerHandler::send);
            }
        }, destroyDto.getDuration());
        return destroyDto;
    }
}
