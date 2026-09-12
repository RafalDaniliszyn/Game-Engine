package org.game.ui.component;

import org.game.WindowCallbackProcessor;
import org.game.isometric.texture2D.TextureManager2D;
import org.joml.Vector2i;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

import static org.game.GraphicsDisplay.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class ItemUiContainer extends Container {
    public static final String UI_CELL_BACKGROUND = "UI_CELL_BACKGROUND";
    private boolean cursorWithinBounds;
    private boolean dragContainerActive;
    private final List<RawUiModel> cells = new ArrayList<>();
    public ItemUiContainer(List<? extends AbstractUiElement> elements, int x, int y, int columns, int rows, int margin) {
        super(elements, x, y, new TileLayout(columns, rows, margin));
        WindowCallbackProcessor.getInstance().addMouseCallback(this);
        setCellBackground();
    }

    private void setCellBackground() {
        for (AbstractUiElement element : getElements()) {
            int margin = getLayout().getMargin();
            float cellSize = getLayout().getCellSize() + margin;
            float posX = element.getX() - 0.5f * margin;
            float posY = element.getY() - 0.5f * margin;
            RawUiModel cellRawModel = new RawUiModel(
                    new Vector3f(1.0f, 1.0f, 0.0f),
                    new Vector3f(posX, posY, 1.0f), cellSize, cellSize);
            cellRawModel.setTextureID(TextureManager2D.getTextureIdByLabel(UI_CELL_BACKGROUND));
            cells.add(cellRawModel);
        }

        TileLayout layout = (TileLayout) getLayout();
        int emptyCells = Math.max((layout.getRows() * layout.getColumns()) - cells.size(), 0);
        for (int i = 0; i < emptyCells; i++) {
            RawCell nextEmptyCell = layout.getNextEmptyCell();
            if (nextEmptyCell != null) {
                int margin = layout.getMargin();
                float cellSize = layout.getCellSize() + margin;
                float posX = nextEmptyCell.getX() - 0.5f * margin;
                float posY = nextEmptyCell.getY() - 0.5f * margin;
                RawUiModel cellRawModel = new RawUiModel(
                        new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector3f(posX, posY, 1.0f), cellSize, cellSize);
                cellRawModel.setTextureID(TextureManager2D.getTextureIdByLabel(UI_CELL_BACKGROUND));
                cells.add(cellRawModel);
            }
        }
        addChildrenUiModel(cells);
    }

    private void setCellPosition() {
        RawCell[][] cellTable = ((TileLayout) getLayout()).getCells();
        int index = 0;
        for (int column = 0; column < cellTable.length; column++) {
            for (int row = 0; row < cellTable[0].length; row++) {
                RawCell rawCell = cellTable[column][row];
                int margin = getLayout().getMargin();
                float posX = rawCell.getX() - 0.5f * margin;
                float posY = rawCell.getY() - 0.5f * margin;
                if (index < cells.size()) {
                    cells.get(index).setPosition(new Vector3f(posX, posY, 1.0f));
                    index += 1;
                } else {
                    return;
                }
            }
        }
    }

    @Override
    public void mouseButtonCallbackI(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS && cursorWithinBounds) {
            dragContainerActive = true;
        }
        if (action == GLFW_RELEASE) {
            dragContainerActive = false;
        }
    }

    @Override
    public void cursorPosCallbackI(long window, double xpos, double ypos) {
        Vector2i bgPos = getRelativeBackgroundPos();
        cursorWithinBounds = UiMouseUtils.isCursorWithinBounds(xpos, ypos, getWidth(), getHeight(), bgPos.x, bgPos.y);
        if (dragContainerActive) {
            float scale = (float) BASE_WIDTH / (float) WIDTH;
            double xPosScaled = (xpos * scale) - (0.5f * getWidth());
            double yPosScaled = (ypos * scale) - (0.5f * getHeight());
            setPosition((int) xPosScaled, (int) (BASE_HEIGHT - yPosScaled));
            setCellPosition();
        }
    }

    @Override
    public void windowSizeCallbackI(long window, int width, int height) {

    }
}
