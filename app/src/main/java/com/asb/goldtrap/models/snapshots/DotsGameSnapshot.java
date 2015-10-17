package com.asb.goldtrap.models.snapshots;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Holds the state of the game
 */
public class DotsGameSnapshot {
    private CellState cells[][];
    private LineState horizontalLines[][];
    private LineState verticalLines[][];
    private Set<Goodie> goodies;
    private Score score = new Score();

    private LineState lastClickedLineState = LineState.FREE;
    private LineType lastClickedLineType = LineType.NONE;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;

    private List<Cell> lastScoredCells;

    public DotsGameSnapshot(CellState[][] cells, LineState[][] horizontalLines,
                            LineState[][] verticalLines, Set<Goodie> goodies) {
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
            lastScoredCells = new ArrayList<>();
        }
        return lastScoredCells;
    }

    public void setLastScoredCells(List<Cell> lastScoredCells) {
        this.lastScoredCells = lastScoredCells;
    }

    public Set<Goodie> getGoodies() {
        return goodies;
    }

    public void setGoodies(Set<Goodie> goodies) {
        this.goodies = goodies;
    }

    public Score getScore() {
        computeScore();
        return score;
    }

    public void computeScore() {
        this.score.clearScore();
        CellState[][] cells = this.getCells();
        Set<Goodie> goodies = this.getGoodies();
        int rows = cells.length;
        int cols = cells[0].length;

        for (int i = 0; i < rows; i += 1) {
            int cellsOccupied = 0;
            for (int j = 0; j < cols; j += 1) {
                if (CellState.PLAYER == cells[i][j]) {
                    cellsOccupied += 1;
                    Cell cell = new Cell(cells[i][j], i, j);
                    score.getCells().add(cell);
                }
            }
            if (cellsOccupied == cols) {
                score.getLines().add(new Line(LineType.HORIZONTAL, i, -1));
            }
        }

        for (int i = 0; i < cols; i += 1) {
            int cellsOccupied = 0;
            for (int j = 0; j < rows; j += 1) {
                if (CellState.PLAYER == cells[j][i]) {
                    cellsOccupied += 1;
                }
            }
            if (cellsOccupied == rows) {
                score.getLines().add(new Line(LineType.VERTICAL, -1, i));
            }
        }

        for (Goodie goodie : goodies) {
            if (CellState.PLAYER == cells[goodie.getRow()][goodie.getCol()]) {
                if (GoodiesState.NOTHING != goodie.getGoodiesState()) {
                    score.getGoodies().add(goodie);
                }
            }
        }
    }
}
