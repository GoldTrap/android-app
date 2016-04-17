package com.asb.goldtrap.models.buyables.impl;

import android.content.Context;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.buyables.BuyablesModel;
import com.asb.goldtrap.models.eo.Buyable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * BuyablesModelImpl.
 * Created by arjun on 17/04/16.
 */
public class BuyablesModelImpl implements BuyablesModel {

    private List<Buyable> buyables;

    public BuyablesModelImpl(Context context) {
        Gson gson = new Gson();
        InputStream inputStream =
                context.getResources().openRawResource(R.raw.buyables);
        Type type = new TypeToken<List<Buyable>>() {
        }.getType();
        buyables =
                gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), type);
    }

    @Override
    public Buyable getBuyable(int position) {
        return buyables.get(position);
    }

    @Override
    public int getBuyablesCount() {
        return buyables.size();
    }
}
