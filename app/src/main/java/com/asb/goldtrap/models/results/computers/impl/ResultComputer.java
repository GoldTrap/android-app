package com.asb.goldtrap.models.results.computers.impl;

import com.asb.goldtrap.models.results.Result;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.ScoreComputer;
import com.asb.goldtrap.models.states.enums.CellState;

/**
 * Created by arjun on 28/11/15.
 */
public class ResultComputer implements ScoreComputer {
    private CellState[][] cells;

    public ResultComputer(CellState[][] cells) {
        this.cells = cells;
    }

    @Override
    public void computeScore(Score score) {
        int totalCellsOccupied = 0;
        int rows = cells.length;
        int cols = cells[0].length;
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                if (CellState.PLAYER == cells[i][j]) {
                    totalCellsOccupied += 1;
                }
            }
        }
        if (0 >= score.getIncompleteTasks().size()) {
            if (totalCellsOccupied > (rows * cols) / 2) {
                score.setResult(Result.WON);
            }
            else if ((totalCellsOccupied == (rows * cols) / 2)) {
                score.setResult(Result.DRAW);
            }
        }
        else {
            score.setResult(Result.LOST);
        }
    }
}
