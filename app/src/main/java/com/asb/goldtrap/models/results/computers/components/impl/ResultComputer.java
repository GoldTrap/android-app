package com.asb.goldtrap.models.results.computers.components.impl;

import com.asb.goldtrap.models.results.Result;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.components.ScoreComponentsComputer;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;

/**
 * Created by arjun on 28/11/15.
 */
public class ResultComputer implements ScoreComponentsComputer {
    private CellState[][] cells;

    public ResultComputer(CellState[][] cells) {
        this.cells = cells;
    }

    @Override
    public void computeScore(DotsGameSnapshot dotsGameSnapshot) {
        Score score = dotsGameSnapshot.getScore();
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
        computeResult(score, totalCellsOccupied, rows, cols);
        computeStars(score, totalCellsOccupied, rows, cols);
    }

    private void computeResult(Score score, int totalCellsOccupied, int rows, int cols) {
        if (0 >= score.getIncompleteTasks().size()) {
            if (totalCellsOccupied > (rows * cols) / 2) {
                score.setResult(Result.WON);
            }
            else if ((totalCellsOccupied == (rows * cols) / 2)) {
                score.setResult(Result.DRAW);
            }
            else {
                score.setResult(Result.LOST);
            }
        }
        else {
            score.setResult(Result.LOST);
        }
    }

    private void computeStars(Score score, int totalCellsOccupied, int rows, int cols) {
        int totalCells = rows * cols;
        if (0 >= score.getIncompleteTasks().size()) {
            if (totalCellsOccupied >= (2f / 3f) * totalCells) {
                score.setStar(3);
            }
            else if (totalCellsOccupied >= (1f / 3f) * totalCells) {
                score.setStar(2);
            }
            else {
                score.setStar(1);
            }
        }
        else {
            score.setStar(1);
        }
    }
}
