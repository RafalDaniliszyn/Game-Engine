package game.isometric;

public class WorldSettings {
    /**
     * Chunk must be square.
     * CHUNK_SIZE is number of tiles on one side.
     */
    public static final Integer CHUNK_SIZE;
    public static final Integer FLOORS;

    static {
        CHUNK_SIZE = 50;
        FLOORS = 2;
    }

}
