package org.barneys.server.destroy;

import game.isometric.entity.Entity;
import org.barneys.WorldState;
import org.barneys.processData.inputPipeline.Pipe;
import org.barneys.server.modelHandler.DestroyModel;
import org.barneys.worldMap.WorldMapUtils;
import java.util.Random;

public class RemoveTilesPipe implements Pipe<DestroyDto, DestroyDto> {

    private final int defaultForce = 20;

    @Override
    public DestroyDto process(DestroyDto destroyDto) {
        Random random = new Random();
        int range = destroyDto.getRange();
        for (int i = -range/2; i < range/2; i++) {
            for (int j = -range/2; j < range/2; j++) {
                int randomInteger = random.nextInt(100);
                if (randomInteger - defaultForce < (100 / (1 + Math.abs(i) + Math.abs(j)))) {
                    int x = destroyDto.getTileX() + i;
                    int y = destroyDto.getTileY() + j;
                    Long entityIdToRemove = WorldState.getWorldMap().getBottomEntityIdFromTile(destroyDto.getFloor(), x, y);
                    Entity entityToRemove = WorldState.entityMapById.get(entityIdToRemove);
                    if (entityToRemove != null) {
                        destroyDto.addDestroyedEntity(new DestroyedEntityDto(x, y, destroyDto.getFloor(), entityToRemove.getProperties().getDrop()));
                        WorldMapUtils.removeTile(x, y, destroyDto.getFloor());
                        destroyDto.addDestroyed(new DestroyModel(x, y, destroyDto.getFloor(), destroyDto.getLabel()));
                    }
                }
            }
        }
        destroyDto.addDestroyed(new DestroyModel(destroyDto.getTileX(), destroyDto.getTileY(), destroyDto.getFloor(), destroyDto.getLabel()));
        return destroyDto;
    }

}
