package com.asb.goldtrap.models.results;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.components.Line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Score {
    private List<Line> lines = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private Set<Goodie> goodies = new HashSet<>();

    public List<Line> getLines() {
        return lines;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Set<Goodie> getGoodies() {
        return goodies;
    }
}
