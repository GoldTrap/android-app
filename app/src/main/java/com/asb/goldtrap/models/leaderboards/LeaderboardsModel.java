package com.asb.goldtrap.models.leaderboards;

import com.asb.goldtrap.models.results.Score;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * LeaderboardsModel.
 * Created by arjun on 27/03/16.
 */
public interface LeaderboardsModel {
    void updateLeaderboards(GoogleApiClient client, Score score);
}
