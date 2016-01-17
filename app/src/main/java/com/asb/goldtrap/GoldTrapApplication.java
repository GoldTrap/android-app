package com.asb.goldtrap;

import android.app.Application;
import android.net.Uri;

/**
 * Created by arjun on 29/11/15.
 */
public class GoldTrapApplication extends Application {
    private static GoldTrapApplication singleton;
    private Uri gamePreviewUri;

    public static GoldTrapApplication getInstance() {
        return singleton;
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
