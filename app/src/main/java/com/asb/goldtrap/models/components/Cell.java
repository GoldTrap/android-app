package com.asb.goldtrap.models.components;

import com.asb.goldtrap.models.states.enums.CellState;

public class Cell {
    CellState cellState;
    private int lastScoredCellRow = -1;
    private int lastScoreCellCol = -1;

    public Cell(CellState cellState, int lastScoredCellRow, int lastScoreCellCol) {
        super();
        this.cellState = cellState;
        this.lastScoredCellRow = lastScoredCellRow;
        this.lastScoreCellCol = lastScoreCellCol;
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public int getLastScoredCellRow() {
        return lastScoredCellRow;
    }

    public void setLastScoredCellRow(int lastScoredCellRow) {
        this.lastScoredCellRow = lastScoredCellRow;
    }

    public int getLastScoreCellCol() {
        return lastScoreCellCol;
    }

    public void setLastScoreCellCol(int lastScoreCellCol) {
        this.lastScoreCellCol = lastScoreCellCol;
    }

}