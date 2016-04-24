package com.asb.goldtrap.models.goodie.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.dao.loaders.GoodiesLoader;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.goodie.GoodieModel;

/**
 * Cursor Goodie Model.
 * Created by arjun on 24/04/16.
 */
public class CursorGoodieModel implements GoodieModel, LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 3;
    private final LoaderManager loaderManager;
    private Listener listener;
    private GoodieDao goodieDao;
    private Context context;
    private Cursor cursor;

    public CursorGoodieModel(Context context, LoaderManager loaderManager, Listener listener) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.context = context;
        this.loaderManager = loaderManager;
        this.listener = listener;
        this.goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void loadGoodies() {
        loaderManager.restartLoader(LOADER_ID, null, this)
                .forceLoad();
    }

    @Override
    public Goodie getGoodie(int position) {
        Goodie goodie = null;
        if (null != cursor && !cursor.isClosed()) {
            cursor.moveToPosition(position);
            goodie = goodieDao.buildGoodieFromCursor(cursor);
        }
        return goodie;
    }

    @Override
    public int getGoodiesCount() {
        return null != cursor ? cursor.getCount() : 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new GoodiesLoader(context, goodieDao);
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
