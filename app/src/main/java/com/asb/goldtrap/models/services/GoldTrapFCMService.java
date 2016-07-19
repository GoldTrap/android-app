package com.asb.goldtrap.models.services;

import android.util.Log;

import com.asb.goldtrap.models.notifications.SwagHandler;
import com.asb.goldtrap.models.notifications.factory.SwagHandlerFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Firebase Messaging Service of the app.
 * Created by arjun on 18/07/16.
 */
public class GoldTrapFCMService extends FirebaseMessagingService {

    private static final String TAG = "GoldTrapFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Map<String, String> data = remoteMessage.getData();
        if (null != data) {
            Log.d(TAG, "Data:" + data);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                SwagHandler handler = SwagHandlerFactory.getSwagHandler(entry.getKey());
                handler.handleSwag(this, entry.getValue());
            }
        }
    }
}
