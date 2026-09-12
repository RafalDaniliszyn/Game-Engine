
package org.game.isometric.renderer.renderer3DTest;

import org.game.component.mesh.MeshComponent;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public final class CubeFactory {

    private CubeFactory() {
    }

    /**
     * Tworzy teksturowany sześcian 3D.
     *
     * Layout wierzchołka:
     * x, y, z, nx, ny, nz, r, g, b, a, u, v
     */
    public static MeshComponent createCube(Vector3f position, Vector3f scale, int textureID) {
        float[] vertices = new float[] {
                // =========================
                // FRONT (+Z)
                // =========================
                -0.5f, -0.5f,  0.5f,   0f, 0f, 1f,   1f, 1f, 1f, 1f,   0f, 0f,
                0.5f, -0.5f,  0.5f,   0f, 0f, 1f,   1f, 1f, 1f, 1f,   1f, 0f,
                0.5f,  0.5f,  0.5f,   0f, 0f, 1f,   1f, 1f, 1f, 1f,   1f, 1f,
                -0.5f,  0.5f,  0.5f,   0f, 0f, 1f,   1f, 1f, 1f, 1f,   0f, 1f,

                // =========================
                // BACK (-Z)
                // =========================
                0.5f, -0.5f, -0.5f,   0f, 0f, -1f,  1f, 1f, 1f, 1f,   0f, 0f,
                -0.5f, -0.5f, -0.5f,   0f, 0f, -1f,  1f, 1f, 1f, 1f,   1f, 0f,
                -0.5f,  0.5f, -0.5f,   0f, 0f, -1f,  1f, 1f, 1f, 1f,   1f, 1f,
                0.5f,  0.5f, -0.5f,   0f, 0f, -1f,  1f, 1f, 1f, 1f,   0f, 1f,

                // =========================
                // LEFT (-X)
                // =========================
                -0.5f, -0.5f, -0.5f,  -1f, 0f, 0f,   1f, 1f, 1f, 1f,   0f, 0f,
                -0.5f, -0.5f,  0.5f,  -1f, 0f, 0f,   1f, 1f, 1f, 1f,   1f, 0f,
                -0.5f,  0.5f,  0.5f,  -1f, 0f, 0f,   1f, 1f, 1f, 1f,   1f, 1f,
                -0.5f,  0.5f, -0.5f,  -1f, 0f, 0f,   1f, 1f, 1f, 1f,   0f, 1f,

                // =========================
                // RIGHT (+X)
                // =========================
                0.5f, -0.5f,  0.5f,   1f, 0f, 0f,   1f, 1f, 1f, 1f,   0f, 0f,
                0.5f, -0.5f, -0.5f,   1f, 0f, 0f,   1f, 1f, 1f, 1f,   1f, 0f,
                0.5f,  0.5f, -0.5f,   1f, 0f, 0f,   1f, 1f, 1f, 1f,   1f, 1f,
                0.5f,  0.5f,  0.5f,   1f, 0f, 0f,   1f, 1f, 1f, 1f,   0f, 1f,

                // =========================
                // TOP (+Y)
                // =========================
                -0.5f,  0.5f,  0.5f,   0f, 1f, 0f,   1f, 1f, 1f, 1f,   0f, 0f,
                0.5f,  0.5f,  0.5f,   0f, 1f, 0f,   1f, 1f, 1f, 1f,   1f, 0f,
                0.5f,  0.5f, -0.5f,   0f, 1f, 0f,   1f, 1f, 1f, 1f,   1f, 1f,
                -0.5f,  0.5f, -0.5f,   0f, 1f, 0f,   1f, 1f, 1f, 1f,   0f, 1f,

                // =========================
                // BOTTOM (-Y)
                // =========================
                -0.5f, -0.5f, -0.5f,   0f, -1f, 0f,  1f, 1f, 1f, 1f,   0f, 0f,
                0.5f, -0.5f, -0.5f,   0f, -1f, 0f,  1f, 1f, 1f, 1f,   1f, 0f,
                0.5f, -0.5f,  0.5f,   0f, -1f, 0f,  1f, 1f, 1f, 1f,   1f, 1f,
                -0.5f, -0.5f,  0.5f,   0f, -1f, 0f,  1f, 1f, 1f, 1f,   0f, 1f
        };

        int[] indices = new int[] {
                // front
                0, 1, 2,
                2, 3, 0,

                // back
                4, 5, 6,
                6, 7, 4,

                // left
                8, 9, 10,
                10, 11, 8,

                // right
                12, 13, 14,
                14, 15, 12,

                // top
                16, 17, 18,
                18, 19, 16,

                // bottom
                20, 21, 22,
                22, 23, 20
        };

        MeshComponent mesh = new MeshComponent(vertices, indices, position, scale, textureID);
        mesh.setRenderMode(GL_TRIANGLES);
        mesh.setCullFace(true);
        return mesh;
    }

    /**
     * Wersja bez tekstury – jeśli shader toleruje textureID = 0.
     */
    public static MeshComponent createCube(Vector3f position, Vector3f scale) {
        return createCube(position, scale, 0);
    }
}
