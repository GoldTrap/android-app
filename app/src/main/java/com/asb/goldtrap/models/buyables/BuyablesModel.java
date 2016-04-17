package com.asb.goldtrap.models.buyables;

import com.asb.goldtrap.models.eo.Buyable;

/**
 * BuyablesModel.
 * Created by arjun on 17/04/16.
 */
public interface BuyablesModel {

    Buyable getBuyable(int position);

    int getBuyablesCount();
}
