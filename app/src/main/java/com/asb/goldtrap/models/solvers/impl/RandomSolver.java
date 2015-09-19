package com.asb.goldtrap.models.solvers.impl;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.solvers.AISolver;

import java.util.List;
import java.util.Random;

public class RandomSolver extends RandomGreedySolver implements AISolver {

    private List<Line> combinations;
    private Random random = new Random();

    public RandomSolver(DotsGameSnapshot dotsGameSnapshot, List<Line> combinations) {
        super(dotsGameSnapshot, combinations);
        this.combinations = combinations;
    }

    @Override
    public Line getNextLine() {
        int size = combinations.size();
        int index = random.nextInt(size);
        Line line = combinations.get(index);
        return line;
    }

}
