package com.asb.goldtrap.models.iap.impl;

import com.asb.goldtrap.models.iap.IAPCreditor;

import org.json.JSONObject;

/**
 * Null Consumer.
 * Created by arjun on 08/05/16.
 */
public class NullCreditor implements IAPCreditor {
    @Override
    public void creditItems(JSONObject item) {

    }
}
