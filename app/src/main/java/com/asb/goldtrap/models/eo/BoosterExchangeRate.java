package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.Map;

/**
 * BoosterExchangeRate.
 * Created by arjun on 17/04/16.
 */
public class BoosterExchangeRate {
    private long points;
    private Map<GoodiesState, Long> goodies;

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public Map<GoodiesState, Long> getGoodies() {
        return goodies;
    }

    public void setGoodies(
            Map<GoodiesState, Long> goodies) {
        this.goodies = goodies;
    }
}
