package org.game.helper;

import org.game.component.mesh.MeshData;
import org.joml.Vector3f;

public class MeshHelper {
    public static void setLightColors(MeshData meshData, float rotationY) {
        int[] indices = meshData.getIndices();
        float[] vertices = meshData.getVertices();
        for (int i = 0; i < indices.length-3; i+=3) {
            int index1 = indices[i];
            int index2 = indices[i+1];
            int index3 = indices[i+2];
            float x1 = vertices[ index1 * 9];
            float y1 = vertices[(index1 * 9) + 1];
            float z1 = vertices[(index1 * 9) + 2];
            Vector3f vec1 = new Vector3f(x1, y1, z1);
            float x2 = vertices[ index2 * 9];
            float y2 = vertices[(index2 * 9) + 1];
            float z2 = vertices[(index2 * 9) + 2];
            Vector3f vec2 = new Vector3f(x2, y2, z2);
            float x3 = vertices[ index3 * 9];
            float y3 = vertices[(index3 * 9) + 1];
            float z3 = vertices[(index3 * 9) + 2];
            Vector3f vec3 = new Vector3f(x3, y3, z3);

            vec1.rotateY(rotationY);
            vec2.rotateY(rotationY);
            vec3.rotateY(rotationY);

            Vector3f u = new Vector3f(vec2.sub(vec1));
            Vector3f v = new Vector3f(vec3.sub(vec1));
            u.cross(v);
            u.normalize();
            Vector3f light = new Vector3f(10.0f, 100.0f, 0.0f);
            light.normalize();
            float dot = u.dot(light);
            float color = 0.8f;
            color += dot * 0.5f;

            vertices[(index1*9) + 3] = color;
            vertices[(index1*9) + 4] = color;
            vertices[(index1*9) + 5] = color;
        }
        meshData.setVertices(vertices);
    }
}
