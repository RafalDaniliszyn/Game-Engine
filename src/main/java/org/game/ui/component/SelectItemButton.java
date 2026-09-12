package org.game.ui.component;

import org.joml.Vector3f;

public class SelectItemButton extends AbstractButton {
    private final String itemLabel;

    public SelectItemButton(float x, float y, float width, float height, String itemLabel, int textureId, int mouseButton) {
        super(x, y, width, height, textureId, mouseButton);
        this.itemLabel = itemLabel;
    }

    @Override
    public void onClick() {
        SelectedItem.label = itemLabel;
        SelectedItem.id = getTextureID();
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        super.cursorPosCallbackI(window, xpos, ypos);
        if (isCursorEntered()) {
            setScale(new Vector3f(1.15f, 1.15f, 0.0f));
        } else {
            setScale(new Vector3f(1.0f, 1.0f, 0.0f));
        }
    }
}
