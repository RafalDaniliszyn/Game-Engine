package org.game.ui.newUiConcept;

import org.joml.Vector2f;

public class UiTopBar extends UiContainer {
    private final String label;
    private final UiTextField uiTextField;
    private final Vector2f fixedPos;
    private double dragXOffset;
    private boolean rigid;

    public UiTopBar(String label, float x, float y, float width, float height) {
        super(x, y, width, height, "UI_TOP_BAR");
        this.label = label;
        this.uiTextField = new UiTextField(label, false, x, y, width, height);
        setLayout(new HorizontalLayout(10.0f));
        addComponent(uiTextField);
        setFixedPosition(true);
        this.fixedPos = new Vector2f(x, y);
    }

    @Override
    public void setX(float x) {
        if (isFixedPosition()) {
            super.setX(x + fixedPos.x);
        } else {
            super.setX(x);
        }
        applyLayout();
    }

    @Override
    public void setY(float y) {
        if (isFixedPosition()) {
            super.setY(y + fixedPos.y);
        } else {
            super.setY(y);
        }
        applyLayout();
    }

    public String getLabel() {
        return label;
    }

    public Vector2f getFixedPos() {
        return fixedPos;
    }

    public double getDragXOffset() {
        return dragXOffset;
    }

    public void setDragXOffset(double dragXOffset) {
        this.dragXOffset = dragXOffset;
    }

    public boolean isRigid() {
        return rigid;
    }

    public void setRigid(boolean rigid) {
        this.rigid = rigid;
    }
}
