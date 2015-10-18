package com.asb.goldtrap.models.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asb.goldtrap.models.dao.ChapterDao;
import com.asb.goldtrap.models.dao.PropertiesDao;

/**
 * Created by arjun on 18/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "GoldTrap.sqlite";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Starting the Database creation");
        db.execSQL(PropertiesDao.TABLE_CREATE);
        db.execSQL(PropertiesDao.INDEX_CREATE);
        db.execSQL(ChapterDao.TABLE_CREATE);
        Log.d(TAG, "Ending the Database creation");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
