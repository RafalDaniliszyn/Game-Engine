package org.game.ui.component.mainMenu;

import org.game.ui.component.AbstractUiElement;
import org.game.ui.component.Layout;
import org.game.ui.component.Container;
import java.util.List;

public class MenuContainer extends Container {

    public MenuContainer(List<? extends AbstractUiElement> elements, Layout layout) {
        super(elements, layout);
    }

    public MenuContainer(List<? extends AbstractUiElement> elements, int x, int y, Layout layout) {
        super(elements, x, y, layout);
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {

    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {

    }

    @Override
    public void windowSizeCallbackI(long window, int width, int height) {

    }
}
