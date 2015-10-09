package com.asb.goldtrap.models.solvers.factory;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.solvers.AISolver;

import java.util.List;

/**
 * Created by arjun on 09/10/15.
 */
public interface SolversFactory {
    AISolver getPlayerSolver(DotsGameSnapshot dotsGameSnapshot, List<Line> combinations);

    AISolver getOtherPlayerSolver(DotsGameSnapshot dotsGameSnapshot, List<Line> combinations);
}
