package org.game.isometric;

import org.game.key.KeySettings;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum contains input types and their associated keys.
 */
public enum Input {
    MOVE_FORWARD(KeySettings.MOVE_FORWARD),
    MOVE_BACKWARD(KeySettings.MOVE_BACKWARD),
    MOVE_LEFT(KeySettings.MOVE_LEFT),
    MOVE_RIGHT(KeySettings.MOVE_RIGHT);
    final int keyCode;
    private static final Map<Integer, Input> inputMap;
    static {
        inputMap = new HashMap<>();
        for (int i = 0; i < values().length; i++) {
            inputMap.put(values()[i].keyCode, values()[i]);
        }
    }

    Input(int keyCode) {
        this.keyCode = keyCode;
    }

    public static Map<Integer, Input> getInputMap() {
        return inputMap;
    }
}
