package com.asb.goldtrap.models.gameplay.impl;

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

    public MigrationImpl(DBHelper dbHelper) {
        propertiesDao = new PropertiesDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void doMigrationOfData() {

    }

    @Override
    public boolean isMigrationComplete() {
        return !TextUtils.isEmpty(propertiesDao.getValue(MIGRATION_COMPLETE));
    }
}
