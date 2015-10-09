package com.asb.goldtrap.models.solvers.factory.impl;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.solvers.factory.SolversFactory;
import com.asb.goldtrap.models.solvers.impl.BasicGreedySolver;
import com.asb.goldtrap.models.solvers.impl.RandomSolver;

import java.util.List;

/**
 * Created by arjun on 09/10/15.
 */
public class BiasedTowardsMeSolversFactory implements SolversFactory {
    @Override
    public AISolver getPlayerSolver(DotsGameSnapshot dotsGameSnapshot, List<Line> combinations) {
        return new BasicGreedySolver(dotsGameSnapshot, combinations);
    }

    @Override
    public AISolver getOtherPlayerSolver(DotsGameSnapshot dotsGameSnapshot,
                                         List<Line> combinations) {
        return new RandomSolver(dotsGameSnapshot, combinations);
    }
}
