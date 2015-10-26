package com.asb.goldtrap.models.components;

import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Created by arjun on 26/10/15.
 */
public class DynamicGoodie extends Goodie {
    private int displayValue = 0;


    public DynamicGoodie(GoodiesState goodiesState, int row, int col, int displayValue) {
        super(goodiesState, row, col);
        this.displayValue = displayValue;
    }

    public int getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(int displayValue) {
        this.displayValue = displayValue;
    }

}
