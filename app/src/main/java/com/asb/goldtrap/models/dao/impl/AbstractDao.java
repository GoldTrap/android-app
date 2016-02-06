package com.asb.goldtrap.models.dao.impl;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by arjun on 06/02/16.
 */
public abstract class AbstractDao {
    protected SQLiteDatabase database;

    public AbstractDao(SQLiteDatabase database) {
        this.database = database;
    }
}
