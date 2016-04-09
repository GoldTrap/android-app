package com.asb.goldtrap.models.achievements.impl;

import android.content.Context;
import android.util.Log;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.results.Result;
import com.asb.goldtrap.models.results.Score;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * MultiplayerAchievementsModel.
 * Created by arjun on 09/04/16.
 */
public class MultiplayerAchievementsModel extends AbstractAchievementsModelImpl {
    private static final String TAG = MultiplayerAchievementsModel.class.getSimpleName();

    public MultiplayerAchievementsModel(Context context) {
        super(context);
    }

    @Override
    public void updateAchievements(GoogleApiClient client, Score score) {
        super.updateAchievements(client, score);
        if (score.getResult().equals(Result.WON)) {
            Games.Achievements
                    .increment(client, context.getString(R.string.achievement_multiplayer_guru), 1);
            Log.d(TAG, "Incremented multiplayer guru");
        }
    }
}
