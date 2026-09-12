package org.game.isometric.mesh;

import static org.game.isometric.WorldSettings.TILE_SIZE;

public class CubeModel extends RawModel {

    public CubeModel() {
        super(new float[] {
                // --- 0) BOTTOM (dół)  z = 0  (Twoja ściana, tylko dodany z=0) ---
                0.0f, TILE_SIZE,   0.0f,  0.0f, 1.0f,
                0.0f, 0.0f,0.0f,  0.0f, 0.0f,
                TILE_SIZE, TILE_SIZE,   0.0f,  1.0f, 1.0f,
                TILE_SIZE,   0.0f,0.0f,  1.0f, 0.0f,

                // --- 1) TOP (góra)  z = s ---
                0.0f, TILE_SIZE, TILE_SIZE,     0.0f, 1.0f,
                0.0f, 0.0f, TILE_SIZE,     0.0f, 0.0f,
                TILE_SIZE, TILE_SIZE, TILE_SIZE,     1.0f, 1.0f,
                TILE_SIZE,   0.0f, TILE_SIZE,     1.0f, 0.0f,

                // --- 2) FRONT (przód)  y = 0 ---
                0.0f, 0.0f, TILE_SIZE,     0.0f, 1.0f,
                0.0f, 0.0f,0.0f,  0.0f, 0.0f,
                TILE_SIZE,   0.0f, TILE_SIZE,     1.0f, 1.0f,
                TILE_SIZE,   0.0f,0.0f,  1.0f, 0.0f,

                // --- 3) BACK (tył)  y = s ---
                TILE_SIZE, TILE_SIZE, TILE_SIZE,     0.0f, 1.0f,
                TILE_SIZE, TILE_SIZE,   0.0f,  0.0f, 0.0f,
                0.0f, TILE_SIZE, TILE_SIZE,     1.0f, 1.0f,
                0.0f, TILE_SIZE,   0.0f,  1.0f, 0.0f,

                // --- 4) LEFT (lewa)  x = 0 ---
                0.0f, TILE_SIZE, TILE_SIZE,     0.0f, 1.0f,
                0.0f, TILE_SIZE,   0.0f,  0.0f, 0.0f,
                0.0f, 0.0f, TILE_SIZE,     1.0f, 1.0f,
                0.0f, 0.0f,0.0f,  1.0f, 0.0f,

                // --- 5) RIGHT (prawa)  x = s ---
                TILE_SIZE,   0.0f, TILE_SIZE,     0.0f, 1.0f,
                TILE_SIZE,   0.0f,0.0f,  0.0f, 0.0f,
                TILE_SIZE, TILE_SIZE, TILE_SIZE,     1.0f, 1.0f,
                TILE_SIZE, TILE_SIZE,   0.0f,  1.0f, 0.0f,
        });
    }

}
