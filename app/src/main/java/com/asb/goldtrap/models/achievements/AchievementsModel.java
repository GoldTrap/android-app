package com.asb.goldtrap.models.achievements;

import com.asb.goldtrap.models.results.Score;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * AchievementsModel.
 * Created by arjun on 09/04/16.
 */
public interface AchievementsModel {
    void updateAchievements(GoogleApiClient client, Score score);

    void updateAchievement(GoogleApiClient client, String achievementId);
}
