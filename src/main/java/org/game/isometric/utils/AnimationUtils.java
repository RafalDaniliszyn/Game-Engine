package org.game.isometric.utils;

import org.game.isometric.component.AnimationComponent2D;
import java.util.List;

public class AnimationUtils {
    public static boolean isAnyAnimationActive(List<AnimationComponent2D> animationComponentList) {
        for (AnimationComponent2D animation : animationComponentList) {
            if (animation.isActive()) {
                return true;
            }
        }
        return false;
    }
}
