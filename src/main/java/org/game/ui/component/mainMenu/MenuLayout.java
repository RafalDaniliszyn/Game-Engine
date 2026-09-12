package org.game.ui.component.mainMenu;

import org.game.ui.component.AbstractUiElement;
import org.game.ui.component.Layout;
import org.game.ui.component.Container;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.game.GraphicsDisplay.HEIGHT;
import static org.game.GraphicsDisplay.WIDTH;

public class MenuLayout implements Layout {

    private final float sideMargin = 10.0f;
    private final float topMargin = 50.0f;
    private Vector2f size;

    public MenuLayout() {
        this.size = new Vector2f();
    }

    @Override
    public void align(Container container) {
        // TODO: 12/1/2024 set width and height in container first.
        float containerWidth = container.getWidth();
        float containerHeight = container.getHeight();
        int x = (int) ((0.5f * WIDTH) - (0.5f * containerWidth));
        int y = (int) ((0.5f * HEIGHT) + (containerHeight));
        container.setX(x);
        container.setY(y);

        List<? extends AbstractUiElement> elements = container.getElements();
        float currentRowY = 5.0f;
        for (int i = 0; i < elements.size(); i++) {
            AbstractUiElement element = elements.get(i);
            float width = element.getWidth();
            float height = element.getHeight();
            float elementX = x;
            float elementY = currentRowY + topMargin + height;
            currentRowY = elementY;
            element.setPosition(new Vector3f(elementX, elementY, 1.0f));
            size.y = size.y + elementY;
            size.x = width;
        }
    }

    @Override
    public Vector2f getSize() {
        return size;
    }

    @Override
    public int getCellSize() {
        return 0;
    }

    @Override
    public int getMargin() {
        return (int) sideMargin;
    }
}
