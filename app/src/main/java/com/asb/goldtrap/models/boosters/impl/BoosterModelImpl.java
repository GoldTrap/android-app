package com.asb.goldtrap.models.boosters.impl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.boosters.BoosterModel;
import com.asb.goldtrap.models.dao.BoosterDao;
import com.asb.goldtrap.models.dao.GoodieDao;
import com.asb.goldtrap.models.dao.ScoreDao;
import com.asb.goldtrap.models.dao.helper.DBHelper;
import com.asb.goldtrap.models.dao.impl.BoosterDaoImpl;
import com.asb.goldtrap.models.dao.impl.GoodieDaoImpl;
import com.asb.goldtrap.models.dao.impl.ScoreDaoImpl;
import com.asb.goldtrap.models.eo.Booster;
import com.asb.goldtrap.models.eo.BoosterExchangeRate;
import com.asb.goldtrap.models.eo.BoosterType;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.eo.Score;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
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

    private static final String TAG = BoosterModelImpl.class.getSimpleName();
    private Context context;
    private BoosterDao boosterDao;
    private GoodieDao goodieDao;
    private ScoreDao scoreDao;
    private Map<BoosterType, BoosterExchangeRate> boosterExchangeRates;

    public BoosterModelImpl(Context context) {
        this.context = context;
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        boosterDao = new BoosterDaoImpl(dbHelper.getWritableDatabase());
        goodieDao = new GoodieDaoImpl(dbHelper.getWritableDatabase());
        scoreDao = new ScoreDaoImpl(dbHelper.getWritableDatabase());
        Gson gson = new Gson();
        InputStream inputStream =
                context.getResources().openRawResource(R.raw.booster_exchange_rates);
        Type type = new TypeToken<Map<BoosterType, BoosterExchangeRate>>() {
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
    public Booster consumeBooster(GoogleApiClient client, BoosterType boosterType) {
        Booster booster = boosterDao.getBooster(BoosterDao.CURRENT, boosterType);
        if (booster.getCount() > 0) {
            booster.setCount(booster.getCount() - 1);
            boosterDao.saveBooster(booster);
            handleAchievements(client, boosterType);
        }
        return booster;
    }

    private void handleAchievements(GoogleApiClient client, BoosterType boosterType) {
        switch (boosterType) {
            case FLIP:
                Games.Achievements
                        .unlock(client, context.getString(R.string.achievement_table_turner));
                Log.d(TAG, "Table Turner");
                break;
            case PLUS_ONE:
                Games.Achievements
                        .unlock(client, context.getString(R.string.achievement_plus_oner));
                Log.d(TAG, "Table Turner");
                break;
            case SKIP:
                Games.Achievements
                        .unlock(client, context.getString(R.string.achievement_skipper));
                Log.d(TAG, "Table Turner");
                break;
        }
    }

    @Override
    public Booster buyBooster(BoosterType boosterType, GoodiesState goodiesState) {
        Booster booster = null;
        Map<GoodiesState, Long> exchangeRate =
                this.boosterExchangeRates.get(boosterType).getGoodies();
        Goodie goodie = goodieDao.getGoodie(GoodieDao.CURRENT, goodiesState);
        if (goodie.getCount() >= exchangeRate.get(goodiesState)) {
            booster = buyBooster(boosterType);
            goodie.setCount(goodie.getCount() - exchangeRate.get(goodiesState));
            goodieDao.updateGoodie(goodie);
        }
        return booster;
    }

    @Override
    public Booster buyBoosterWithPoints(BoosterType boosterType) {
        Booster booster = null;
        long pointsExchangeRate = this.boosterExchangeRates.get(boosterType).getPoints();
        Score score = scoreDao.getScore(ScoreDao.CURRENT);
        if (score.getValue() >= pointsExchangeRate) {
            booster = buyBooster(boosterType);
            score.setValue(score.getValue() - pointsExchangeRate);
            scoreDao.updateScore(score);
        }
        return booster;
    }

    @NonNull
    private Booster buyBooster(BoosterType boosterType) {
        Booster booster;
        booster = boosterDao.getBooster(BoosterDao.CURRENT, boosterType);
        booster.setCount(booster.getCount() + 1);
        boosterDao.saveBooster(booster);
        Booster totalbooster = boosterDao.getBooster(BoosterDao.TOTAL, boosterType);
        totalbooster.setCount(booster.getCount() + 1);
        boosterDao.saveBooster(totalbooster);
        return booster;
    }
}
