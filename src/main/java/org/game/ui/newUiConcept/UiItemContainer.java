package org.game.ui.newUiConcept;

public class UiItemContainer extends UiContainer {

    public UiItemContainer(float x, float y, float width, float height, UiLayout uiLayout) {
        super(x, y, width, height);

        UiTextField item = new UiTextField("item", true, 0, 0, 128, 32);
        addComponent(item);

        setLayout(uiLayout);
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {
        super.mouseButtonCallbackI(window, button, action, mods);
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        super.cursorPosCallbackI(window, xpos, ypos);
    }
}
