package org.game.ui.component;

import org.joml.Vector2i;

import static org.game.GraphicsDisplay.BASE_WIDTH;
import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;

public class UiMouseUtils {
    public static boolean isCursorWithinBounds(double cursorX, double cursorY, float width, float height, float x, float y) {
        float scale = (float) WIDTH / (float) BASE_WIDTH;
        float buttonWidth = width * scale;
        float buttonHeight = height * scale;
        int xScaled = (int) (x * scale);
        int yScaled = (int) (y * scale);
        return cursorX >= xScaled && cursorX <= xScaled + buttonWidth && HEIGHT - cursorY >= yScaled && HEIGHT - cursorY <= yScaled + buttonHeight;
    }

    public static Vector2i getRelativeMousePos(double xpos, double ypos) {
        float scale = (float) WIDTH / (float) BASE_WIDTH;
        return new Vector2i(
                (int) (xpos * scale),
                (int) (HEIGHT - ypos * scale)
        );
    }
}
