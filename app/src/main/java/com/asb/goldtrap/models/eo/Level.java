package com.asb.goldtrap.models.eo;

import java.util.List;
import java.util.Map;

/**
 * Created by arjun on 25/10/15.
 */
public class Level {
    private String solver;
    private int rows;
    private int cols;
    private Map<String, Integer> tasks;
    private Map<String, List<GoodieValue>> goodies;
    private String firstPlayer;
    private String adType;
    private Map<String, Unlocks> unlocks;

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Map<String, Integer> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Integer> tasks) {
        this.tasks = tasks;
    }

    public Map<String, List<GoodieValue>> getGoodies() {
        return goodies;
    }

    public void setGoodies(
            Map<String, List<GoodieValue>> goodies) {
        this.goodies = goodies;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public Map<String, Unlocks> getUnlocks() {
        return unlocks;
    }

    public void setUnlocks(
            Map<String, Unlocks> unlocks) {
        this.unlocks = unlocks;
    }
}
