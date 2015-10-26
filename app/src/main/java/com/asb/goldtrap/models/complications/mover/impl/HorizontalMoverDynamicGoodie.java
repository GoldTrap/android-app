package com.asb.goldtrap.models.complications.mover.impl;

import com.asb.goldtrap.models.complications.mover.DynamicGoodieMover;
import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.states.enums.CellState;

import java.util.Set;

/**
 * Created by arjun on 25/10/15.
 */
public class HorizontalMoverDynamicGoodie implements DynamicGoodieMover {
    @Override
    public void moveGoodie(CellState[][] cells, int cols, int rows, Set<DynamicGoodie> goodies,
                           DynamicGoodie goodie, int startRow, int startCol) {
        int row = startRow;
        int col = (startCol + 1) % cols;
        while (!(col == startCol)) {
            if (CellState.FREE == cells[row][col]) {
                DynamicGoodie newGoodie = new DynamicGoodie(goodie.getGoodiesState(), row, col,
                        goodie.getDisplayValue());
                goodies.add(newGoodie);
                break;
            }
            col = (col + 1) % cols;
        }
        if (col == startCol) {
            goodies.add(goodie);
        }
    }
}
