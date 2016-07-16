package com.asb.goldtrap.models.iap.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.iap.IAPCreditor;

import org.json.JSONObject;

/**
 * Unlock All Level Creditor.
 * Created by arjun on 16/07/16.
 */
public class UnlockAllLevelCreditor implements IAPCreditor {
    private static final String TAG = UnlockLevelCreditor.class.getSimpleName();
    private final LevelDao levelDao;

    public UnlockAllLevelCreditor(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void creditItems(JSONObject item) {
        ContentValues values = new ContentValues();
        values.put(LevelDao.LOCKED, 0);
        int updatedRows = levelDao.updateAllLevels(values);
        Log.i(TAG, "Unlocked " + updatedRows + " levels");
    }
}
