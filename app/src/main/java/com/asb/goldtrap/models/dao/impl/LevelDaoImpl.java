package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
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
}
