package com.asb.goldtrap.models.boosters.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.boosters.BoosterModel;
import com.asb.goldtrap.models.dao.BoosterDao;
import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.BoosterDaoImpl;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * BoosterModelImpl.
 * Created by arjun on 09/04/16.
 */
public class BoosterModelImpl implements BoosterModel {

    private Context context;
    private BoosterDao boosterDao;
    private GoodieDao goodieDao;
    private Map<BoosterType, Map<GoodiesState, Long>> boosterExchangeRates;

    public BoosterModelImpl(Context context) {
        this.context = context;
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        boosterDao = new BoosterDaoImpl(dbHelper.getWritableDatabase());
        goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
        Gson gson = new Gson();
        InputStream inputStream =
                context.getResources().openRawResource(R.raw.booster_exchange_rates);
        Type type = new TypeToken<Map<BoosterType, Map<GoodiesState, Long>>>() {
        }.getType();
        boosterExchangeRates =
                gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), type);
    }

    @Override
    public Map<BoosterType, Booster> getBoostersState() {
        Map<BoosterType, Booster> boosters = new HashMap<>();
        for (BoosterType boosterType : BoosterType.values()) {
            boosters.put(boosterType, boosterDao.getBooster(BoosterDao.CURRENT, boosterType));
        }
        return boosters;
    }

    @Override
    public Booster consumeBooster(BoosterType boosterType) {
        Booster booster = boosterDao.getBooster(BoosterDao.CURRENT, boosterType);
        if (booster.getCount() > 0) {
            booster.setCount(booster.getCount() - 1);
            boosterDao.saveBooster(booster);
        }
        return booster;
    }

    @Override
    public Booster buyBooster(BoosterType boosterType, GoodiesState goodiesState) {
        Booster booster = null;
        Map<GoodiesState, Long> exchangeRate = this.boosterExchangeRates.get(boosterType);
        Goodie goodie = goodieDao.getGoodie(GoodieDao.CURRENT, goodiesState);
        if (goodie.getCount() >= exchangeRate.get(goodiesState)) {
            booster = boosterDao.getBooster(BoosterDao.CURRENT, boosterType);
            booster.setCount(booster.getCount() + 1);
            boosterDao.saveBooster(booster);
            Booster totalbooster = boosterDao.getBooster(BoosterDao.TOTAL, boosterType);
            totalbooster.setCount(booster.getCount() + 1);
            boosterDao.saveBooster(totalbooster);
            goodie.setCount(goodie.getCount() - exchangeRate.get(goodiesState));
            goodieDao.updateGoodie(goodie);
        }
        return booster;
    }
}
