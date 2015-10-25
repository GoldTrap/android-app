package com.asb.goldtrap.models.complications.goodies.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.complications.mover.GoodieMover;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arjun on 17/10/15.
 */
public class GoodiePositionModifier implements GoodieOperator {

    private GoodieMover mover;

    public GoodiePositionModifier(GoodieMover mover) {
        this.mover = mover;
    }

    @Override
    public void operateOnGoodie(DotsGameSnapshot dotsGameSnapshot) {
        CellState[][] cells = dotsGameSnapshot.getCells();
        int cols = cells[0].length;
        int rows = cells.length;
        Set<Goodie> currentGoodies = dotsGameSnapshot.getGoodies();
        Set<Goodie> goodies = new HashSet<>();
        for (Goodie goodie : currentGoodies) {
            int startRow = goodie.getRow();
            int startCol = goodie.getCol();
            if (CellState.FREE == cells[startRow][startCol]) {
                mover.moveGoodie(cells, cols, rows, goodies, goodie, startRow, startCol);
            }
            else {
                goodies.add(goodie);
            }
        }
        dotsGameSnapshot.setGoodies(goodies);
    }

}
