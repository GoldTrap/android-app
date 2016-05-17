package com.asb.goldtrap.models.gameplay.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.asb.goldtrap.models.dao.BoosterDao;
import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.ScoreDao;
import com.asb.goldtrap.models.dao.impl.BoosterDaoImpl;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.dao.impl.ScoreDaoImpl;
import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.eo.Score;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.gameplay.Migration;
import com.asb.goldtrap.models.sound.SoundType;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.ArrayList;
import java.util.List;

/**
 * InitDataMigrationImpl.
 * Created by arjun on 06/02/16.
 */
public class InitDataMigrationImpl implements Migration {
    private static final String TAG = InitDataMigrationImpl.class.getSimpleName();
    private final PropertiesDao propertiesDao;
    private final LevelDao levelDao;
    private final ScoreDao scoreDao;
    private final GoodieDao goodieDao;
    private final BoosterDao boosterDao;

    public InitDataMigrationImpl(Context context, SQLiteOpenHelper dbHelper,
                                 PropertiesDao propertiesDao) {
        this.propertiesDao = propertiesDao;
        this.levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
        this.scoreDao = new ScoreDaoImpl(dbHelper.getWritableDatabase());
        this.goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
        this.boosterDao = new BoosterDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public boolean isMigrationComplete() {
        return !TextUtils.isEmpty(propertiesDao.getValue(DATA_MIGRATION_COMPLETE));
    }

    @Override
    public void doMigrationOfData() {
        unlockFirstLevelOfFirstEpisode();
        setUpScores();
        setUpGoodies();
        setUpBoosters();
        setUpSound();
        propertiesDao.setValue(DATA_MIGRATION_COMPLETE, "YES");
    }

    private void setUpSound() {
        propertiesDao.setValue(PropertiesDao.SOUND_TYPE, SoundType.GUITAR.name());
    }

    private void unlockFirstLevelOfFirstEpisode() {
        Level level = levelDao.getLevel("e01c01");
        level.setLocked(false);
        int rows = levelDao.update(level);
        Log.d(TAG, "Unlocked " + rows + " levels");
    }

    private void setUpScores() {
        scoreDao.saveScore(Score.builder()
                .setType(ScoreDao.CURRENT)
                .setValue(0L)
                .build());
        scoreDao.saveScore(Score.builder()
                .setType(ScoreDao.TOTAL)
                .setValue(0L)
                .build());
        Log.d(TAG, "Set up the scores");
    }


    private void setUpGoodies() {
        List<Goodie> goodies = new ArrayList<>();
        Goodie.Builder goodieBuilder = Goodie.builder().setCount(0L);
        for (GoodiesState goodiesState : GoodiesState.values()) {
            if (-1 != goodiesState.getNameRes()) {
                goodieBuilder.setGoodiesState(goodiesState);
                goodies.add(goodieBuilder.setType(GoodieDao.CURRENT).build());
                goodies.add(goodieBuilder.setType(GoodieDao.TOTAL).build());
            }
        }
        goodieDao.saveGoodies(goodies);
        Log.d(TAG, "Set up the goodies");
    }

    private void setUpBoosters() {
        Booster.Builder boosterBuilder = Booster.builder().setCount(1L);
        List<Booster> boosters = new ArrayList<>();
        for (BoosterType boosterType : BoosterType.values()) {
            boosterBuilder.setBoosterType(boosterType);
            boosters.add(boosterBuilder.setType(BoosterDao.CURRENT).build());
            boosters.add(boosterBuilder.setType(BoosterDao.TOTAL).build());
        }
        boosterDao.saveBoosters(boosters);
        Log.d(TAG, "Set up the boosters");
    }
}
