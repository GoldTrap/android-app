package com.asb.goldtrap.models.iap.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.asb.goldtrap.models.dao.BoosterDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.BoosterDaoImpl;
import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.iap.IAPCreditor;

import org.json.JSONObject;

/**
 * Abstract IAP Consumer.
 * Created by arjun on 08/05/16.
 */
public abstract class AbstractIAPCreditor implements IAPCreditor {

    private final BoosterDaoImpl boosterDao;

    public AbstractIAPCreditor(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.boosterDao = new BoosterDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void creditItems(JSONObject item) {
        Booster booster = boosterDao.getBooster(BoosterDao.CURRENT, getBoosterType());
        booster.setCount(booster.getCount() + getItemsToCredit());
        boosterDao.updateBooster(booster);
    }

    protected abstract long getItemsToCredit();

    protected abstract BoosterType getBoosterType();
}
