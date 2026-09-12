package org.game.ui.newUiConcept;

import org.game.isometric.texture2D.TextureManager2D;
import org.game.ui.component.RawUiModel;
import org.game.ui.component.UiMouseUtils;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

import static org.game.GraphicsDisplay.BASE_HEIGHT;
import static org.game.GraphicsDisplay.BASE_WIDTH;
import static org.game.GraphicsDisplay.WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class UiContainer extends UiComponent {
    private UiTopBar uiTopBar;
    private final List<UiComponent> children;
    private final List<UiContainer> internalContainers;
    private UiLayout layout;
    private final RawUiModel backgroundRawUiModel;
    private final int backgroundTextureId;
    private boolean backgroundVisible;
    private boolean cursorWithinBounds;
    private boolean dragContainerActive;
    private boolean fixedPosition;
    private boolean isVisible;

    private float depth = 1.0f; //test variable to remove


    public UiContainer(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.children = new ArrayList<>();
        this.internalContainers = new ArrayList<>();
        this.backgroundTextureId = TextureManager2D.getTextureIdByLabel("UI_CONTAINER");
        this.backgroundRawUiModel = new RawUiModel(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(x, y, depth), width, height);
        this.backgroundRawUiModel.setTextureID(backgroundTextureId);
        this.backgroundVisible = false;
        this.fixedPosition = false;
        this.isVisible = false;
    }

    public UiContainer(float x, float y, float width, float height, String backgroundTextureLabel) {
        super(x, y, width, height);
        this.children = new ArrayList<>();
        this.internalContainers = new ArrayList<>();
        this.backgroundTextureId = TextureManager2D.getTextureIdByLabel(backgroundTextureLabel);
        this.backgroundRawUiModel = new RawUiModel(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(x, y, depth), width, height);
        this.backgroundRawUiModel.setTextureID(backgroundTextureId);
        this.backgroundVisible = true;
        this.fixedPosition = false;
        this.isVisible = false;
    }

    @Override
    public List<RawUiModel> generateRawUiModels() {
        List<RawUiModel> result = new ArrayList<>();
        children.forEach(uiComponent -> {
            result.addAll(uiComponent.generateRawUiModels());
        });
        internalContainers.forEach(uiContainer -> {
            result.addAll(uiContainer.generateRawUiModels());
        });
        if (backgroundVisible) {
            result.add(backgroundRawUiModel);

        }
        return result;
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS && cursorWithinBounds) {
            dragContainerActive = true;
        }
        if (action == GLFW_RELEASE) {
            dragContainerActive = false;
            if (uiTopBar != null) {
                uiTopBar.setDragXOffset(0);
            }
        }
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        if (uiTopBar != null && !uiTopBar.isRigid()) {
            cursorWithinBounds = UiMouseUtils.isCursorWithinBounds(xpos, ypos, uiTopBar.getWidth(), uiTopBar.getHeight(), uiTopBar.getX(), uiTopBar.getY());
            float scale = (float) BASE_WIDTH / (float) WIDTH;
            double xPosScaled = (xpos * scale);
            if (!dragContainerActive) {
                uiTopBar.setDragXOffset(Math.abs(getX() - xPosScaled));
            }
            if (dragContainerActive) {
                double yPosScaled = (ypos * scale) + getHeight() + uiTopBar.getFixedPos().y + (0.5f * uiTopBar.getHeight());
                setX((float) (xPosScaled - uiTopBar.getDragXOffset()));
                setY((float) (BASE_HEIGHT - yPosScaled));
                applyLayout();
            }
        }
    }

    public void setLayout(UiLayout layout) {
        this.layout = layout;
        applyLayout();
    }

    public void addComponent(UiComponent component) {
        children.add(component);
        applyLayout();
    }

    public void addInternalContainer(UiContainer uiContainer) {
        internalContainers.add(uiContainer);
        applyLayout();
    }

    public void removeComponent(UiComponent component) {
        children.remove(component);
        applyLayout();
    }

    public List<UiComponent> getChildren() {
        return children;
    }

    public List<UiContainer> getInternalContainers() {
        return internalContainers;
    }

    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }

    public boolean isFixedPosition() {
        return fixedPosition;
    }

    public void setUiTopBar(UiTopBar uiTopBar) {
        this.uiTopBar = uiTopBar;
        addInternalContainer(uiTopBar);
    }

    public void setFixedPosition(boolean fixedPosition) {
        this.fixedPosition = fixedPosition;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void applyLayout() {
        if (layout != null) {
            layout.applyLayout(this);
            backgroundRawUiModel.setPosition(new Vector3f(getX(), getY(), depth));
        }
    }
}
