package com.asb.goldtrap.models.gameplay.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.gameplay.Migration;

/**
 * Created by arjun on 06/02/16.
 */
public class InitDataMigrationImpl implements Migration {
    private static final String TAG = InitDataMigrationImpl.class.getSimpleName();
    private final PropertiesDao propertiesDao;
    private final LevelDao levelDao;

    public InitDataMigrationImpl(Context context, SQLiteOpenHelper dbHelper,
                                 PropertiesDao propertiesDao) {
        this.propertiesDao = propertiesDao;
        this.levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public boolean isMigrationComplete() {
        return !TextUtils.isEmpty(propertiesDao.getValue(DATA_MIGRATION_COMPLETE));
    }

    @Override
    public void doMigrationOfData() {
        unlockFirstLevelOfFirstEpisode();
        propertiesDao.setValue(DATA_MIGRATION_COMPLETE, "YES");
    }

    private void unlockFirstLevelOfFirstEpisode() {
        Level level = levelDao.getLevel("e01c01");
        level.setLocked(false);
        int rows = levelDao.update(level);
        Log.d(TAG, "Unlocked " + rows + " levels");
    }
}
