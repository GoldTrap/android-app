package com.asb.goldtrap.models.gameplay.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.PropertiesDaoImpl;
import com.asb.goldtrap.models.gameplay.Migration;

/**
 * Created by arjun on 04/10/15.
 */
public class MigrationImpl implements Migration {
    private PropertiesDao propertiesDao;
    private Migration dataMigration;
    private Migration fileMigration;
    private SQLiteOpenHelper dbHelper;

    public MigrationImpl(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        propertiesDao = new PropertiesDaoImpl(dbHelper.getWritableDatabase());
        fileMigration = new FileToDBMigrationImpl(context, dbHelper, propertiesDao);
        dataMigration = new InitDataMigrationImpl(context, dbHelper, propertiesDao);
    }

    @Override
    public void doMigrationOfData() {
        fileMigration.doMigrationOfData();
        dataMigration.doMigrationOfData();
        propertiesDao.setValue(MIGRATION_COMPLETE, "YES");
    }

    @Override
    public boolean isMigrationComplete() {
        return !TextUtils.isEmpty(propertiesDao.getValue(MIGRATION_COMPLETE));
    }
}
