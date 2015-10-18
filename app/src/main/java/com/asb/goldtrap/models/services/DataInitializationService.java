package com.asb.goldtrap.models.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Migrates all the data from JSON
 */
public class DataInitializationService extends IntentService {
    public static final String INITIALIZATION_COMPLETE =
            "com.asb.goldtrap.fragments.launch.INITIALIZATION_COMPLETE";

    /**
     * Starts this service to perform migration
     */
    public static void startMigration(Context context) {
        Intent intent = new Intent(context, DataInitializationService.class);
        context.startService(intent);
    }

    public DataInitializationService() {
        super("DataInitializationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleMigration();
        }
    }

    /**
     * Handle Migration
     */
    private void handleMigration() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(INITIALIZATION_COMPLETE);
                intent.putExtra("message", "This is my message!");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }.execute();
    }
}
