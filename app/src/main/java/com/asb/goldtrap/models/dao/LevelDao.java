package com.asb.goldtrap.models.dao;

import android.database.Cursor;

import com.asb.goldtrap.models.eo.migration.Level;

/**
 * Created by arjun on 18/10/15.
 */
public interface LevelDao {
    String TABLE = "LEVELS";
    String INDEX = "IDX_LEVELS";
    String ID = "_id";
    String EPISODE_ID = "EPISODE_ID";
    String NUMBER = "NUMBER";
    String EPISODE_CODE = "EPISODE_CODE";
    String CODE = "CODE";
    String COMPLETED = "COMPLETED";
    String BEST_SCORE = "BEST_SCORE";
    String BEST_STAR = "BEST_STAR";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EPISODE_ID + " INTEGER NOT NULL, "
            + EPISODE_CODE + " VARCHAR NOT NULL, "
            + CODE + " VARCHAR UNIQUE, "
            + NUMBER + " INT NOT NULL, "
            + BEST_SCORE + " INT DEFAULT 0, "
            + BEST_STAR + " INT DEFAULT 0, "
            + COMPLETED + " INT DEFAULT 0"
            + ");";
    String INDEX_CREATE = "CREATE UNIQUE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + CODE
            + ");";

    String LEVELS_FILTER_BY_EPISODE_CODE = EPISODE_CODE + " = ? ";
    String LEVELS_ORDER_BY = NUMBER + " ASC";

    long save(long episodeId, String episodeCode, long levelNumber, Level level);

    Cursor getLevels(String episodeCode);
}
