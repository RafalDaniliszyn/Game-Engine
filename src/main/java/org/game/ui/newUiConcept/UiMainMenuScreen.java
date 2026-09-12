package org.game.ui.newUiConcept;

import org.game.GraphicsDisplay;
import org.game.isometric.WorldSettings;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class UiMainMenuScreen extends UiScreen {

    private final List<UiContainer> containerList;

    public UiMainMenuScreen(float width, float height) {
        this.containerList = new ArrayList<>();
        Vector2f windowSize = GraphicsDisplay.getWindowSize();
        setWidth(width);
        setHeight(height);
        float x = (0.5f * windowSize.x()) - (0.5f * width);
        float y = (0.5f * windowSize.y()) - (0.5f * height);

        UiContainer uiMainMenuContainer = new UiContainer(x, y, width, height);
        uiMainMenuContainer.setBackgroundVisible(true);
        uiMainMenuContainer.setLayout(new VerticalLayout(15.0f));

        UiTopBar uiTopBar = new UiTopBar("Main menu", 0, 3.0f, width, 32);
        uiTopBar.setRigid(true);
        uiMainMenuContainer.setUiTopBar(uiTopBar);
        uiMainMenuContainer.setUiKeyCallback((window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                uiMainMenuContainer.setVisible(!uiMainMenuContainer.isVisible());
            }
        });

        UiCheckboxContainer uiCheckboxContainer =
                new UiCheckboxContainer(0, 0, 128, 36,
                        new UiCheckbox(0, 0, 128, 24),
                        new HorizontalLayout(-128.0f),
                        new UiTextField("QUIT GAME", true, 100, 0, 128, 32));
        uiCheckboxContainer.getUiCheckbox().setOnSelect(() -> {
            if (uiMainMenuContainer.isVisible()) {
                glfwSetWindowShouldClose(GraphicsDisplay.getDisplayId(), true);
            }
        });
        uiMainMenuContainer.addInternalContainer(uiCheckboxContainer);

        //FULLSCREEN
        UiCheckboxContainer uiCheckboxContainer1 =
                new UiCheckboxContainer(0, 0, 128, 36,
                        new UiCheckbox(0, 0, 24, 24),
                        new HorizontalLayout(1.0f),
                        new UiTextField("fullscreen", true, 100, 0, 128, 32));
        uiCheckboxContainer1.getUiCheckbox().setOnSelect(() -> {
            if (uiMainMenuContainer.isVisible()) {
                GraphicsDisplay.setMode(true);
            }
        });
        uiCheckboxContainer1.getUiCheckbox().setOnUnSelect(() -> {
            if (uiMainMenuContainer.isVisible()) {
                GraphicsDisplay.setMode(false);
            }
        });
        uiMainMenuContainer.addInternalContainer(uiCheckboxContainer1);


        uiMainMenuContainer.addComponent(new UiTextField("123456789", true, 100, 0, 128, 32));

        UiItemContainer uiItemContainer = new UiItemContainer(0, 0, 100, 100, new HorizontalLayout(10.0f));
        //uiMainMenuContainer.addInternalContainer(uiItemContainer);

        this.containerList.add(uiMainMenuContainer);
    }

    @Override
    public List<UiContainer> getContainers() {
        return this.containerList;
    }
}
