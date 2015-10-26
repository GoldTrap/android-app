package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 26/10/15.
 */
public class Complication {
    private String operator;
    private String strategy;

    public Complication(String operator, String strategy) {
        this.operator = operator;
        this.strategy = strategy;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}

