package org.game.ui.newUiConcept;

public class HorizontalLayout implements UiLayout {
    private float padding; // Odstęp między komponentami

    public HorizontalLayout(float padding) {
        this.padding = padding;
    }

    @Override
    public void applyLayout(UiContainer uiContainer) {
        float currentX = uiContainer.getX(); // Startujemy od lewej krawędzi kontenera
        for (UiComponent child : uiContainer.getChildren()) {
            child.setX(currentX);
            child.setY(uiContainer.getY() + (uiContainer.getHeight() - child.getHeight()) / 2); // Wyśrodkowanie w pionie
            currentX += child.getWidth() + padding; // Przesunięcie na prawo
        }
    }
}
