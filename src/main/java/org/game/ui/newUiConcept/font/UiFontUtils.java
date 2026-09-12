package org.game.ui.newUiConcept.font;

import org.game.isometric.texture2D.TextureManager2D;
import org.game.ui.component.RawUiModel;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class UiFontUtils {

    private static final Font defaultFont;

    static {
        defaultFont = new Font("/textures/2D/ui/defaultFont19Data.txt");
    }

    public static List<RawUiModel> getTextModel(String text, float x, float y, float maxWidth) {
        Integer textureId = TextureManager2D.getTextureIdByLabel("DEFAULT_FONT");
        int imageW = 512;
        int imageH = 50;

        List<RawUiModel> textModelList = new ArrayList<>();
        float cursorX = x;
        float cursorY = y;
        int[] ints = text.chars().toArray();
        for (int i = 0; i < ints.length; i++) {
            if (cursorX >= x + maxWidth) {
                break;
            }
            Letter letter = defaultFont.getLetter(ints[i]);
            Vector2f uv1 = new Vector2f(letter.x()/imageW, (letter.y() + letter.height())/imageH);
            Vector2f uv2 = new Vector2f(letter.x()/imageW, letter.y()/imageH);
            Vector2f uv3 = new Vector2f((letter.x() + letter.width())/imageW, (letter.y() + letter.height())/imageH);
            Vector2f uv4 = new Vector2f((letter.x() + letter.width())/imageW, letter.y()/imageH);

            Vector2f charPos = new Vector2f(cursorX + letter.xOffset(), cursorY - letter.yOffset());
            RawUiModel charModel = new RawUiModel(new Vector3f(charPos.x(), charPos.y(), 1.0f), letter.width(), -letter.height(), uv1, uv2, uv3, uv4);
            charModel.setTextureID(textureId);
            textModelList.add(charModel);
            cursorX += letter.xAdvance();
        }

        return textModelList;
    }

    public static Vector2f[] getTextPosition(String text, float x, float y, float maxWidth) {
        Vector2f[] position = new Vector2f[text.length()];
        float cursorX = x;
        float cursorY = y;
        int[] ints = text.chars().toArray();
        for (int i = 0; i < ints.length; i++) {
            if (cursorX >= x + maxWidth) {
                break;
            }
            Letter letter = defaultFont.getLetter(ints[i]);
            Vector2f charPos = new Vector2f(cursorX + letter.xOffset(), cursorY - letter.yOffset());
            position[i] = charPos;
            cursorX += letter.xAdvance();
        }

        return position;
    }


}
