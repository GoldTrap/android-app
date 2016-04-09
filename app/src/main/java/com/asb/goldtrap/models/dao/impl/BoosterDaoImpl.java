package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.BoosterDao;
import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;

import java.util.List;

/**
 * BoosterDaoImpl.
 * Created by arjun on 09/04/16.
 */
public class BoosterDaoImpl extends AbstractDao implements BoosterDao {

    public BoosterDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public void saveBoosters(List<Booster> boosters) {
        try {
            database.beginTransaction();
            for (Booster booster : boosters) {
                this.saveBooster(booster);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public long saveBooster(Booster booster) {
        return database.insert(TABLE, null, getContentValues(booster));
    }

    @Override
    public long updateBooster(Booster booster) {
        String[] args = {booster.getType(), booster.getBoosterType().name()};
        return this.database.update(TABLE, getContentValuesForUpdate(booster),
                TYPE + " = ? AND " + BOOSTER_TYPE + " = ?", args);
    }

    @Override
    public Booster getBooster(String type, BoosterType boosterType) {
        Booster booster = null;
        String[] args = {type, boosterType.name()};
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE, null, TYPE + " = ? AND " + BOOSTER_TYPE + " = ?", args,
                    null, null, null);
            if (cursor.moveToFirst()) {
                booster = buildBoosterFromCursor(cursor);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return booster;
    }

    private ContentValues getContentValues(Booster booster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOSTER_TYPE, booster.getBoosterType().name());
        contentValues.put(TYPE, booster.getType());
        contentValues.put(COUNT, booster.getCount());
        return contentValues;
    }

    private Booster buildBoosterFromCursor(Cursor cursor) {
        return Booster.builder()
                .setId(cursor.getLong(cursor.getColumnIndex(ID)))
                .setType(cursor.getString(cursor.getColumnIndex(TYPE)))
                .setCount(cursor.getLong(cursor.getColumnIndex(COUNT)))
                .setBoosterType(cursor.getString(cursor.getColumnIndex(BOOSTER_TYPE)))
                .build();
    }

    private ContentValues getContentValuesForUpdate(Booster booster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNT, booster.getCount());
        return contentValues;
    }

}
