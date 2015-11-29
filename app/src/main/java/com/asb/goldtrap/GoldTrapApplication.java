package com.asb.goldtrap;

import android.app.Application;

import com.asb.goldtrap.models.results.Score;

/**
 * Created by arjun on 29/11/15.
 */
public class GoldTrapApplication extends Application {
    private static GoldTrapApplication singleton;
    private Score score;

    public GoldTrapApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
