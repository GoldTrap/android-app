package com.asb.goldtrap.models.dao;

import com.asb.goldtrap.models.eo.Attempt;

/**
 * AttemptDao.
 * Created by arjun on 20/03/16.
 */
public interface AttemptDao {
    String TABLE = "ATTEMPTS";
    String INDEX = "IDX_ATTEMPTS";
    String ID = "_id";
    String LEVEL_ID = "LEVEL_ID";
    String LEVEL_CODE = "LEVEL_CODE";
    String TIME = "TIME";
    String RESULT = "RESULT";
    String SCORE = "SCORE";
    String STAR = "STAR";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LEVEL_ID + " INTEGER NOT NULL, "
            + LEVEL_CODE + " VARCHAR NOT NULL, "
            + TIME + " INT NOT NULL, "
            + SCORE + " INT NOT NULL, "
            + STAR + " INT NOT NULL, "
            + RESULT + " VARCHAR"
            + ");";

    String INDEX_CREATE = "CREATE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + LEVEL_CODE
            + ");";

    long save(Attempt attempt);

    Attempt getBestAttempt(String levelCode);
}
