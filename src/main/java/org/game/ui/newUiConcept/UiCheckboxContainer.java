package org.game.ui.newUiConcept;

public class UiCheckboxContainer extends UiContainer {

    private UiCheckbox uiCheckbox;

    public UiCheckboxContainer(float x, float y, float width, float height, UiCheckbox uiCheckbox, UiLayout uiLayout, UiComponent... children) {
        super(x, y, width, height);
        this.uiCheckbox = uiCheckbox;
        setUiCheckbox(uiCheckbox);
        for (UiComponent child : children) {
            addComponent(child);
        }
        addComponent(uiCheckbox);

        setLayout(uiLayout);
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {

    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {

    }

    public UiCheckbox getUiCheckbox() {
        return uiCheckbox;
    }

    public void setUiCheckbox(UiCheckbox uiCheckbox) {
        this.uiCheckbox = uiCheckbox;
    }
}
