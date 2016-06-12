package com.asb.goldtrap.models.iap;

import org.json.JSONObject;

/**
 * IAP Consumer.
 * Created by arjun on 08/05/16.
 */
public interface IAPCreditor {
    String LEVEL_TO_UNLOCK = "levelToUnlock";

    void creditItems(JSONObject item);
}
