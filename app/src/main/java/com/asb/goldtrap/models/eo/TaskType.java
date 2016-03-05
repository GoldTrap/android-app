package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.R;

/**
 * Created by arjun on 07/11/15.
 */
public enum TaskType {
    POINTS(R.drawable.coins, R.plurals.get_points),
    LINES(R.drawable.spark, R.plurals.get_lines),
    HORIZONTAL_LINE(R.drawable.spark, R.plurals.get_horizontal_lines),
    VERTICAL_LINE(R.drawable.spark, R.plurals.get_vertical_lines),
    GOODIES(R.drawable.coins, R.plurals.get_goodies),
    ONE_K(R.drawable.coins, R.plurals.get_golds),
    TWO_K(R.drawable.coins, R.plurals.get_golds),
    FIVE_K(R.drawable.coins, R.plurals.get_golds),
    DIAMOND(R.drawable.diamond, R.plurals.get_diamonds),
    DYNAMIC_GOODIE(R.drawable.spark, R.plurals.get_dynamic_goodies);

    private int image;
    private int text;

    TaskType(int image, int text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public int getText() {
        return text;
    }
}
