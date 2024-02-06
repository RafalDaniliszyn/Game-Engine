package org.game;

public class IdGenerator {
    private static Long currentId;
    static {
        currentId = 0L;
    }

    public static Long getNextId() {
        currentId+=1;
        return currentId;
    }

    public static Integer getNextIntegerId() {
        currentId+=1;
        return Math.toIntExact(currentId);
    }
}
