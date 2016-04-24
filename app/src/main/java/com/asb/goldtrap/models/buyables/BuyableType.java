package com.asb.goldtrap.models.buyables;

import com.asb.goldtrap.R;

/**
 * BuyableType.
 * Created by arjun on 17/04/16.
 */
public enum BuyableType {
    FLIP(R.string.flip),
    PLUS_ONE(R.string.extra_chance),
    SKIP(R.string.skip),
    DONATE(R.string.donate_name);

    private int nameRes;

    public int getNameRes() {
        return nameRes;
    }

    BuyableType(int nameRes) {
        this.nameRes = nameRes;
    }
}
