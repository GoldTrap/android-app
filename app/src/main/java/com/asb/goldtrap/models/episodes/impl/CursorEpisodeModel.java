package com.asb.goldtrap.models.episodes.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.EpisodeDaoImpl;
import com.asb.goldtrap.models.eo.Episode;
import com.asb.goldtrap.models.episodes.EpisodeModel;

import static com.asb.goldtrap.models.dao.EpisodeDao.CODE;
import static com.asb.goldtrap.models.dao.EpisodeDao.COMPLETED;
import static com.asb.goldtrap.models.dao.EpisodeDao.IMAGE;
import static com.asb.goldtrap.models.dao.EpisodeDao.NAME;
import static com.asb.goldtrap.models.dao.EpisodeDao.NUMBER;

/**
 * Created by arjun on 06/02/16.
 */
public class CursorEpisodeModel implements EpisodeModel {
    private EpisodeDao episodeDao;

    private Cursor cursor;

    public CursorEpisodeModel(Context context) {
        SQLiteOpenHelper dbHelper = new DBHelper(context);
        this.episodeDao = new EpisodeDaoImpl(dbHelper.getWritableDatabase());
        this.loadAllEpisodes();
    }

    public Episode getEpisode(int position) {
        Episode episode = null;
        if (null != cursor && !cursor.isClosed()) {
            cursor.moveToPosition(position);
            episode = Episode.builder()
                    .withNumber(cursor.getInt(cursor.getColumnIndex(NUMBER)))
                    .withImage(cursor.getString(cursor.getColumnIndex(NAME)))
                    .withName(cursor.getString(cursor.getColumnIndex(IMAGE)))
                    .withCode(cursor.getString(cursor.getColumnIndex(CODE)))
                    .withCompleted(cursor.getInt(cursor.getColumnIndex(COMPLETED)))
                    .build();
        }
        return episode;
    }

    @Override
    public void loadAllEpisodes() {
        cursor = episodeDao.getAllEpisodes();
    }

    @Override
    public int getEpisodeCount() {
        return null != cursor ? cursor.getCount() : 0;
    }

    @Override
    public void close() {
        if (null != cursor) {
            cursor.close();
        }
    }
}
