package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 25/10/15.
 */
public class Unlocks {
    private UnlockType unlockType;
    private int points;

    public Unlocks(UnlockType unlockType, int points) {
        this.unlockType = unlockType;
        this.points = points;
    }

    public UnlockType getUnlockType() {
        return unlockType;
    }

    public void setUnlockType(UnlockType unlockType) {
        this.unlockType = unlockType;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
