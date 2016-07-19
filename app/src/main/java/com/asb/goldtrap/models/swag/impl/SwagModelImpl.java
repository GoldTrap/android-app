package com.asb.goldtrap.models.swag.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.swag.SwagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Swag Model Implementation.
 * Created by arjun on 18/07/16.
 */
public class SwagModelImpl implements SwagModel {
    private static final String TAG = SwagModelImpl.class.getSimpleName();
    private GoodieDao goodieDao;

    public SwagModelImpl(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void addGoodieCount(GoodiesState goodiesState, int count) {
        List<Goodie> goodies = new ArrayList<>(2);
        Goodie currentGoodie = goodieDao.getGoodie(GoodieDao.CURRENT, goodiesState);
        Goodie totalGoodie = goodieDao.getGoodie(GoodieDao.TOTAL, goodiesState);
        currentGoodie.setCount(currentGoodie.getCount() + count);
        totalGoodie.setCount(totalGoodie.getCount() + count);
        goodies.add(currentGoodie);
        goodies.add(totalGoodie);
        goodieDao.updateGoodies(goodies);
        Log.i(TAG, "Updated the Goodie: " + goodiesState + " with " + count);
    }
}
