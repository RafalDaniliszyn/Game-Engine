package org.game.ui.component;

import org.joml.Vector2f;
import java.util.List;

public class TileLayout implements Layout {
    private int rows;
    private int columns;
    private int margin;
    private int cellSize;

    private final RawCell[][] cells;

    public TileLayout(int columns, int rows, int margin) {
        this.rows = rows;
        this.columns = columns;
        this.margin = margin;
        this.cells = new RawCell[columns][rows];
    }

    @Override
    public void align(Container container) {
        List<? extends AbstractUiElement> elements = container.getElements();
        int size = elements.size();
        cellSize = countCellSize(elements);
        int containerX = container.getX();
        int containerY = container.getY();
        int index = 0;
        for (int column = 0; column < cells.length; column++) {
            for (int row = 0; row < cells[0].length; row++) {
                boolean isEmpty;
                if (index < size) {
                    AbstractUiElement uiElement = elements.get(index);
                    uiElement.setX(containerX + (column * (cellSize + margin)));
                    uiElement.setY(containerY - ((row + 1) * (cellSize + margin)));
                    index += 1;
                    isEmpty = false;
                } else {
                    isEmpty = true;
                }
                if (cells[column][row] == null) {
                    cells[column][row] = new RawCell(containerX + (column * (cellSize + margin)), containerY - ((row + 1) * (cellSize + margin)), isEmpty);
                } else {
                    cells[column][row].setX(containerX + (column * (cellSize + margin)));
                    cells[column][row].setY(containerY - ((row + 1) * (cellSize + margin)));
                }

            }
        }
    }

    @Override
    public Vector2f getSize() {
        return new Vector2f(columns * (cellSize + margin), rows * (cellSize + margin));
    }

    @Override
    public int getCellSize() {
        return cellSize;
    }

    @Override
    public int getMargin() {
        return margin;
    }

    public RawCell getNextEmptyCell() {
        for (int column = 0; column < cells.length; column++) {
            for (int row = 0; row < cells[0].length; row++) {
                if (cells[column][row].isEmpty()) {
                    cells[column][row].setEmpty(false);
                    return cells[column][row];
                }
            }
        }
        return null;
    }

    public RawCell[][] getCells() {
        return cells;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    private int countCellSize(List<? extends AbstractUiElement> uiElements) {
        int resultSize = 0;
        for (AbstractUiElement uiElement : uiElements) {
            double max = Math.max(uiElement.getWidth(), uiElement.getHeight());
            if (max > resultSize) {
                resultSize = (int) max;
            }
        }
        return resultSize;
    }

}
