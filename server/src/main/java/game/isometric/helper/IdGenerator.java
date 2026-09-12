package game.isometric.helper;

import java.util.HashSet;
import java.util.Set;

public class IdGenerator {
    private static Long currentId;
    private static final Set<Long> allocatedPool;
    static {
        currentId = 0L;
        allocatedPool = new HashSet<>();
    }

    public static Long getNextId() {
        while (allocatedPool.contains(currentId)) {
            currentId+=1;
        }
        allocatedPool.add(currentId);
        return currentId;
    }

    public static void importAllocatedIds(Set<Long> ids) {
        allocatedPool.addAll(ids);
    }

}
