package com.asb.goldtrap.models.components;

import com.asb.goldtrap.models.states.enums.CellState;

public class Cell {
    CellState cellState;
    private int row = -1;
    private int col = -1;

    public Cell(CellState cellState, int row, int col) {
        super();
        this.cellState = cellState;
        this.row = row;
        this.col = col;
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public int hashCode() {
        return this.cellState.hashCode() + row + col;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof Cell) {
            Cell cell = (Cell) o;
            if (cell.col == this.col && cell.row == this.row && cell.cellState == this.cellState) {
                equals = true;
            }

        }
        return equals;
    }
}