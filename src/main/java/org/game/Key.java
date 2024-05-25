package org.game;

import java.util.ArrayList;
import java.util.List;

public class Key {
    private static int key;
    private static final List<Integer> pressedKeys;
    private static int action;

    static {
        pressedKeys = new ArrayList<>();
    }

    public static void update(Integer key, int action) {
        if (action == 1) {
            pressedKeys.add(key);
        }
        if (action == 0) {
            pressedKeys.remove(key);
        }
        Key.key = key;
        Key.action = action;
    }


    public static int getKey() {
        return key;
    }

    public static int getAction() {
        return action;
    }

    public static List<Integer> getPressedKeys() {
        return pressedKeys;
    }

    public static boolean isPressed(int key) {
        return pressedKeys.contains(key);
    }
}
