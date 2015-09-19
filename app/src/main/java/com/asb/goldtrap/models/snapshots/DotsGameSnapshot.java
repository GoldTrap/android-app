package com.asb.goldtrap.models.snapshots;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the state of the game
 */
public class DotsGameSnapshot {
    private CellState cells[][];
    private LineState horizontalLines[][];
    private LineState verticalLines[][];
    private GoodiesState goodies[][];

    private LineState lastClickedLineState = LineState.FREE;
    private LineType lastClickedLineType = LineType.NONE;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;

    private List<Cell> lastScoredCells;

    public DotsGameSnapshot(CellState[][] cells, LineState[][] horizontalLines,
                            LineState[][] verticalLines, GoodiesState goodies[][]) {
        super();
        this.cells = cells;
        this.horizontalLines = horizontalLines;
        this.verticalLines = verticalLines;
        this.goodies = goodies;
    }

    public LineState getLastClickedLineState() {
        return lastClickedLineState;
    }

    public void setLastClickedLineState(LineState lastClickedLineState) {
        this.lastClickedLineState = lastClickedLineState;
    }

    public CellState[][] getCells() {
        return cells;
    }

    public void setCells(CellState[][] cells) {
        this.cells = cells;
    }

    public LineState[][] getHorizontalLines() {
        return horizontalLines;
    }

    public void setHorizontalLines(LineState[][] horizontalLines) {
        this.horizontalLines = horizontalLines;
    }

    public LineState[][] getVerticalLines() {
        return verticalLines;
    }

    public void setVerticalLines(LineState[][] verticalLines) {
        this.verticalLines = verticalLines;
    }

    public LineType getLastClickedLineType() {
        return lastClickedLineType;
    }

    public void setLastClickedLineType(LineType lastClickedLineType) {
        this.lastClickedLineType = lastClickedLineType;
    }

    public int getLastClickedRow() {
        return lastClickedRow;
    }

    public void setLastClickedRow(int lastClickedRow) {
        this.lastClickedRow = lastClickedRow;
    }

    public int getLastClickedCol() {
        return lastClickedCol;
    }

    public void setLastClickedCol(int lastClickedCol) {
        this.lastClickedCol = lastClickedCol;
    }

    public List<Cell> getLastScoredCells() {
        if (null == lastScoredCells) {
            lastScoredCells = new ArrayList<Cell>();
        }
        return lastScoredCells;
    }

    public void setLastScoredCells(List<Cell> lastScoredCells) {
        this.lastScoredCells = lastScoredCells;
    }

    public GoodiesState[][] getGoodies() {
        return goodies;
    }

    public void setGoodies(GoodiesState[][] goodies) {
        this.goodies = goodies;
    }

}
