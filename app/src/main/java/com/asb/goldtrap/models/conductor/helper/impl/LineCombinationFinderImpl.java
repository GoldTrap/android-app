package com.asb.goldtrap.models.conductor.helper.impl;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.helper.LineCombinationFinder;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;
import com.asb.goldtrap.views.LineType;

import java.util.List;
import java.util.Set;

/**
 * Created by arjun on 15/01/16.
 */
public class LineCombinationFinderImpl implements LineCombinationFinder {

    @Override
    public void findAllLineCombinations(DotsGameSnapshot dotsGameSnapshot, List<Line> combinations,
                                        Set<Line> cSet) {
        combinations.clear();
        cSet.clear();
        CellState[][] cells = dotsGameSnapshot.getCells();
        LineState[][] horizontalLines = dotsGameSnapshot.getHorizontalLines();

        int horiRow = cells.length + 1;
        int horiCol = cells[0].length;

        for (int row = 0; row < horiRow; row += 1) {
            for (int col = 0; col < horiCol; col += 1) {
                if (horizontalLines[row][col] == LineState.FREE) {
                    Line line = new Line(LineType.HORIZONTAL, row, col);
                    combinations.add(line);
                    cSet.add(line);
                }
            }
        }

        int vertiRow = cells.length;
        int vertiCol = cells[0].length + 1;
        LineState[][] verticalLines = dotsGameSnapshot.getVerticalLines();
        for (int row = 0; row < vertiRow; row += 1) {
            for (int col = 0; col < vertiCol; col += 1) {
                if (verticalLines[row][col] == LineState.FREE) {
                    Line line = new Line(LineType.VERTICAL, row, col);
                    combinations.add(line);
                    cSet.add(line);
                }
            }
        }

    }
}
