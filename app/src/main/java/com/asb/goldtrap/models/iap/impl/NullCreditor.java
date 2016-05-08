package com.asb.goldtrap.models.iap.impl;

import com.asb.goldtrap.models.iap.IAPCreditor;

/**
 * Null Consumer.
 * Created by arjun on 08/05/16.
 */
public class NullCreditor implements IAPCreditor {
    @Override
    public void creditItems() {
        // No-op
    }
}
