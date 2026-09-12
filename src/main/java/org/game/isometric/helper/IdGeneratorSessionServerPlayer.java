package org.game.isometric.helper;

public class IdGeneratorSessionServerPlayer {
    private static Long currentId;
    static {
        currentId = 0L;
    }

    public static Long getNextId() {
        currentId+=1;
        return currentId;
    }
}
