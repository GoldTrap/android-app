package com.asb.goldtrap.models.achievements;

import com.asb.goldtrap.models.results.Score;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * AchievementsModel.
 * Created by arjun on 27/03/16.
 */
public interface AchievementsModel {
    void updateAchievements(GoogleApiClient client, Score score);
}
