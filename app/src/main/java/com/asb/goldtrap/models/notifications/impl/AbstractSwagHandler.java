package com.asb.goldtrap.models.notifications.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.asb.goldtrap.models.notifications.SwagHandler;

/**
 * Abstract Swag Handler.
 * Created by arjun on 18/07/16.
 */
public abstract class AbstractSwagHandler implements SwagHandler {

    protected void notifyUser(final Context context, final String qtyString) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, qtyString, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
