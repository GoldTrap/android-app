package com.asb.goldtrap.models.scores.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asb.goldtrap.models.components.DynamicGoodie;
import com.asb.goldtrap.models.dao.AttemptDao;
import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.ScoreDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.AttemptDaoImpl;
import com.asb.goldtrap.models.dao.impl.EpisodeDaoImpl;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.dao.impl.PropertiesDaoImpl;
import com.asb.goldtrap.models.dao.impl.ScoreDaoImpl;
import com.asb.goldtrap.models.eo.Attempt;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.results.Result;
import com.asb.goldtrap.models.results.Score;
import com.asb.goldtrap.models.scores.ScoreModel;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * ScoreModelImpl.
 * Created by arjun on 20/03/16.
 */
public class ScoreModelImpl implements ScoreModel {
    private static final String TAG = ScoreModelImpl.class.getSimpleName();
    private EpisodeDao episodeDao;
    private LevelDao levelDao;
    private PropertiesDao propertiesDao;
    private AttemptDao attemptDao;
    private ScoreDao scoreDao;
    private GoodieDao goodieDao;
    private SQLiteOpenHelper dbHelper;

    public ScoreModelImpl(Context context) {
        dbHelper = DBHelper.getInstance(context);
        episodeDao = new EpisodeDaoImpl(dbHelper.getWritableDatabase());
        levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
        propertiesDao = new PropertiesDaoImpl(dbHelper.getWritableDatabase());
        attemptDao = new AttemptDaoImpl(dbHelper.getWritableDatabase());
        scoreDao = new ScoreDaoImpl(dbHelper.getWritableDatabase());
        goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void updateLevelAndScore(String levelCode, Score score) {
        this.doTheLevelUpdate(levelCode, score);
        this.doTheScoreUpdate(score);
        this.doTheGoodieUpdate(score);
    }

    private void doTheGoodieUpdate(Score score) {
        List<Goodie> goodies = new ArrayList<>();
        for (Map.Entry<GoodiesState, List<com.asb.goldtrap.models.components.Goodie>> goodieEntry :
                score.getGoodies().entrySet()) {
            Goodie currentGoodie = goodieDao.getGoodie(GoodieDao.CURRENT, goodieEntry.getKey());
            Goodie totalGoodie = goodieDao.getGoodie(GoodieDao.TOTAL, goodieEntry.getKey());
            currentGoodie.setCount(currentGoodie.getCount() + goodieEntry.getValue().size());
            totalGoodie.setCount(totalGoodie.getCount() + goodieEntry.getValue().size());
            goodies.add(currentGoodie);
            goodies.add(totalGoodie);
        }
        goodieDao.updateGoodies(goodies);
        Log.d(TAG, "Updated the goodies");
    }

    private void doTheScoreUpdate(Score score) {
        com.asb.goldtrap.models.eo.Score current = scoreDao.getScore(ScoreDao.CURRENT);
        com.asb.goldtrap.models.eo.Score total = scoreDao.getScore(ScoreDao.TOTAL);
        long scoreValue = current.getValue() + score.basicScore();
        long dynamicValue = 0L;
        for (DynamicGoodie dynamicGoodie : score.getDynamicGoodies()) {
            dynamicValue += dynamicGoodie.getDisplayValue() * DynamicGoodie.MULTIPLIER;
        }
        current.setValue(scoreValue + dynamicValue);
        total.setValue(scoreValue + dynamicValue);
        scoreDao.updateScore(current);
        scoreDao.updateScore(total);
        Log.d(TAG, "Updated the Score");
    }

    private void doTheLevelUpdate(String levelCode, Score score) {
        Level level = levelDao.getLevel(levelCode);
        attemptDao.save(Attempt.builder()
                .setLevelId(level.getId())
                .setLevelCode(levelCode)
                .setResult(score.getResult())
                .setScore(score.basicScore())
                .setTime(new Date())
                .setStar(score.getStar())
                .build());
        Attempt bestAttempt = attemptDao.getBestAttempt(levelCode);

        if (level.getBestScore() <= bestAttempt.getScore()) {
            level.setBestScore(bestAttempt.getScore());
        }
        if (level.getBestStar() <= bestAttempt.getStar()) {
            level.setBestStar(bestAttempt.getStar());
        }
        if (Result.WON == score.getResult()) {
            level.setCompleted(true);
            unlockNextLevel(level);
        }
        levelDao.update(level);
    }

    private void unlockNextLevel(Level level) {
        Episode episode = episodeDao.getEpisode(level.getEpisodeCode());
        boolean updated = unlockNextLevel(
                String.format(Locale.US, "%sc%02d", episode.getCode(), level.getNumber() + 1));
        Log.d(TAG, "Unlocked next level");
        if (!updated) {
            episode.setCompleted(true);
            episodeDao.update(episode);
            unlockNextLevel(String.format(Locale.US, "e%02dc01", episode.getNumber() + 1));
            Log.d(TAG, "Unlocked next episode");
        }
    }

    private boolean unlockNextLevel(String nextLevelNumber) {
        boolean updated = false;
        Level nextLevel = levelDao.getLevel(nextLevelNumber);
        if (null != nextLevel) {
            nextLevel.setLocked(false);
            levelDao.update(nextLevel);
            updated = true;
        }
        return updated;
    }
}
