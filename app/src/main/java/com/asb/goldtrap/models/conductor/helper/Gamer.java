package com.asb.goldtrap.models.conductor.helper;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.LineState;

import java.util.List;

/**
 * Created by arjun on 15/01/16.
 */
public interface Gamer {
    boolean allCellsFilled(CellState[][] cells);

    List<Cell> getBoundedCellsForVertical(CellState[][] cells,
                                          LineState[][] horizontalLines,
                                          LineState[][] verticalLines,
                                          CellState cellState, int row, int col);

    List<Cell> getBoundedCellsForHorizontal(CellState[][] cells,
                                            LineState[][] horizontalLines,
                                            LineState[][] verticalLines,
                                            CellState cellState, int row, int col);

    void flipBoard(DotsGameSnapshot dotsGameSnapshot);
}
