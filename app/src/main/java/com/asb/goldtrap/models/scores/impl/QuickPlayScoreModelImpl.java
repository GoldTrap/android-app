package com.asb.goldtrap.models.scores.impl;

import android.content.Context;

import com.asb.goldtrap.models.results.Score;

/**
 * QuickPlayScoreModelImpl.
 * Created by arjun on 26/03/16.
 */
public class QuickPlayScoreModelImpl extends AbstractScoreModelImpl {
    public QuickPlayScoreModelImpl(Context context) {
        super(context);
    }

    @Override
    public void updateScore(String levelCode, Score score) {
        this.doTheScoreUpdate(score);
        this.doTheGoodieUpdate(score);
    }

}
