package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 31/10/15.
 */
public class StrategyData {
    private String type;
    private int value;

    public StrategyData() {
    }

    public StrategyData(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
