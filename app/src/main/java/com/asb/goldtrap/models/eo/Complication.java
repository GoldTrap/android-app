package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 26/10/15.
 */
public class Complication {
    private String operator;
    private StrategyData strategy;

    public Complication() {
    }

    public Complication(String operator, StrategyData strategy) {
        this.operator = operator;
        this.strategy = strategy;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public StrategyData getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyData strategy) {
        this.strategy = strategy;
    }
}

