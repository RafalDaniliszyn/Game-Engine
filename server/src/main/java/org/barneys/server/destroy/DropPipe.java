package org.barneys.server.destroy;

import game.isometric.entity.Entity;
import org.barneys.WorldState;
import org.barneys.model.WorldMapModel;
import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.server.modelHandler.DestroyModel;
import org.barneys.worldMap.WorldMapUtils;

import java.util.Random;

public class DropPipe implements Pipe<DestroyDto, DestroyDto> {

    @Override
    public DestroyDto process(DestroyDto destroyDto) {
        destroyDto.getDestroyedEntityDtoList().forEach(destroyedEntityDto -> {
            Random random = new Random();
            destroyedEntityDto.getDrop().forEach((entityLabel, chance) -> {
                int randomInt = random.nextInt(100);
                if (randomInt <= chance) {
                    destroyDto.addDropItem(destroyedEntityDto.getX(), destroyedEntityDto.getY(), destroyedEntityDto.getFloor(), entityLabel);
                }
            });
        });
        return destroyDto;
    }
}
