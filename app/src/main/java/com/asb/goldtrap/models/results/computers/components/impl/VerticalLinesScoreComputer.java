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
public class VerticalLinesScoreComputer implements ScoreComponentsComputer {
    private CellState[][] cells;

    public VerticalLinesScoreComputer(CellState[][] cells) {
        this.cells = cells;
    }

    @Override
    public void computeScore(DotsGameSnapshot dotsGameSnapshot) {
        Score score = dotsGameSnapshot.getScore();
        int rows = cells.length;
        int cols = cells[0].length;
        for (int i = 0; i < cols; i += 1) {
            int cellsOccupied = 0;
            for (int j = 0; j < rows; j += 1) {
                if (CellState.PLAYER == cells[j][i]) {
                    cellsOccupied += 1;
                }
            }
            if (cellsOccupied == rows) {
                score.getVerticalLines().add(new Line(LineType.VERTICAL, -1, i));
            }
        }
    }
}
