package com.asb.goldtrap.models.iap.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.LevelDaoImpl;
import com.asb.goldtrap.models.dao.impl.PropertiesDaoImpl;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.iap.IAPCreditor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Unlock Level Creditor.
 * Created by arjun on 12/06/16.
 */
public class UnlockLevelCreditor implements IAPCreditor {

    private static final String TAG = UnlockLevelCreditor.class.getSimpleName();
    private final LevelDao levelDao;
    private final PropertiesDao propertiesDao;

    public UnlockLevelCreditor(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        this.levelDao = new LevelDaoImpl(dbHelper.getWritableDatabase());
        this.propertiesDao = new PropertiesDaoImpl(dbHelper.getWritableDatabase());
    }

    @Override
    public void creditItems(JSONObject item) {
        if (item.has(LEVEL_TO_UNLOCK)) {
            try {
                unlockLevel(item.getString(LEVEL_TO_UNLOCK));
            } catch (JSONException e) {
                Log.e(TAG, "Json screw up", e);
            }
        }
        String level = propertiesDao.getValue(PropertiesDao.LEVEL_SELECTED_FOR_UNLOCK);
        if (!TextUtils.isEmpty(level)) {
            unlockLevel(level);
        }
    }

    private void unlockLevel(String levelCode) {
        Level level = levelDao.getLevel(levelCode);
        level.setLocked(false);
        levelDao.update(level);
    }
}
