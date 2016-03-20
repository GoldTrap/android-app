package com.asb.goldtrap.models.results;

import com.asb.goldtrap.models.components.Cell;
import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.components.Line;
import com.asb.goldtrap.models.eo.Task;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Score {
    public static final int CELL_SCORE = 10;
    private long star;
    private Result result;
    private List<Task> completedTasks = new ArrayList<>();
    private List<Task> incompleteTasks = new ArrayList<>();
    private List<Line> horizontalLines = new ArrayList<>();
    private List<Line> verticalLines = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private Map<GoodiesState, List<Goodie>> goodies = new HashMap<>();
    private Set<DynamicGoodie> dynamicGoodies = new HashSet<>();

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }

    public List<Task> getIncompleteTasks() {
        return incompleteTasks;
    }

    public List<Line> getHorizontalLines() {
        return horizontalLines;
    }

    public List<Line> getVerticalLines() {
        return verticalLines;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Map<GoodiesState, List<Goodie>> getGoodies() {
        return goodies;
    }

    public Set<DynamicGoodie> getDynamicGoodies() {
        return dynamicGoodies;
    }

    public int basicScore() {
        return cells.size() * CELL_SCORE;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public long getStar() {
        return star;
    }

    public void setStar(long star) {
        this.star = star;
    }

    public void clearScore() {
        completedTasks.clear();
        incompleteTasks.clear();
        horizontalLines.clear();
        verticalLines.clear();
        cells.clear();
        goodies.clear();
        dynamicGoodies.clear();
    }
}
