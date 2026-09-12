package org.game.ui.component;

import org.joml.Vector2f;

public interface Layout {
    void align(Container container);
    Vector2f getSize();
    int getCellSize();
    int getMargin();
}
