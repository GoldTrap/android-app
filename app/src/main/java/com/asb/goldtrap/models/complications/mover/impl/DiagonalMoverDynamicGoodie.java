package com.asb.goldtrap.models.complications.mover.impl;

import com.asb.goldtrap.models.complications.mover.DynamicGoodieMover;
import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.states.enums.CellState;

import java.util.Set;

/**
 * Created by arjun on 25/10/15.
 */
public class DiagonalMoverDynamicGoodie implements DynamicGoodieMover {
    @Override
    public void moveGoodie(CellState[][] cells, int cols, int rows, Set<DynamicGoodie> goodies,
                           DynamicGoodie goodie, int startRow, int startCol) {
        int row = (startRow + 1) % rows;
        int col = (startCol + 1) % cols;
        while (!(row == startRow && col == startCol)) {
            if (CellState.FREE == cells[row][col]) {
                DynamicGoodie newGoodie = new DynamicGoodie(goodie.getGoodiesState(), row, col,
                        goodie.getDisplayValue());
                goodies.add(newGoodie);
                break;
            }
            row = (row + 1) % rows;
            col = (col + 1) % cols;
        }
        if (row == startRow && col == startCol) {
            goodies.add(goodie);
        }
    }
}
