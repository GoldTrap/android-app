package com.asb.goldtrap.models.dao.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.asb.goldtrap.models.dao.EpisodeDao;

/**
 * Created by arjun on 07/02/16.
 */
public class EpisodesLoader extends AsyncTaskLoader<Cursor> {

    private EpisodeDao episodeDao;

    public EpisodesLoader(Context context, EpisodeDao episodeDao) {
        super(context);
        this.episodeDao = episodeDao;
    }

    @Override
    public Cursor loadInBackground() {
        return episodeDao.getAllEpisodes();
    }
}
