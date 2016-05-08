package com.asb.goldtrap.models.buyables;

import com.asb.goldtrap.R;

/**
 * BuyableType.
 * Created by arjun on 17/04/16.
 */
public enum BuyableType {
    FLIP(R.string.flip, "get_flip", 18001, "Let\'s Flip Maccha"),
    PLUS_ONE(R.string.extra_chance, "get_plus_one", 18002, "Let\'s Play Again Maccha"),
    SKIP(R.string.skip, "get_skip", 18003, "Let\'s Skip Maccha"),
    DONATE(R.string.donate_name, "donate", 18004, "Let\'s Donate");

    private int nameRes;
    private String sku;
    private int requestCode;
    private String developerPayload;

    public int getNameRes() {
        return nameRes;
    }

    public String getSku() {
        return sku;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    BuyableType(int nameRes, String sku, int requestCode, String developerPayload) {
        this.nameRes = nameRes;
        this.sku = sku;
        this.requestCode = requestCode;
        this.developerPayload = developerPayload;
    }
}
