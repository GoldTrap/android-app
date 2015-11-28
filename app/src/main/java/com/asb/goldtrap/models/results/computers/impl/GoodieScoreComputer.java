package com.asb.goldtrap.models.results.computers.impl;

import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.results.computers.ScoreComputer;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by arjun on 28/11/15.
 */
public class GoodieScoreComputer implements ScoreComputer {
    private Set<Goodie> goodies;
    private CellState[][] cells;

    public GoodieScoreComputer(Set<Goodie> goodies,
                               CellState[][] cells) {
        this.goodies = goodies;
        this.cells = cells;
    }

    @Override
    public void computeScore(Score score) {
        for (Goodie goodie : goodies) {
            if (CellState.PLAYER == cells[goodie.getRow()][goodie.getCol()]) {
                if (GoodiesState.NOTHING != goodie.getGoodiesState()) {
                    List<Goodie> goodieScore = score.getGoodies().get(goodie.getGoodiesState());
                    if (null == goodieScore) {
                        goodieScore = new LinkedList<>();
                        score.getGoodies().put(goodie.getGoodiesState(), goodieScore);
                    }
                    goodieScore.add(goodie);
                }
            }
        }
    }
}
