package com.asb.goldtrap.models.levels.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.dao.loaders.LevelsLoader;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.levels.LevelModel;

/**
 * Created by arjun on 07/02/16.
 */
public class CursorLevelModel implements LevelModel, LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 2;
    private final LoaderManager loaderManager;
    private LevelModel.Listener listener;
    private LevelDao levelDao;
    private Context context;
    private Cursor cursor;

    public CursorLevelModel(Context context, LoaderManager loaderManager,
                            LevelModel.Listener listener) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.context = context;
        this.loaderManager = loaderManager;
        this.listener = listener;
        this.levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void loadLevels(String episodeCode) {
        Bundle args = new Bundle();
        args.putString(LevelDao.EPISODE_CODE, episodeCode);
        loaderManager.restartLoader(LOADER_ID, args, this)
                .forceLoad();
    }

    @Override
    public Level getLevel(int position) {
        Level level = null;
        if (null != cursor && !cursor.isClosed()) {
            cursor.moveToPosition(position);
            level = levelDao.getLevelFromCursor(cursor);
        }
        return level;
    }

    @Override
    public int getLevelCount() {
        return null != cursor ? cursor.getCount() : 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LevelsLoader(context, levelDao, args.getString(LevelDao.EPISODE_CODE));
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
