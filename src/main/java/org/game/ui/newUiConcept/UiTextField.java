package org.game.ui.newUiConcept;

import org.game.isometric.texture2D.TextureManager2D;
import org.game.ui.component.RawUiModel;
import org.game.ui.newUiConcept.font.UiFontUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class UiTextField extends UiComponent {

    private final RawUiModel rawUiModel;
    private final String label;
    private List<RawUiModel> rawUiModelText;
    private boolean borderVisible;

    public UiTextField(String label, boolean borderVisible, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.label = label;
        this.rawUiModel = new RawUiModel(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(x, y, 1.0f), width, height);
        Integer textField = TextureManager2D.getTextureIdByLabel("UI_TEXT_FIELD");
        this.rawUiModel.setTextureID(textField);
        this.rawUiModelText = new ArrayList<>(UiFontUtils.getTextModel(label, getX() + 10.0f, getY() + 0.75f * getHeight(), 0.8f * getWidth()));
        this.borderVisible = borderVisible;
    }

    @Override
    public List<RawUiModel> generateRawUiModels() {
        List<RawUiModel> resultList = new ArrayList<>(rawUiModelText);
        if (isBorderVisible()) {
            resultList.add(rawUiModel);
        }
        return resultList;
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        Vector3f currentPos = rawUiModel.getPosition();
        rawUiModel.setPosition(new Vector3f(x, currentPos.y, currentPos.z));
        Vector2f[] textPosition = UiFontUtils.getTextPosition(label, x + 10.0f, getY() + 0.75f * getHeight(), 0.8f * getWidth());
        for (int i = 0; i < rawUiModelText.size(); i++) {
            rawUiModelText.get(i).setPosition(new Vector3f(textPosition[i].x, textPosition[i].y, 1.0f));
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        Vector3f currentPos = rawUiModel.getPosition();
        rawUiModel.setPosition(new Vector3f(currentPos.x, y, currentPos.z));
        Vector2f[] textPosition = UiFontUtils.getTextPosition(label, getX() + 10.0f, y + 0.75f * getHeight(), 0.8f * getWidth());
        for (int i = 0; i < rawUiModelText.size(); i++) {
            rawUiModelText.get(i).setPosition(new Vector3f(textPosition[i].x, textPosition[i].y, 1.0f));
        }
    }

    public boolean isBorderVisible() {
        return borderVisible;
    }
}
