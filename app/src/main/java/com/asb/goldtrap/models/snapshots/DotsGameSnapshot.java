package com.asb.goldtrap.models.snapshots;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.ScoreComputer;
import com.asb.goldtrap.models.results.computers.impl.CellScoreComputer;
import com.asb.goldtrap.models.results.computers.impl.DynamicGoodieScoreComputer;
import com.asb.goldtrap.models.results.computers.impl.GoodieScoreComputer;
import com.asb.goldtrap.models.results.computers.impl.HorizontalLinesScoreComputer;
import com.asb.goldtrap.models.results.computers.impl.ResultComputer;
import com.asb.goldtrap.models.results.computers.impl.VerticalLinesScoreComputer;
import com.asb.goldtrap.models.results.examiners.TaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.GoodiesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.HorizontalLinesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.LinesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.VerticalLinesTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.goodies.DynamicGoodieTaskCompletionExaminer;
import com.asb.goldtrap.models.results.examiners.impl.goodies.GoodieTaskCompletionExaminer;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Score score = new Score();

    private LineState lastClickedLineState = LineState.FREE;
    private LineType lastClickedLineType = LineType.NONE;
    private int lastClickedRow = -1;
    private int lastClickedCol = -1;

    private List<Cell> lastScoredCells;
    private TaskCompletionExaminer linesTaskCompletionExaminer =
            new LinesTaskCompletionExaminer();
    private TaskCompletionExaminer horizontalLinesTaskCompletionExaminer =
            new HorizontalLinesTaskCompletionExaminer();
    private TaskCompletionExaminer verticalLinesTaskCompletionExaminer =
            new VerticalLinesTaskCompletionExaminer();
    private TaskCompletionExaminer goodiesTaskCompletionExaminer =
            new GoodiesTaskCompletionExaminer();
    private TaskCompletionExaminer goodieTaskCompletionExaminer =
            new GoodieTaskCompletionExaminer();
    private TaskCompletionExaminer dynamicGoodieTaskCompletionExaminer =
            new DynamicGoodieTaskCompletionExaminer();

    private List<ScoreComputer> scoreComputers;
    private ScoreComputer resultComputer;

    public DotsGameSnapshot(CellState[][] cells, LineState[][] horizontalLines,
                            LineState[][] verticalLines, Set<Goodie> goodies,
                            Set<DynamicGoodie> dynamicGoodies) {
        super();
        this.cells = cells;
        this.horizontalLines = horizontalLines;
        this.verticalLines = verticalLines;
        this.goodies = goodies;
        this.dynamicGoodies = dynamicGoodies;
        scoreComputers =
                Arrays.asList(new CellScoreComputer(cells), new HorizontalLinesScoreComputer(cells),
                        new VerticalLinesScoreComputer(cells),
                        new GoodieScoreComputer(goodies, cells),
                        new DynamicGoodieScoreComputer(dynamicGoodies, cells));
        resultComputer = new ResultComputer(cells);
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
        computeScore();
        return score;
    }

    public Score getScoreWithResult(List<Task> tasks) {
        computeScore();
        computeResult(tasks);
        return score;
    }

    private void computeResult(List<Task> tasks) {
        for (Task task : tasks) {
            switch (task.getTaskType()) {
                case LINES:
                    linesTaskCompletionExaminer.examine(score, task);
                    break;
                case HORIZONTAL_LINE:
                    horizontalLinesTaskCompletionExaminer.examine(score, task);
                    break;
                case VERTICAL_LINE:
                    verticalLinesTaskCompletionExaminer.examine(score, task);
                    break;
                case GOODIES:
                    goodiesTaskCompletionExaminer.examine(score, task);
                    break;
                case ONE_K:
                case TWO_K:
                case FIVE_K:
                case DIAMOND:
                    goodieTaskCompletionExaminer.examine(score, task);
                    break;
                case DYNAMIC_GOODIE:
                    dynamicGoodieTaskCompletionExaminer.examine(score, task);
                    break;
            }
        }
        resultComputer.computeScore(score);
    }

    public void computeScore() {
        this.score.clearScore();
        for (ScoreComputer scoreComputer : scoreComputers) {
            scoreComputer.computeScore(score);
        }
    }
}
