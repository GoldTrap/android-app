package com.asb.goldtrap.models.dao;

import com.asb.goldtrap.models.eo.Score;

/**
 * ScoreDao.
 * Created by arjun on 26/03/16.
 */
public interface ScoreDao {
    String CURRENT = "CURRENT";
    String TOTAL = "TOTAL";

    String TABLE = "SCORES";
    String INDEX = "IDX_SCORES";
    String ID = "_id";
    String TYPE = "TYPE";
    String VALUE = "VALUE";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT NOT NULL, "
            + VALUE + " INT NOT NULL"
            + ");";
    String INDEX_CREATE = "CREATE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + TYPE
            + ");";

    Score getScore(String type);

    long saveScore(Score score);

    long updateScore(Score score);
}
