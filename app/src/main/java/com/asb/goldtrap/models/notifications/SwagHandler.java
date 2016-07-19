package com.asb.goldtrap.models.notifications;

import android.content.Context;
import android.content.Intent;

/**
 * Swag Handler.
 * Created by arjun on 18/07/16.
 */
public interface SwagHandler {

    void handleAllSwags(Context context, Intent intent);

    void handleSwag(Context context, String value);
}
