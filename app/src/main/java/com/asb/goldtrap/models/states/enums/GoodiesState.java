package com.asb.goldtrap.models.states.enums;

import com.asb.goldtrap.R;

public enum GoodiesState {
    NOTHING(-1, -1),
    ONE_K(R.drawable.coins, R.string.one_k),
    TWO_K(R.drawable.coins, R.string.two_k),
    FIVE_K(R.drawable.coins, R.string.five_k),
    DIAMOND(R.drawable.diamond, R.string.diamonds),
    DYNAMIC_GOODIE(-1, -1);

    private int drawableRes;
    private int nameRes;

    public int getNameRes() {
        return nameRes;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    GoodiesState(int drawableRes, int nameRes) {
        this.drawableRes = drawableRes;
        this.nameRes = nameRes;
    }
}