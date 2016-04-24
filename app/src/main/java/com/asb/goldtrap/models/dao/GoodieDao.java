package com.asb.goldtrap.models.dao;

import android.database.Cursor;

import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.states.enums.GoodiesState;

import java.util.List;

/**
 * Created by arjun on 26/03/16.
 */
public interface GoodieDao {
    String CURRENT = "CURRENT";
    String TOTAL = "TOTAL";

    String TABLE = "GOODIES";
    String INDEX = "IDX_GOODIES";
    String ID = "_id";
    String TYPE = "TYPE";
    String GOODIE_STATE = "GOODIE_STATE";
    String COUNT = "COUNT";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT NOT NULL, "
            + GOODIE_STATE + " TEXT NOT NULL, "
            + COUNT + " INT NOT NULL"
            + ");";
    String INDEX_CREATE = "CREATE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + TYPE + ","
            + GOODIE_STATE
            + ");";
    String GOODIES_ORDER_BY = GOODIE_STATE + " ASC";

    Goodie getGoodie(String type, GoodiesState goodiesState);

    long saveGoodie(Goodie goodie);

    void saveGoodies(List<Goodie> goodies);

    long updateGoodie(Goodie goodie);

    void updateGoodies(List<Goodie> goodies);

    Goodie buildGoodieFromCursor(Cursor cursor);

    Cursor getAllGoodies(String type);
}
