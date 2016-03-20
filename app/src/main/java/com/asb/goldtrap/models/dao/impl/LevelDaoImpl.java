package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.eo.migration.Level;

import java.util.Locale;

/**
 * Created by arjun on 06/02/16.
 */
public class LevelDaoImpl extends AbstractDao implements LevelDao {
    public LevelDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    public long save(long episodeId, String episodeCode, long levelNumber, Level level) {
        return database
                .insert(TABLE, null, getContentValues(episodeId, episodeCode, levelNumber, level));
    }

    private ContentValues getContentValues(long episodeId, String episodeCode, long levelNumber,
                                           Level level) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EPISODE_ID, episodeId);
        contentValues.put(EPISODE_CODE, episodeCode);
        contentValues.put(NUMBER, levelNumber);
        contentValues.put(CODE,
                String.format(Locale.US, "%s%s%02d", episodeCode, level.getCode(), levelNumber));
        return contentValues;
    }

    @Override
    public Cursor getLevels(String episodeCode) {
        String[] args = {episodeCode};
        return this.database
                .query(TABLE, null, LEVELS_FILTER_BY_EPISODE_CODE, args, null, null,
                        LEVELS_ORDER_BY);
    }

    @Override
    public Level getLevel(String code) {
        Level level = null;
        String[] args = {code};
        Cursor cursor = this.database
                .query(TABLE, null, CODE + " = ? ", args, null, null, null);
        if (cursor.moveToNext()) {
            level = this.getLevelFromCursor(cursor);
        }
        return level;
    }

    @Override
    public Level getLevelFromCursor(Cursor cursor) {
        return Level.builder()
                .withId(cursor.getLong(cursor.getColumnIndex(ID)))
                .withNumber(cursor.getInt(cursor.getColumnIndex(NUMBER)))
                .withCode(cursor.getString(cursor.getColumnIndex(CODE)))
                .withEpisodeCode(cursor.getString(cursor.getColumnIndex(EPISODE_CODE)))
                .withCompleted(cursor.getInt(cursor.getColumnIndex(COMPLETED)))
                .withLocked(cursor.getInt(cursor.getColumnIndex(LOCKED)))
                .withBestScore(cursor.getLong(cursor.getColumnIndex(BEST_SCORE)))
                .withBestStar(cursor.getLong(cursor.getColumnIndex(BEST_STAR)))
                .build();
    }

    @Override
    public int update(Level level) {
        String[] args = {level.getCode()};
        return this.database.update(TABLE, getContentValuesForUpdate(level), CODE + " = ?", args);
    }

    private ContentValues getContentValuesForUpdate(Level level) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BEST_SCORE, level.getBestScore());
        contentValues.put(BEST_STAR, level.getBestStar());
        contentValues.put(LOCKED, level.isLocked() ? 1 : 0);
        contentValues.put(COMPLETED, level.isCompleted() ? 1 : 0);
        return contentValues;
    }
}
