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
    private int blocked;
    private Map<String, Integer> tasks;
    private List<GoodieData> goodies;
    private List<DynamicGoodieData> dynamicGoodies;
    private String firstPlayer;
    private String adType;
    private List<Complication> complications;
    private List<Unlocks> unlocks;

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

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public Map<String, Integer> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Integer> tasks) {
        this.tasks = tasks;
    }

    public List<GoodieData> getGoodies() {
        return goodies;
    }

    public void setGoodies(List<GoodieData> goodies) {
        this.goodies = goodies;
    }

    public List<DynamicGoodieData> getDynamicGoodies() {
        return dynamicGoodies;
    }

    public void setDynamicGoodies(
            List<DynamicGoodieData> dynamicGoodies) {
        this.dynamicGoodies = dynamicGoodies;
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

    public List<Complication> getComplications() {
        return complications;
    }

    public void setComplications(
            List<Complication> complications) {
        this.complications = complications;
    }

    public List<Unlocks> getUnlocks() {
        return unlocks;
    }

    public void setUnlocks(List<Unlocks> unlocks) {
        this.unlocks = unlocks;
    }
}
