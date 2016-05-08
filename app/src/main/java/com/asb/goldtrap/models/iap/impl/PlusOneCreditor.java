package com.asb.goldtrap.models.iap.impl;

import android.content.Context;

import com.asb.goldtrap.models.eo.BoosterType;

/**
 * Plus One Creditor.
 * Created by arjun on 08/05/16.
 */
public class PlusOneCreditor extends AbstractIAPCreditor {

    public PlusOneCreditor(Context context) {
        super(context);
    }

    @Override
    protected long getItemsToCredit() {
        return 2;
    }

    @Override
    protected BoosterType getBoosterType() {
        return BoosterType.PLUS_ONE;
    }
}
