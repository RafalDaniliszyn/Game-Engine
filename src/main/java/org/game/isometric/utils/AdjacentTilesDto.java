package org.game.isometric.utils;

import org.game.isometric.blockLoader.Side;
import java.util.List;
import java.util.Map;

public record AdjacentTilesDto(Map<Side, List<Long>> adjacentTilesMap, List<Long> adjacentTilesList) {
}
