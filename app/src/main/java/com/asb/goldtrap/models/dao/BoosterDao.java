package com.asb.goldtrap.models.dao;

import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;

import java.util.List;

/**
 * BoosterDao.
 * Created by arjun on 09/04/16.
 */
public interface BoosterDao {
    String CURRENT = "CURRENT";
    String TOTAL = "TOTAL";

    String TABLE = "BOOSTERS";
    String INDEX = "IDX_BOOSTERS";
    String ID = "_id";
    String TYPE = "TYPE";
    String BOOSTER_TYPE = "BOOSTER_TYPE";
    String COUNT = "COUNT";
    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE
            + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT NOT NULL, "
            + BOOSTER_TYPE + " TEXT NOT NULL, "
            + COUNT + " INT NOT NULL"
            + ");";
    String INDEX_CREATE = "CREATE INDEX " + INDEX
            + " ON " + TABLE
            + " ("
            + TYPE + ","
            + BOOSTER_TYPE
            + ");";

    void saveBoosters(List<Booster> booster);

    long saveBooster(Booster booster);

    long updateBooster(Booster booster);

    Booster getBooster(String type, BoosterType boosterType);
}
