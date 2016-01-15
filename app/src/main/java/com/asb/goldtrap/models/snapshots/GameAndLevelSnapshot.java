package com.asb.goldtrap.models.snapshots;

import com.asb.goldtrap.models.eo.Level;

/**
 * Created by arjun on 15/01/16.
 */
public class GameAndLevelSnapshot {
    private DotsGameSnapshot dotsGameSnapshot;
    private Level level;

    public GameAndLevelSnapshot() {
    }

    public GameAndLevelSnapshot(DotsGameSnapshot dotsGameSnapshot,
                                Level level) {
        this.dotsGameSnapshot = dotsGameSnapshot;
        this.level = level;
    }

    public DotsGameSnapshot getDotsGameSnapshot() {
        return dotsGameSnapshot;
    }

    public void setDotsGameSnapshot(
            DotsGameSnapshot dotsGameSnapshot) {
        this.dotsGameSnapshot = dotsGameSnapshot;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
