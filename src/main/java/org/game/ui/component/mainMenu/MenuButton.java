package org.game.ui.component.mainMenu;

import org.game.isometric.blockLoader.BlocksReader;
import org.game.isometric.texture2D.TextureManager2D;
import org.game.ui.component.AbstractButton;

public class MenuButton extends AbstractButton {

    private static final String DEFAULT_TEXTURE_LABEL = "IRON_ORE";

    public MenuButton(float width, float height, int mouseButton) {
        super(width, height, mouseButton);
        Integer checkbox = TextureManager2D.getTextureIdByLabel("UI_CHECKBOX");
        setTextureID(checkbox);
    }

    public MenuButton(float width, float height, String textureLabel, int mouseButton) {
        super(width, height, mouseButton);
        setTextureID(BlocksReader.getTextureIdByItemLabel(textureLabel));
    }

    @Override
    public void onClick() {

    }
}
