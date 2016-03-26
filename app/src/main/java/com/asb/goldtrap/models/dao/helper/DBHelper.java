package com.asb.goldtrap.models.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asb.goldtrap.models.dao.AttemptDao;
import com.asb.goldtrap.models.dao.EpisodeDao;
import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.LevelDao;
import com.asb.goldtrap.models.dao.PropertiesDao;
import com.asb.goldtrap.models.dao.ScoreDao;

/**
 * DBHelper.
 * Created by arjun on 18/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "GoldTrap.sqlite";
    public static final int DATABASE_VERSION = 1;

    public static DBHelper instance;

    public static SQLiteOpenHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (DBHelper.class) {
                if (null == instance) {
                    instance = new DBHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Starting the Database creation");
        createTables(db);
        createIndexes(db);
        Log.d(TAG, "Ending the Database creation");
    }

    private void createTables(SQLiteDatabase db) {
        Log.d(TAG, "Starting the Tables creation");
        db.execSQL(PropertiesDao.TABLE_CREATE);
        db.execSQL(EpisodeDao.TABLE_CREATE);
        db.execSQL(LevelDao.TABLE_CREATE);
        db.execSQL(AttemptDao.TABLE_CREATE);
        db.execSQL(ScoreDao.TABLE_CREATE);
        db.execSQL(GoodieDao.TABLE_CREATE);
        Log.d(TAG, "Ending the Tables creation");
    }

    private void createIndexes(SQLiteDatabase db) {
        Log.d(TAG, "Starting the Indexes creation");
        db.execSQL(PropertiesDao.INDEX_CREATE);
        db.execSQL(EpisodeDao.INDEX_CREATE);
        db.execSQL(LevelDao.INDEX_CREATE);
        db.execSQL(AttemptDao.INDEX_CREATE);
        db.execSQL(ScoreDao.INDEX_CREATE);
        db.execSQL(GoodieDao.INDEX_CREATE);
        Log.d(TAG, "Ending the Indexes creation");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
