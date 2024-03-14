package org.game.helper;

import org.game.component.mesh.MeshComponent;
import org.game.component.mesh.MeshData;
import org.joml.Vector3f;

public class MeshHelper {
    public static void calculateNormal(MeshData meshData, float rotationY) {
        int STRIDE = 12;
        int[] indices = meshData.getIndices();
        float[] vertices = meshData.getVertices();
        for (int i = 0; i < indices.length-3; i+=3) {
            int index1 = indices[i];
            int index2 = indices[i+1];
            int index3 = indices[i+2];
            float x1 = vertices[ index1 * STRIDE];
            float y1 = vertices[(index1 * STRIDE) + 1];
            float z1 = vertices[(index1 * STRIDE) + 2];
            Vector3f vec1 = new Vector3f(x1, y1, z1);
            float x2 = vertices[ index2 * STRIDE];
            float y2 = vertices[(index2 * STRIDE) + 1];
            float z2 = vertices[(index2 * STRIDE) + 2];
            Vector3f vec2 = new Vector3f(x2, y2, z2);
            float x3 = vertices[ index3 * STRIDE];
            float y3 = vertices[(index3 * STRIDE) + 1];
            float z3 = vertices[(index3 * STRIDE) + 2];
            Vector3f vec3 = new Vector3f(x3, y3, z3);

            vec1.rotateY(rotationY);
            vec2.rotateY(rotationY);
            vec3.rotateY(rotationY);

            Vector3f u = new Vector3f(vec2.sub(vec1));
            Vector3f v = new Vector3f(vec3.sub(vec1));
            u.cross(v);
            u.normalize();

            vertices[(index1 * STRIDE) + 9] = u.x;
            vertices[(index1 * STRIDE) + 10] = u.y;
            vertices[(index1 * STRIDE) + 11] = u.z;
        }
        meshData.setVertices(vertices);
    }

    public static void setNormal(MeshComponent meshComponent, float normal) {
        int STRIDE = 12;
        float[] vertices = meshComponent.getVertices();
        for (int i = 0; i < vertices.length; i+=STRIDE) {
            vertices[i + 9] =  normal;
            vertices[i + 10] = normal;
            vertices[i + 11] = normal;
        }
        meshComponent.setVertices(vertices);
    }
}
