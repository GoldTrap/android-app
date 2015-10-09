package com.asb.goldtrap.models.gameplay.impl;

import android.os.AsyncTask;

import com.asb.goldtrap.models.gameplay.Migration;

/**
 * Created by arjun on 04/10/15.
 */
public class MigrationImpl implements Migration {
    private Listener listener;

    public MigrationImpl(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void doMigrationOfData() {
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
                listener.migrationComplete();
            }
        }.execute();
    }
}
