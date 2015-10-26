package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 25/10/15.
 */
public class DynamicGoodieData {
    private int value;
    private int count;

    public DynamicGoodieData(int value, int count) {
        this.value = value;
        this.count = count;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
