package com.asb.goldtrap.models.iap.impl;

import android.content.Context;

import com.asb.goldtrap.models.eo.BoosterType;

/**
 * Flip Creditor.
 * Created by arjun on 08/05/16.
 */
public class FlipCreditor extends AbstractIAPCreditor {

    public FlipCreditor(Context context) {
        super(context);
    }

    @Override
    protected long getItemsToCredit() {
        return 1;
    }

    @Override
    protected BoosterType getBoosterType() {
        return BoosterType.FLIP;
    }
}
