package org.game.ui.newUiConcept;

public class VerticalLayout implements UiLayout {
    private final float padding;

    public VerticalLayout(float padding) {
        this.padding = padding;
    }

    @Override
    public void applyLayout(UiContainer container) {
        float currentY = container.getY() + container.getHeight() - padding; // Startujemy od górnej krawędzi kontenera
        if (currentY <= container.getY() - container.getHeight() + padding) {
            return;
        }
        for (UiComponent child : container.getChildren()) {
            child.setX(container.getX() + (container.getWidth() - child.getWidth()) / 2); // Wyśrodkowanie w poziomie
            //child.setX(container.getX() + padding);
            child.setY(currentY - child.getHeight());
            currentY -= child.getHeight() + padding; // Przesunięcie w dół
        }

        for (UiContainer internalContainer : container.getInternalContainers()) {
            if (!internalContainer.isFixedPosition()) {
                internalContainer.setX(container.getX() + (container.getWidth() - internalContainer.getWidth()) / 2); // Wyśrodkowanie w poziomie
                //internalContainer.setX(container.getX() + padding);
                internalContainer.setY(currentY - internalContainer.getHeight());
                internalContainer.applyLayout();
                currentY -= internalContainer.getHeight() + padding; // Przesunięcie w dół
            } else {
                internalContainer.setX(container.getX());
                internalContainer.setY(container.getY() + container.getHeight());
                internalContainer.applyLayout();
            }

        }
    }
}
