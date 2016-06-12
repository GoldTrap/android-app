package com.asb.goldtrap.models.iap.factory;

import android.content.Context;

import com.asb.goldtrap.models.iap.IAPCreditor;
import com.asb.goldtrap.models.iap.impl.FlipCreditor;
import com.asb.goldtrap.models.iap.impl.NullCreditor;
import com.asb.goldtrap.models.iap.impl.PlusOneCreditor;
import com.asb.goldtrap.models.iap.impl.SkipCreditor;
import com.asb.goldtrap.models.iap.impl.UnlockLevelCreditor;

/**
 * IAP Creditor Factory.
 * Created by arjun on 08/05/16.
 */
public class IAPCreditorFactory {
    public IAPCreditor getIAPCreditor(String sku, Context context) {
        IAPCreditor iapCreditor = null;
        switch (sku) {
            case "donate":
                iapCreditor = new NullCreditor();
                break;
            case "get_flip":
                iapCreditor = new FlipCreditor(context);
                break;
            case "get_plus_one":
                iapCreditor = new PlusOneCreditor(context);
                break;
            case "get_skip":
                iapCreditor = new SkipCreditor(context);
                break;
            case "unlock_level":
                iapCreditor = new UnlockLevelCreditor(context);
                break;
        }
        return iapCreditor;
    }
}
