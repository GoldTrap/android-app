package com.asb.goldtrap.models.results.computers.components.impl;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.components.ScoreComponentsComputer;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.views.LineType;

/**
 * Created by arjun on 28/11/15.
 */
public class HorizontalLinesScoreComputer implements ScoreComponentsComputer {
    private CellState[][] cells;

    public HorizontalLinesScoreComputer(CellState[][] cells) {
        this.cells = cells;
    }

    @Override
    public void computeScore(DotsGameSnapshot dotsGameSnapshot) {
        Score score = dotsGameSnapshot.getScore();
        int rows = cells.length;
        int cols = cells[0].length;

        for (int i = 0; i < rows; i += 1) {
            int cellsOccupied = 0;
            for (int j = 0; j < cols; j += 1) {
                if (CellState.PLAYER == cells[i][j]) {
                    cellsOccupied += 1;
                }
            }
            if (cellsOccupied == cols) {
                score.getHorizontalLines().add(new Line(LineType.HORIZONTAL, i, -1));
            }
        }
    }
}
