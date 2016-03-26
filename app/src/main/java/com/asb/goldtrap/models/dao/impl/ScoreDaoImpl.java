package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.ScoreDao;
import com.asb.goldtrap.models.eo.Score;

/**
 * ScoreDaoImpl.
 * Created by arjun on 26/03/16.
 */
public class ScoreDaoImpl extends AbstractDao implements ScoreDao {

    public ScoreDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public Score getScore(String type) {
        Score score = null;
        String[] args = {type};
        Cursor cursor = null;
        try {
            cursor = database.query(TABLE, null, TYPE + " = ?", args, null, null, null);
            if (cursor.moveToFirst()) {
                score = buildScoreFromCursor(cursor);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return score;
    }

    @Override
    public long saveScore(Score score) {
        return database.insert(TABLE, null, getContentValues(score));
    }

    @Override
    public long updateScore(Score score) {
        String[] args = {score.getType()};
        return this.database.update(TABLE, getContentValuesForUpdate(score), TYPE + " = ?", args);
    }

    private Score buildScoreFromCursor(Cursor cursor) {
        return Score.builder()
                .setId(cursor.getLong(cursor.getColumnIndex(ID)))
                .setType(cursor.getString(cursor.getColumnIndex(TYPE)))
                .setValue(cursor.getLong(cursor.getColumnIndex(VALUE)))
                .build();
    }

    private ContentValues getContentValues(Score score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE, score.getType());
        contentValues.put(VALUE, score.getValue());
        return contentValues;
    }


    private ContentValues getContentValuesForUpdate(Score score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VALUE, score.getValue());
        return contentValues;
    }
}
