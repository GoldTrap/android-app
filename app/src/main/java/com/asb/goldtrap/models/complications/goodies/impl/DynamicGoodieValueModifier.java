package com.asb.goldtrap.models.complications.goodies.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.complications.series.Series;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.Set;

/**
 * Created by arjun on 25/10/15.
 */
public class DynamicGoodieValueModifier implements GoodieOperator {
    private Series series;

    public DynamicGoodieValueModifier(Series series) {
        this.series = series;
    }

    @Override
    public void operateOnGoodie(DotsGameSnapshot dotsGameSnapshot) {
        CellState[][] cells = dotsGameSnapshot.getCells();
        Set<Goodie> goodies = dotsGameSnapshot.getGoodies();
        for (Goodie goodie : goodies) {
            if (GoodiesState.DYNAMIC_GOODIE == goodie.getGoodiesState()) {
                if (CellState.FREE == cells[goodie.getRow()][goodie.getCol()]) {
                    goodie.setDisplayValue(series.getNextTerm(goodie.getDisplayValue()));
                }
            }
        }
    }
}
