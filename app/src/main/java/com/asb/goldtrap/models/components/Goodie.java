package com.asb.goldtrap.models.components;

import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Created by arjun on 17/10/15.
 */
public class Goodie {
    final private GoodiesState goodiesState;
    final private int row;
    final private int col;
    private int displayValue = 0;

    public Goodie(GoodiesState goodiesState, int row, int col) {
        this.goodiesState = goodiesState;
        this.row = row;
        this.col = col;
    }

    public Goodie(GoodiesState goodiesState, int row, int col, int displayValue) {
        this(goodiesState, row, col);
        this.displayValue = displayValue;
    }

    public GoodiesState getGoodiesState() {
        return goodiesState;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(int displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public int hashCode() {
        return 17 * row + 31 * col + 37 * goodiesState.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof Goodie) {
            Goodie goodie = (Goodie) o;
            if (goodie.row == this.row && goodie.col == this.col &&
                    goodie.goodiesState == this.goodiesState) {
                equals = true;
            }
        }
        return equals;
    }
}
