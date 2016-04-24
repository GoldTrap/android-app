package com.asb.goldtrap.models.dao.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.asb.goldtrap.models.dao.GoodieDao;

/**
 * Goodies Loader.
 * Created by arjun on 24/04/16.
 */
public class GoodiesLoader extends AsyncTaskLoader<Cursor> {
    private GoodieDao goodieDao;

    public GoodiesLoader(Context context, GoodieDao goodieDao) {
        super(context);
        this.goodieDao = goodieDao;
    }

    @Override
    public Cursor loadInBackground() {
        return goodieDao.getAllGoodies(GoodieDao.CURRENT);
    }
}
