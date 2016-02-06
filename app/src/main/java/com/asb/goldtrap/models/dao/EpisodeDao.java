package com.asb.goldtrap.models.dao;

import android.database.Cursor;

import com.asb.goldtrap.models.eo.migration.Episode;

/**
 * Created by arjun on 18/10/15.
 */
public interface EpisodeDao {
    String TABLE = "EPISODES";
    String INDEX = "IDX_EPISODES";
    String ID = "_id";
    String NUMBER = "NUMBER";
    String CODE = "CODE";
    String NAME = "NAME";
    String IMAGE = "IMAGE";
    String COMPLETED = "COMPLETED";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CODE + " VARCHAR UNIQUE, "
            + NAME + " VARCHAR NOT NULL, "
            + IMAGE + " VARCHAR NOT NULL, "
            + NUMBER + " INT NOT NULL, "
            + COMPLETED + " INT DEFAULT 0"
            + ");";
    String INDEX_CREATE = "CREATE UNIQUE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + CODE
            + ");";

    String EPISODES_ORDER_BY = EpisodeDao.NUMBER + " ASC";

    long save(Episode episode);

    Cursor getAllEpisodes();
}
