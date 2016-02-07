package com.asb.goldtrap.models.dao.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.asb.goldtrap.models.dao.LevelDao;

/**
 * Created by arjun on 07/02/16.
 */
public class LevelsLoader extends AsyncTaskLoader<Cursor> {
    private LevelDao levelDao;
    private String episodeCode;

    public LevelsLoader(Context context, LevelDao levelDao, String episodeCode) {
        super(context);
        this.levelDao = levelDao;
        this.episodeCode = episodeCode;
    }

    @Override
    public Cursor loadInBackground() {
        return levelDao.getLevels(episodeCode);
    }
}
