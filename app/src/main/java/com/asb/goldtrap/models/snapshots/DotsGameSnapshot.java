package com.asb.goldtrap.models.snapshots;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.Collections;
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
    private Set<DynamicGoodie> dynamicGoodies;
    private transient Score score;
    private List<Task> tasks;
    private LineState lastClickedLineState = LineState.FREE;
    private LineType lastClickedLineType = LineType.NONE;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;

    private List<Cell> lastScoredCells;

    public DotsGameSnapshot(CellState[][] cells, LineState[][] horizontalLines,
                            LineState[][] verticalLines, Set<Goodie> goodies,
                            Set<DynamicGoodie> dynamicGoodies, List<Task> tasks) {
        super();
        this.cells = cells;
        this.horizontalLines = horizontalLines;
        this.verticalLines = verticalLines;
        this.goodies = goodies;
        this.dynamicGoodies = dynamicGoodies;
        this.tasks = tasks;
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

    public Set<DynamicGoodie> getDynamicGoodies() {
        return dynamicGoodies;
    }

    public void setDynamicGoodies(
            Set<DynamicGoodie> dynamicGoodies) {
        this.dynamicGoodies = dynamicGoodies;
    }

    public Score getScore() {
        if (null == score) {
            score = new Score();
        }
        return score;
    }

    public List<Task> getTasks() {
        if (null == tasks) {
            tasks = Collections.emptyList();
        }
        return tasks;
    }
}
