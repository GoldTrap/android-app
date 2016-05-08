package com.asb.goldtrap.models.iap.impl;

import android.content.Context;

import com.asb.goldtrap.models.eo.BoosterType;

/**
 * Skip Creditor.
 * Created by arjun on 08/05/16.
 */
public class SkipCreditor extends AbstractIAPCreditor {

    public SkipCreditor(Context context) {
        super(context);
    }

    @Override
    protected long getItemsToCredit() {
        return 3;
    }

    @Override
    protected BoosterType getBoosterType() {
        return BoosterType.SKIP;
    }
}
