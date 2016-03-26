package com.asb.goldtrap.models.scores;

import com.asb.goldtrap.models.results.Score;

/**
 * Created by arjun on 20/03/16.
 */
public interface ScoreModel {
    void updateScore(String levelCode, Score score);
}
