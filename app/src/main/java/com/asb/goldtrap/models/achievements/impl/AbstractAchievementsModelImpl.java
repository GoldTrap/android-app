package com.asb.goldtrap.models.achievements.impl;

import com.asb.goldtrap.models.achievements.AchievementsModel;
import com.asb.goldtrap.models.results.Score;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * AbstractAchievementsModelImpl.
 * Created by arjun on 09/04/16.
 */
public abstract class AbstractAchievementsModelImpl implements AchievementsModel {
    @Override
    public void updateAchievements(GoogleApiClient client, Score score) {

    }

    @Override
    public void updateAchievement(GoogleApiClient client, String achievementId) {

    }
}
