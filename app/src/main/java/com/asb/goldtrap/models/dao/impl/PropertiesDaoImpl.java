package com.asb.goldtrap.models.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.PropertiesDao;

/**
 * Created by arjun on 18/10/15.
 */
public class PropertiesDaoImpl implements PropertiesDao {

    private SQLiteDatabase database;

    public PropertiesDaoImpl(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public String getValue(String key) {
        String value = null;
        String[] args = {key};
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE, null, "KEY = ?", args, null, null, null);
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndex(VALUE));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return value;
    }
}
