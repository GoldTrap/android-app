package com.asb.goldtrap;

import android.app.Application;
import android.net.Uri;

import com.asb.goldtrap.models.snapshots.DotsGameSnapshot;

/**
 * Created by arjun on 29/11/15.
 */
public class GoldTrapApplication extends Application {
    private static GoldTrapApplication singleton;
    private DotsGameSnapshot dotsGameSnapshot;
    private Uri gamePreviewUri;

    public GoldTrapApplication getInstance() {
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

    public DotsGameSnapshot getDotsGameSnapshot() {
        return dotsGameSnapshot;
    }

    public void setDotsGameSnapshot(
            DotsGameSnapshot dotsGameSnapshot) {
        this.dotsGameSnapshot = dotsGameSnapshot;
    }
}
