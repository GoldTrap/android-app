package com.asb.goldtrap.models.episodes.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.EpisodeDaoImpl;
import com.asb.goldtrap.models.episodes.EpisodeModel;

/**
 * Created by arjun on 06/02/16.
 */
public class EpisodeModelImpl implements EpisodeModel {
    private EpisodeDao episodeDao;

    public EpisodeModelImpl(Context context) {
        SQLiteOpenHelper dbHelper = new DBHelper(context);
        this.episodeDao = new EpisodeDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public Cursor getAllEpisodes() {
        return episodeDao.getAllEpisodes();
    }
}
