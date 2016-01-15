package com.asb.goldtrap.models.conductor.helper.impl;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.conductor.helper.Flipper;
import com.asb.goldtrap.models.conductor.helper.Gamer;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.ArrayList;
import java.util.List;

public class GamerImpl implements Gamer {
    private static final String TAG = GamerImpl.class.getSimpleName();
    private Flipper flipper;

    public GamerImpl() {
        flipper = new FlipperImpl();
    }

    @Override
    public boolean allCellsFilled(CellState[][] cells) {
        boolean allFilled = true;
        for (CellState[] cellRows : cells) {
            for (CellState cell : cellRows) {
                if (cell == CellState.FREE) {
                    return false;
                }
            }
        }
        return allFilled;
    }

    @Override
    public List<Cell> getBoundedCellsForVertical(CellState[][] cells,
                                                 LineState[][] horizontalLines,
                                                 LineState[][] verticalLines,
                                                 CellState cellState, int row, int col) {
        int cols = horizontalLines[0].length + 1;
        List<Cell> boundedCells = new ArrayList<>();
        if (0 == col) {
            checkIfBoundedForVertical(cells, horizontalLines, verticalLines,
                    cellState, row, col, true, boundedCells);
        }
        else if (cols - 1 == col) {
            checkIfBoundedForVertical(cells, horizontalLines, verticalLines,
                    cellState, row, col, false, boundedCells);
        }
        else {
            checkIfBoundedForVertical(cells, horizontalLines, verticalLines,
                    cellState, row, col, false, boundedCells);
            checkIfBoundedForVertical(cells, horizontalLines, verticalLines,
                    cellState, row, col, true, boundedCells);
        }
        return boundedCells;
    }

    private void checkIfBoundedForVertical(CellState[][] cells,
                                           LineState[][] horizontalLines,
                                           LineState[][] verticalLines,
                                           CellState cellState, int row, int col, boolean isRight,
                                           List<Cell> boundedCells) {

        if (isRight) {
            if (horizontalLines[row][col] != LineState.FREE
                    && horizontalLines[row + 1][col] != LineState.FREE
                    && verticalLines[row][col] != LineState.FREE
                    && verticalLines[row][col + 1] != LineState.FREE) {
                cells[row][col] = cellState;
                Cell cell = new Cell(cellState, row, col);
                boundedCells.add(cell);
            }
        }
        else {
            if (horizontalLines[row][col - 1] != LineState.FREE
                    && horizontalLines[row + 1][col - 1] != LineState.FREE
                    && verticalLines[row][col] != LineState.FREE
                    && verticalLines[row][col - 1] != LineState.FREE) {
                cells[row][col - 1] = cellState;
                Cell cell = new Cell(cellState, row, col - 1);
                boundedCells.add(cell);
            }
        }

    }

    @Override
    public List<Cell> getBoundedCellsForHorizontal(CellState[][] cells,
                                                   LineState[][] horizontalLines,
                                                   LineState[][] verticalLines,
                                                   CellState cellState, int row, int col) {
        int rows = horizontalLines.length;
        List<Cell> boundedCells = new ArrayList<>();
        if (0 == row) {
            checkIfBoundedForHorizontal(cells, horizontalLines, verticalLines,
                    cellState, row, col, true, boundedCells);
        }
        else if (rows - 1 == row) {
            checkIfBoundedForHorizontal(cells, horizontalLines, verticalLines,
                    cellState, row, col, false, boundedCells);
        }
        else {
            checkIfBoundedForHorizontal(cells, horizontalLines, verticalLines,
                    cellState, row, col, false, boundedCells);
            checkIfBoundedForHorizontal(cells, horizontalLines, verticalLines,
                    cellState, row, col, true, boundedCells);
        }
        return boundedCells;
    }

    private void checkIfBoundedForHorizontal(CellState[][] cells,
                                             LineState[][] horizontalLines,
                                             LineState[][] verticalLines,
                                             CellState cellState, int row, int col, boolean isDown,
                                             List<Cell> boundedCells) {
        if (isDown) {
            if (horizontalLines[row][col] != LineState.FREE
                    && horizontalLines[row + 1][col] != LineState.FREE
                    && verticalLines[row][col] != LineState.FREE
                    && verticalLines[row][col + 1] != LineState.FREE) {
                cells[row][col] = cellState;
                Cell cell = new Cell(cellState, row, col);
                boundedCells.add(cell);
            }
        }
        else {
            if (horizontalLines[row][col] != LineState.FREE
                    && horizontalLines[row - 1][col] != LineState.FREE
                    && verticalLines[row - 1][col] != LineState.FREE
                    && verticalLines[row - 1][col + 1] != LineState.FREE) {
                cells[row - 1][col] = cellState;
                Cell cell = new Cell(cellState, row - 1, col);
                boundedCells.add(cell);
            }
        }
    }

    @Override
    public void flipBoard(DotsGameSnapshot dotsGameSnapshot) {
        flipper.flipBoard(dotsGameSnapshot);
    }
}
