package com.asb.goldtrap.models.solvers.impl;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.solvers.AISolver;

import java.util.List;

public class BasicGreedySolver extends RandomGreedySolver implements AISolver {

    public BasicGreedySolver(DotsGameSnapshot dotsGameSnapshot,
                             List<Line> combinations) {
        super(dotsGameSnapshot, combinations);
    }

    @Override
    public Line getNextLine() {
        return super.getNextGreedyLine();
    }
}
