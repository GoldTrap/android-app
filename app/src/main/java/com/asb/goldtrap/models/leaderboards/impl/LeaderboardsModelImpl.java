package com.asb.goldtrap.models.leaderboards.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.components.Goodie;
import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.ScoreDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.dao.impl.ScoreDaoImpl;
import com.asb.goldtrap.models.leaderboards.LeaderboardsModel;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.List;
import java.util.Map;

/**
 * LeaderboardsModelImpl.
 * Created by arjun on 27/03/16.
 */
public class LeaderboardsModelImpl implements LeaderboardsModel {
    private static final String TAG = LeaderboardsModelImpl.class.getSimpleName();
    private Context context;
    private ScoreDao scoreDao;
    private GoodieDao goodieDao;

    public LeaderboardsModelImpl(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.context = context;
        scoreDao = new ScoreDaoImpl(dbHelper.getWritableDatabase());
        goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void updateLeaderboards(GoogleApiClient client, Score score) {
        com.asb.goldtrap.models.eo.Score updatedScore = scoreDao.getScore(ScoreDao.CURRENT);
        Games.Leaderboards.submitScore(client, context.getString(R.string.leaderboard_points),
                updatedScore.getValue());
        Log.d(TAG, "Submitted the score for leaderboard");
        for (Map.Entry<GoodiesState, List<Goodie>> goodieEntry :
                score.getGoodies().entrySet()) {
            com.asb.goldtrap.models.eo.Goodie totalGoodie =
                    goodieDao.getGoodie(GoodieDao.CURRENT, goodieEntry.getKey());
            Games.Leaderboards.submitScore(client, getLeaderboard(goodieEntry.getKey()),
                    totalGoodie.getCount());
            Log.d(TAG, "Submitted the goodie for leaderboard: " + goodieEntry.getKey());
        }
    }

    private String getLeaderboard(GoodiesState goodiesState) {
        String leaderboard = null;
        switch (goodiesState) {
            case NOTHING:
                break;
            case ONE_K:
                leaderboard = context.getString(R.string.leaderboard_gold_coins);
                break;
            case TWO_K:
                leaderboard = context.getString(R.string.leaderboard_gold_nuggets);
                break;
            case FIVE_K:
                leaderboard = context.getString(R.string.leaderboard_gold_bars);
                break;
            case DIAMOND:
                leaderboard = context.getString(R.string.leaderboard_diamonds);
                break;
            case DYNAMIC_GOODIE:
                break;
        }
        return leaderboard;
    }
}
