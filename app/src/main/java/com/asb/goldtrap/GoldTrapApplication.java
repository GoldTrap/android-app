package com.asb.goldtrap;

import android.app.Application;
import android.net.Uri;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by arjun on 29/11/15.
 */
public class GoldTrapApplication extends Application {
    private static GoldTrapApplication singleton;
    private Uri gamePreviewUri;
    private Tracker mTracker;

    public static GoldTrapApplication getInstance() {
        return singleton;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public Uri getGamePreviewUri() {
        return gamePreviewUri;
    }

    public void setGamePreviewUri(Uri gamePreviewUri) {
        this.gamePreviewUri = gamePreviewUri;
    }

}
