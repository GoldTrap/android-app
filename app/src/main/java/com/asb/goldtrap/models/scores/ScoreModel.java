package com.asb.goldtrap.models.scores;

import com.asb.goldtrap.models.eo.BoosterExchangeRate;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.results.Score;

/**
 * Created by arjun on 20/03/16.
 */
public interface ScoreModel {
    void updateScore(String levelCode, Score score, String matchId);

    com.asb.goldtrap.models.eo.Score getCurrentScore();

    com.asb.goldtrap.models.eo.Score tradeBoosterForScore(BoosterType boosterType,
                                                          BoosterExchangeRate boosterExchangeRate);
}
