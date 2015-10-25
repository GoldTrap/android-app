package com.asb.goldtrap.models.complications.series.impl;

import com.asb.goldtrap.models.complications.series.Series;

/**
 * Created by arjun on 25/10/15.
 */
public class GP implements Series {
    private int ratio;

    public GP(int ratio) {
        this.ratio = ratio;
    }

    @Override
    public int getNextTerm(int currentTerm) {
        return currentTerm * ratio;
    }
}
