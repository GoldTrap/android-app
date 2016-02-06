package com.asb.goldtrap.models.dao;

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
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EPISODE_ID + " INTEGER NOT NULL, "
            + EPISODE_CODE + " VARCHAR NOT NULL, "
            + CODE + " VARCHAR UNIQUE, "
            + NUMBER + " INT NOT NULL, "
            + COMPLETED + " INT DEFAULT 0"
            + ");";
    String INDEX_CREATE = "CREATE UNIQUE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + CODE
            + ");";

    long save(long episodeId, String episodeCode, long levelNumber, Level level);
}
