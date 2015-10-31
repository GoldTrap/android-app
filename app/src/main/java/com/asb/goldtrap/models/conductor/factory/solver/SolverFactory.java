package com.asb.goldtrap.models.conductor.factory.solver;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.eo.Level;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;

import java.util.List;

/**
 * Created by arjun on 31/10/15.
 */
public interface SolverFactory {
    AISolver getAISolver(Level level, DotsGameSnapshot
            dotsGameSnapshot, List<Line> combinations);
}
