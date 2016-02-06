package com.asb.goldtrap.models.gameplay.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.gameplay.Migration;

/**
 * Created by arjun on 06/02/16.
 */
public class InitDataMigrationImpl implements Migration {
    private final PropertiesDao propertiesDao;

    public InitDataMigrationImpl(Context context, SQLiteOpenHelper dbHelper,
                                 PropertiesDao propertiesDao) {
        this.propertiesDao = propertiesDao;
    }

    @Override
    public boolean isMigrationComplete() {
        return !TextUtils.isEmpty(propertiesDao.getValue(DATA_MIGRATION_COMPLETE));
    }

    @Override
    public void doMigrationOfData() {
        propertiesDao.setValue(DATA_MIGRATION_COMPLETE, "YES");
    }
}
