package com.asb.goldtrap.models.dao;

/**
 * Created by arjun on 18/10/15.
 */
public interface PropertiesDao {
    String TABLE = "PROPERTIES";
    String INDEX = "IDX_PROPERTIES";
    String KEY = "KEY";
    String VALUE = "VALUE";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + KEY + " TEXT NOT NULL, "
            + VALUE + " TEXT NOT NULL"
            + ");";
    String INDEX_CREATE = "CREATE UNIQUE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + KEY
            + ");";

    String SOUND_TYPE = "SOUND_TYPE";

    String getValue(String key);

    long setValue(String key, String value);
}
