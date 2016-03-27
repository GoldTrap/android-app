package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.MultiplayerGameDao;

/**
 * MultiplayerGameDaoImpl.
 * Created by arjun on 27/03/16.
 */
public class MultiplayerGameDaoImpl extends AbstractDao implements MultiplayerGameDao {
    public MultiplayerGameDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public String getGameStatus(String game) {
        String value = null;
        String[] args = {game};
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE, null, GAME + " = ?", args, null, null, null);
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndex(STATUS));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return value;
    }

    @Override
    public long setGameStatus(String game, String status) {
        ContentValues values = new ContentValues(1);
        values.put(GAME, game);
        values.put(STATUS, status);
        return database.insert(TABLE, null, values);
    }
}
