package com.asb.goldtrap.models.conductor.factory.solver.impl;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.conductor.factory.solver.SolverFactory;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;
import com.asb.goldtrap.models.solvers.impl.BasicGreedySolver;
import com.asb.goldtrap.models.solvers.impl.RandomGreedySolver;
import com.asb.goldtrap.models.solvers.impl.RandomSolver;

import java.util.List;


/**
 * Created by arjun on 31/10/15.
 */
public class SolverFactoryImpl implements SolverFactory {

    public static final String RANDOM = "RANDOM";
    public static final String BASIC_GREEDY = "BASIC_GREEDY";
    public static final String RANDOM_GREEDY = "RANDOM_GREEDY";

    @Override
    public AISolver getAISolver(Level level, DotsGameSnapshot
            dotsGameSnapshot, List<Line> combinations) {
        AISolver solver;
        switch (level.getSolver()) {
            case RANDOM:
                solver = new RandomSolver(dotsGameSnapshot, combinations);
                break;
            case BASIC_GREEDY:
                solver = new BasicGreedySolver(dotsGameSnapshot, combinations);
                break;
            case RANDOM_GREEDY:
                solver = new RandomGreedySolver(dotsGameSnapshot, combinations);
                break;
            default:
                solver = new RandomSolver(dotsGameSnapshot, combinations);
        }
        return solver;
    }
}
