package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.AttemptDao;
import com.asb.goldtrap.models.eo.Attempt;

/**
 * AttemptDaoImpl.
 * Created by arjun on 20/03/16.
 */
public class AttemptDaoImpl extends AbstractDao implements AttemptDao {
    public AttemptDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public long save(Attempt attempt) {
        return database
                .insert(TABLE, null, getContentValues(attempt));
    }

    @Override
    public Attempt getBestAttempt(String levelCode) {
        Attempt attempt = null;
        String[] cols =
                {ID, LEVEL_ID, LEVEL_CODE, TIME, "MAX(" + SCORE + ") AS " + SCORE, STAR, RESULT};
        String[] args = {levelCode};
        Cursor cursor = database.query(TABLE, cols, LEVEL_CODE + " = ?", args, null, null, null);
        if (cursor.moveToNext()) {
            attempt = buildAttemptFromCursor(cursor);
        }
        return attempt;
    }

    private Attempt buildAttemptFromCursor(Cursor cursor) {
        return Attempt.builder()
                .setId(cursor.getLong(cursor.getColumnIndex(ID)))
                .setLevelId(cursor.getLong(cursor.getColumnIndex(LEVEL_ID)))
                .setLevelCode(cursor.getString(cursor.getColumnIndex(LEVEL_CODE)))
                .setTime(cursor.getLong(cursor.getColumnIndex(TIME)))
                .setScore(cursor.getLong(cursor.getColumnIndex(SCORE)))
                .setStar(cursor.getLong(cursor.getColumnIndex(STAR)))
                .setResult(cursor.getString(cursor.getColumnIndex(RESULT)))
                .build();
    }

    private ContentValues getContentValues(Attempt attempt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LEVEL_ID, attempt.getLevelId());
        contentValues.put(LEVEL_CODE, attempt.getLevelCode());
        contentValues.put(SCORE, attempt.getScore());
        contentValues.put(STAR, attempt.getStar());
        contentValues.put(TIME, attempt.getTime().getTime());
        contentValues.put(RESULT, attempt.getResult().name());
        return contentValues;
    }
}
