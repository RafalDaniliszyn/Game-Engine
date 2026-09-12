package org.game.ui.newUiConcept;

import org.game.isometric.texture2D.TextureManager2D;
import org.game.ui.component.RawUiModel;
import org.game.ui.component.UiMouseUtils;
import org.joml.Vector3f;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class UiCheckbox extends UiComponent {

    private final RawUiModel rawUiModel;
    private boolean selected;
    private boolean cursorWithinBounds;
    private final int checkboxTextureId;
    private final int checkboxSelectedTextureId;
    private Runnable onSelect;
    private Runnable onUnSelect;

    public UiCheckbox(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.rawUiModel = new RawUiModel(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(x, y, 1.0f), width, height);
        this.checkboxTextureId = TextureManager2D.getTextureIdByLabel("UI_CHECKBOX");
        this.checkboxSelectedTextureId = TextureManager2D.getTextureIdByLabel("UI_CHECKBOX_SELECTED");
        this.rawUiModel.setTextureID(checkboxTextureId);
    }

    @Override
    public List<RawUiModel> generateRawUiModels() {
        return List.of(rawUiModel);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        Vector3f currentPos = rawUiModel.getPosition();
        rawUiModel.setPosition(new Vector3f(x, currentPos.y, currentPos.z));

    }

    @Override
    public void setY(float y) {
        super.setY(y);
        Vector3f currentPos = rawUiModel.getPosition();
        rawUiModel.setPosition(new Vector3f(currentPos.x, y, currentPos.z));
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {
        if (cursorWithinBounds && button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
            setSelected(!selected);
            rawUiModel.setTextureID(selected ? checkboxSelectedTextureId : checkboxTextureId);
        }
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        cursorWithinBounds = UiMouseUtils.isCursorWithinBounds(xpos, ypos, getWidth(), getHeight(), getX(), getY());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (onSelect != null && selected) {
            onSelect.run();
        }
        if (onUnSelect != null && !selected) {
            onUnSelect.run();
        }
    }

    public void setOnSelect(Runnable onSelect) {
        this.onSelect = onSelect;
    }

    public void setOnUnSelect(Runnable onUnSelect) {
        this.onUnSelect = onUnSelect;
    }
}
