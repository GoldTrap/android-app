package com.asb.goldtrap.models.complications.series.impl;

import com.asb.goldtrap.models.complications.series.Series;

/**
 * Created by arjun on 25/10/15.
 */
public class AP implements Series {
    private int difference;

    public AP(int difference) {
        this.difference = difference;
    }

    @Override
    public int getNextTerm(int currentTerm) {
        return currentTerm + difference;
    }
}
