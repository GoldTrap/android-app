package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.models.conductor.factory.goodie.impl.GenericGoodieOperator;
import com.asb.goldtrap.models.conductor.factory.goodie.impl.ValueModifierGoodieOperator;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by arjun on 25/10/15.
 */
public class Level {
    public static final int MIN_ROWS = 4;
    public static final int MIN_COLS = 4;
    public static final int ADDITIONAL_ROWS = 1;
    public static final int ADDITIONAL_COLS = 1;
    public static final int BLOCKED_CELLS = 1;
    private String solver;
    private int rows;
    private int cols;
    private int blocked;
    private List<Task> tasks;
    private List<GoodieData> goodies;
    private List<DynamicGoodieData> dynamicGoodies;
    private PlayerType firstPlayer;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
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

    public PlayerType getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(PlayerType firstPlayer) {
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

    public static Level generateRandomLevel() {
        Random random = new Random();
        Level level = new Level();
        level.setCols(MIN_COLS + random.nextInt(ADDITIONAL_COLS));
        level.setRows(MIN_ROWS + random.nextInt(ADDITIONAL_ROWS));
        level.setBlocked(random.nextInt(BLOCKED_CELLS));
        level.setGoodies(Arrays.asList(new GoodieData(GoodiesState.DIAMOND, 1 + random.nextInt(2)),
                new GoodieData(GoodiesState.ONE_K, 1 + random.nextInt(2)),
                new GoodieData(GoodiesState.TWO_K, 1 + random.nextInt(2)),
                new GoodieData(GoodiesState.FIVE_K, 1 + random.nextInt(2))));
        level.setDynamicGoodies(
                Collections.singletonList(new DynamicGoodieData(15, 1 + random.nextInt(2))));
        level.setComplications(Collections.singletonList(
                new Complication(GenericGoodieOperator.DYNAMIC_GOODIE_VALUE_MODIFIER,
                        new StrategyData(ValueModifierGoodieOperator.AP, 5))));
        return level;
    }
}
