package com.asb.goldtrap.models.states.impl;

import android.util.Log;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.ArrayList;
import java.util.List;

public class Gamer {
    private static final String TAG = Gamer.class.getSimpleName();

    protected boolean allCellsFilled(CellState[][] cells) {
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

    protected List<Cell> getBoundedCellsForVertical(CellState[][] cells,
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
                Log.v(TAG, "Row: " + row + ", Col: " + col);
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
                Log.v(TAG, "Row: " + row + ", Col: " + (col - 1));
                boundedCells.add(cell);
            }
        }

    }

    protected List<Cell> getBoundedCellsForHorizontal(CellState[][] cells,
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
                Log.v(TAG, "Row: " + row + ", Col: " + col);
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
                Log.v(TAG, "Row: " + (row - 1) + ", Col: " + col);
                boundedCells.add(cell);
            }
        }
    }

    protected void flipBoard(DotsGameSnapshot dotsGameSnapshot) {
        CellState[][] cells = dotsGameSnapshot.getCells();
        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        for (int i = 0; i < cells.length; i += 1) {
            CellState cell[] = cells[i];
            for (int j = 0; j < cell.length; j += 1) {
                switch (cells[i][j]) {
                    case SECONDARY_PLAYER:
                        cells[i][j] = CellState.PLAYER;
                        break;
                    case FREE:
                        break;
                    case PLAYER:
                        cells[i][j] = CellState.SECONDARY_PLAYER;
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < horizontalLines.length; i += 1) {
            LineState horizontalLine[] = horizontalLines[i];
            for (int j = 0; j < horizontalLine.length; j += 1) {
                switch (horizontalLine[j]) {
                    case SECONDARY_PLAYER:
                        horizontalLine[j] = LineState.PLAYER;
                        break;
                    case FREE:
                        break;
                    case PLAYER:
                        horizontalLine[j] = LineState.SECONDARY_PLAYER;
                        break;
                    default:
                        break;

                }
            }
        }
        for (int i = 0; i < verticalLines.length; i += 1) {
            LineState verticalLine[] = verticalLines[i];
            for (int j = 0; j < verticalLine.length; j += 1) {
                switch (verticalLine[j]) {
                    case SECONDARY_PLAYER:
                        verticalLine[j] = LineState.PLAYER;
                        break;
                    case FREE:
                        break;
                    case PLAYER:
                        verticalLine[j] = LineState.SECONDARY_PLAYER;
                        break;
                    default:
                        break;

                }
            }
        }

        switch (dotsGameSnapshot.getLastClickedLineState()) {
            case SECONDARY_PLAYER:
                dotsGameSnapshot.setLastClickedLineState(LineState.PLAYER);
                break;
            case FREE:
                break;
            case PLAYER:
                dotsGameSnapshot.setLastClickedLineState(LineState.SECONDARY_PLAYER);
                break;
            default:
                break;
        }

        List<Cell> scoredCells = dotsGameSnapshot.getLastScoredCells();
        for (Cell cell : scoredCells) {
            switch (cell.getCellState()) {
                case SECONDARY_PLAYER:
                    cell.setCellState(CellState.PLAYER);
                    break;
                case FREE:
                    break;
                case PLAYER:
                    cell.setCellState(CellState.SECONDARY_PLAYER);
                    break;
                default:
                    break;
            }
        }
    }
}
