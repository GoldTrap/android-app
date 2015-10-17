package com.asb.goldtrap.models.components;

import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Created by arjun on 17/10/15.
 */
public class Goodie {
    private GoodiesState goodiesState = GoodiesState.NOTHING;
    private int row;
    private int col;

    public Goodie(GoodiesState goodiesState, int row, int col) {
        this.goodiesState = goodiesState;
        this.row = row;
        this.col = col;
    }

    public GoodiesState getGoodiesState() {
        return goodiesState;
    }

    public void setGoodiesState(GoodiesState goodiesState) {
        this.goodiesState = goodiesState;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
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
