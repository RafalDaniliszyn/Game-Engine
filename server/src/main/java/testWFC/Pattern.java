package testWFC;

import java.util.List;

import static testWFC.Rules.*;

public class Pattern {

    private String label;
    private int width = 0;
    private int height = 0;
    String[][] tiles;


    public Pattern(String[][] tiles) {
        this.tiles = tiles;
        for (int i = 0; i < tiles.length; i++) {
            for (int i1 = 0; i1 < tiles[0].length; i1++) {
                if (tiles[i][i1] == null) {
                    tiles[i][i1] = "";
                } else {
                    width = Math.max(i, width);
                    height = Math.max(i1, height);
                }
            }
        }
    }
}
