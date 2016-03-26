package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.List;

/**
 * GoodieDaoImpl.
 * Created by arjun on 26/03/16.
 */
public class GoodieDaoImpl extends AbstractDao implements GoodieDao {
    public GoodieDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public Goodie getGoodie(String type, GoodiesState goodiesState) {
        Goodie goodie = null;
        String[] args = {type, goodiesState.name()};
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE, null, TYPE + " = ? AND " + GOODIE_STATE + " = ?", args,
                    null, null, null);
            if (cursor.moveToFirst()) {
                goodie = buildGoodieFromCursor(cursor);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return goodie;
    }

    @Override
    public long saveGoodie(Goodie goodie) {
        return database.insert(TABLE, null, getContentValues(goodie));
    }

    @Override
    public void saveGoodies(List<Goodie> goodies) {
        try {
            database.beginTransaction();
            for (Goodie goodie : goodies) {
                this.saveGoodie(goodie);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public long updateGoodie(Goodie goodie) {
        String[] args = {goodie.getType(), goodie.getGoodiesState().name()};
        return this.database.update(TABLE, getContentValuesForUpdate(goodie),
                TYPE + " = ? AND " + GOODIE_STATE + " = ?", args);
    }

    @Override
    public void updateGoodies(List<Goodie> goodies) {
        try {
            database.beginTransaction();
            for (Goodie goodie : goodies) {
                this.updateGoodie(goodie);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private ContentValues getContentValues(Goodie goodie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GOODIE_STATE, goodie.getGoodiesState().name());
        contentValues.put(TYPE, goodie.getType());
        contentValues.put(COUNT, goodie.getCount());
        return contentValues;
    }


    private Goodie buildGoodieFromCursor(Cursor cursor) {
        return Goodie.builder()
                .setId(cursor.getLong(cursor.getColumnIndex(ID)))
                .setType(cursor.getString(cursor.getColumnIndex(TYPE)))
                .setCount(cursor.getLong(cursor.getColumnIndex(COUNT)))
                .setGoodiesState(cursor.getString(cursor.getColumnIndex(GOODIE_STATE)))
                .build();
    }

    private ContentValues getContentValuesForUpdate(Goodie goodie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNT, goodie.getCount());
        return contentValues;
    }

}
