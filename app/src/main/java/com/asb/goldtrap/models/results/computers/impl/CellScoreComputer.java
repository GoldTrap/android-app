package com.asb.goldtrap.models.results.computers.impl;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.ScoreComputer;
import com.asb.goldtrap.models.states.enums.CellState;

/**
 * Created by arjun on 28/11/15.
 */
public class CellScoreComputer implements ScoreComputer {
    private CellState[][] cells;

    public CellScoreComputer(CellState[][] cells) {
        this.cells = cells;
    }

    @Override
    public void computeScore(Score score) {
        int rows = cells.length;
        int cols = cells[0].length;

        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                if (CellState.PLAYER == cells[i][j]) {
                    Cell cell = new Cell(cells[i][j], i, j);
                    score.getCells().add(cell);
                }
            }
        }
    }
}
