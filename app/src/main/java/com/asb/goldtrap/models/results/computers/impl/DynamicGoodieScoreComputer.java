package com.asb.goldtrap.models.results.computers.impl;

import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.ScoreComputer;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.Set;

/**
 * Created by arjun on 28/11/15.
 */
public class DynamicGoodieScoreComputer implements ScoreComputer {
    private Set<DynamicGoodie> dynamicGoodies;
    private CellState[][] cells;

    public DynamicGoodieScoreComputer(Set<DynamicGoodie> dynamicGoodies,
                                      CellState[][] cells) {
        this.dynamicGoodies = dynamicGoodies;
        this.cells = cells;
    }

    @Override
    public void computeScore(Score score) {
        for (DynamicGoodie goodie : dynamicGoodies) {
            if (CellState.PLAYER == cells[goodie.getRow()][goodie.getCol()]) {
                if (GoodiesState.NOTHING != goodie.getGoodiesState()) {
                    score.getDynamicGoodies().add(goodie);
                }
            }
        }
    }
}
