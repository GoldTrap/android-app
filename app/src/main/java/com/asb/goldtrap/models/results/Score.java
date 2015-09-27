package com.asb.goldtrap.models.results;

import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.states.enums.CellState;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.ArrayList;
import java.util.List;

public class Score {
    private List<Line> lines = new ArrayList<>();
    private List<CellState> cells = new ArrayList<>();
    private List<GoodiesState> goodies = new ArrayList<>();

    public List<Line> getLines() {
        return lines;
    }

    public List<CellState> getCells() {
        return cells;
    }

    public List<GoodiesState> getGoodies() {
        return goodies;
    }
}
