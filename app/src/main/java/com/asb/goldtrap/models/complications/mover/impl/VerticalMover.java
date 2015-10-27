package com.asb.goldtrap.models.complications.mover.impl;

import com.asb.goldtrap.models.complications.mover.GoodieMover;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.states.enums.CellState;

import java.util.Set;

/**
 * Created by arjun on 25/10/15.
 */
public class VerticalMover implements GoodieMover {

    @Override
    public void moveGoodie(CellState[][] cells, int cols, int rows, Set goodies,
                           Goodie goodie, int startRow, int startCol) {
        int row = (startRow + 1) % rows;
        int col = startCol;
        while (!(row == startRow)) {
            if (CellState.FREE == cells[row][col]) {
                goodie.setRow(row);
                goodie.setCol(col);
                goodies.add(goodie);
                break;
            }
            row = (row + 1) % rows;
        }
        if (row == startRow) {
            goodies.add(goodie);
        }
    }
}
