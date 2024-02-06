package org.game;

import org.game.component.MeshComponent;

public class MeshComponentMapper {
    public static void map(MeshData meshData, MeshComponent meshComponent) {
        meshComponent.setVertices(meshData.getVertices());
        meshComponent.setIndices(meshData.getIndices());
        meshComponent.setTextureID(meshData.getTextureID());
    }
}
