package com.asb.goldtrap.models.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
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

    @Override
    public Cursor getAllEpisodes() {
        return this.database.query(TABLE, null, null, null, null, null, EPISODES_ORDER_BY);
    }

    @Override
    public Episode getEpisode(String code) {
        Episode episode = null;
        String[] args = {code};
        Cursor cursor = this.database
                .query(TABLE, null, CODE + " = ? ", args, null, null, null);
        if (cursor.moveToNext()) {
            episode = this.getEpisodeFromCursor(cursor);
        }
        return episode;
    }

    @Override
    public int update(Episode episode) {
        String[] args = {episode.getCode()};
        return this.database.update(TABLE, getContentValuesForUpdate(episode), CODE + " = ?", args);
    }

    private ContentValues getContentValuesForUpdate(Episode episode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPLETED, episode.isCompleted() ? 1 : 0);
        return contentValues;
    }

    private Episode getEpisodeFromCursor(Cursor cursor) {
        return Episode.builder()
                .withId(cursor.getLong(cursor.getColumnIndex(ID)))
                .withCode(cursor.getString(cursor.getColumnIndex(CODE)))
                .withName(cursor.getString(cursor.getColumnIndex(NAME)))
                .withImage(cursor.getString(cursor.getColumnIndex(IMAGE)))
                .withNumber(cursor.getInt(cursor.getColumnIndex(NUMBER)))
                .withCompleted(cursor.getInt(cursor.getColumnIndex(COMPLETED)))
                .build();
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
