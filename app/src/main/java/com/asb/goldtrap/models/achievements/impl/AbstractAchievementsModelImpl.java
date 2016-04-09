package com.asb.goldtrap.models.achievements.impl;

import android.content.Context;
import android.util.Log;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.achievements.AchievementsModel;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.results.Result;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.List;
import java.util.Map;

/**
 * AbstractAchievementsModelImpl.
 * Created by arjun on 09/04/16.
 */
public abstract class AbstractAchievementsModelImpl implements AchievementsModel {
    private static final String TAG = AbstractAchievementsModelImpl.class.getSimpleName();
    protected Context context;

    public AbstractAchievementsModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void updateAchievements(GoogleApiClient client, Score score) {
        Games.Achievements.increment(client, context.getString(R.string.achievement_newbie), 1);
        Log.d(TAG, "Incremented Newbie");
        if (score.getResult().equals(Result.WON)) {
            Games.Achievements
                    .increment(client, context.getString(R.string.achievement_playmaster), 1);
            Log.d(TAG, "Incremented playmaster");
        }
        for (Map.Entry<GoodiesState, List<Goodie>> entry : score.getGoodies().entrySet()) {
            String achievement = getSwagResource(entry);
            Games.Achievements.increment(client, achievement, entry.getValue().size());
            Log.d(TAG,
                    "Incremented " + entry.getKey().toString() + " by " + entry.getValue().size());
        }
        Games.Achievements
                .increment(client, context.getString(R.string.achievement_super_star),
                        (int) score.getStar());
        Log.d(TAG, "Incremented Superstar " + score.getStar());
    }

    private String getSwagResource(Map.Entry<GoodiesState, List<Goodie>> entry) {
        String achievement = null;
        switch (entry.getKey()) {
            case NOTHING:
                achievement = null;
                break;
            case ONE_K:
                achievement = context.getString(R.string.achievement_coins_master);
                break;
            case TWO_K:
                achievement = context.getString(R.string.achievement_nuggets_hoarder);
                break;
            case FIVE_K:
                achievement = context.getString(R.string.achievement_nuggets_hoarder);
                break;
            case DIAMOND:
                achievement = context.getString(R.string.achievement_gold_bar_swag);
                break;
            case DYNAMIC_GOODIE:
                achievement = context.getString(R.string.achievement_points_swag);
                break;
        }
        return achievement;
    }

    @Override
    public void updateAchievement(GoogleApiClient client, String achievementId) {
        Games.Achievements.increment(client, achievementId, 1);
        Log.d(TAG, "Incremented achievement");
    }
}
