package com.asb.goldtrap.models.episodes.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.EpisodeDaoImpl;
import com.asb.goldtrap.models.dao.loaders.EpisodesLoader;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.episodes.EpisodeModel;

import static com.asb.goldtrap.models.dao.EpisodeDao.CODE;
import static com.asb.goldtrap.models.dao.EpisodeDao.COMPLETED;
import static com.asb.goldtrap.models.dao.EpisodeDao.ID;
import static com.asb.goldtrap.models.dao.EpisodeDao.IMAGE;
import static com.asb.goldtrap.models.dao.EpisodeDao.NAME;
import static com.asb.goldtrap.models.dao.EpisodeDao.NUMBER;

/**
 * Created by arjun on 06/02/16.
 */
public class CursorEpisodeModel implements EpisodeModel, LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 1;
    private final LoaderManager loaderManager;
    private Listener listener;
    private EpisodeDao episodeDao;
    private Context context;
    private Cursor cursor;

    public CursorEpisodeModel(Context context, LoaderManager loaderManager, Listener listener) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.context = context;
        this.loaderManager = loaderManager;
        this.listener = listener;
        this.episodeDao = new EpisodeDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public Episode getEpisode(int position) {
        Episode episode = null;
        if (null != cursor && !cursor.isClosed()) {
            cursor.moveToPosition(position);
            episode = Episode.builder()
                    .withId(cursor.getLong(cursor.getColumnIndex(ID)))
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
    public void loadEpisodes() {
        loaderManager.restartLoader(LOADER_ID, null, this)
                .forceLoad();
    }

    @Override
    public int getEpisodeCount() {
        return null != cursor ? cursor.getCount() : 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new EpisodesLoader(context, episodeDao);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.cursor = data;
        listener.dataChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.cursor = null;
        listener.dataChanged();
    }
}
