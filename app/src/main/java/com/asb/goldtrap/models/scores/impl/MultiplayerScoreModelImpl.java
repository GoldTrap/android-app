package com.asb.goldtrap.models.scores.impl;

import android.content.Context;
import android.util.Log;

import com.asb.goldtrap.models.dao.MultiplayerGameDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.MultiplayerGameDaoImpl;
import com.asb.goldtrap.models.results.Score;

/**
 * MultiplayerScoreModelImpl.
 * Created by arjun on 27/03/16.
 */
public class MultiplayerScoreModelImpl extends ScoreModelImpl {
    private MultiplayerGameDao multiplayerGameDao;
    private static final String TAG = MultiplayerScoreModelImpl.class.getSimpleName();

    public MultiplayerScoreModelImpl(Context context) {
        super(context);
        multiplayerGameDao =
                new MultiplayerGameDaoImpl(DBHelper.getInstance(context).getWritableDatabase());
    }

    @Override
    public void updateScore(String levelCode, Score score, String matchId) {
        if (!MultiplayerGameDao.COMPLETED.equals(multiplayerGameDao.getGameStatus(matchId))) {
            Log.d(TAG, "Updating the Score");
            this.doTheScoreUpdate(score);
            this.doTheGoodieUpdate(score);
            multiplayerGameDao.setGameStatus(matchId, MultiplayerGameDao.COMPLETED);
        }
        else {
            Log.d(TAG, "Already updated once, not updating it");
        }
    }
}
