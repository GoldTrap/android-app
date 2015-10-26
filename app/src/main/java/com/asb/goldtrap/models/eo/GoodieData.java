package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Created by arjun on 26/10/15.
 */
public class GoodieData {
    private GoodiesState type;
    private int count;

    public GoodieData(GoodiesState type, int count) {
        this.type = type;
        this.count = count;
    }

    public GoodiesState getType() {
        return type;
    }

    public void setType(GoodiesState type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
