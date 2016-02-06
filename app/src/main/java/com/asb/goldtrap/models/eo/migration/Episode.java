package com.asb.goldtrap.models.eo.migration;

/**
 * Created by arjun on 06/02/16.
 */
public class Episode {
    private int number;
    private String name;
    private String code;
    private Level level;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
