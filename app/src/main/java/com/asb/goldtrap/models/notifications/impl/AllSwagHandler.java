package com.asb.goldtrap.models.notifications.impl;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.asb.goldtrap.models.notifications.factory.SwagHandlerFactory;

/**
 * All Swag Handler.
 * Created by arjun on 18/07/16.
 */
public class AllSwagHandler {
    private static final String TAG = AllSwagHandler.class.getSimpleName();
    public static final String GOLD_COINS = "GOLD_COINS";
    public static final String GOLD_NUGGETS = "GOLD_NUGGETS";
    public static final String GOLD_BARS = "GOLD_BARS";
    public static final String DIAMONDS = "DIAMONDS";

    public void handleAllSwags(Context context, Intent intent) {
        if (null != intent) {
            Bundle data = intent.getExtras();
            if (null != data) {
                Log.d(TAG, "Data:" + data);
                if (data.containsKey(GOLD_COINS)) {
                    SwagHandlerFactory.getSwagHandler(GOLD_COINS)
                            .handleSwag(context, data.getString(GOLD_COINS));
                }
                if (data.containsKey(GOLD_NUGGETS)) {
                    SwagHandlerFactory.getSwagHandler(GOLD_NUGGETS)
                            .handleSwag(context, data.getString(GOLD_NUGGETS));
                }
                if (data.containsKey(GOLD_BARS)) {
                    SwagHandlerFactory.getSwagHandler(GOLD_BARS)
                            .handleSwag(context, data.getString(GOLD_BARS));
                }
                if (data.containsKey(DIAMONDS)) {
                    SwagHandlerFactory.getSwagHandler(DIAMONDS)
                            .handleSwag(context, data.getString(DIAMONDS));
                }

            }
        }
    }
}
