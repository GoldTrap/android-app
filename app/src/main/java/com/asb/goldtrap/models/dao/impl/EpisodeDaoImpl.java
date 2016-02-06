package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.eo.migration.Episode;

/**
 * Created by arjun on 06/02/16.
 */
public class EpisodeDaoImpl extends AbstractDao implements EpisodeDao {

    public EpisodeDaoImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public long save(Episode episode) {
        return this.database.insert(TABLE, null, getContentValues(episode));
    }

    private ContentValues getContentValues(Episode episode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMBER, episode.getNumber());
        contentValues.put(NAME, episode.getName());
        contentValues.put(IMAGE, episode.getImage());
        contentValues.put(CODE, episode.getCode());
        return contentValues;
    }
}
