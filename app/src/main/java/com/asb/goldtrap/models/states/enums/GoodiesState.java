package com.asb.goldtrap.models.states.enums;

public enum GoodiesState {
    NOTHING(0, "Nothing"),
    ONE_K(1000, "One Thousand"),
    TWO_K(2000, "Two Thousand"),
    FIVE_K(5000, "Five Thousand");

    private int value;
    private String name;

    GoodiesState(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}