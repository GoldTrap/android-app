package com.asb.goldtrap.models.goodie;

import com.asb.goldtrap.models.eo.Goodie;

/**
 * Goodie Model.
 * Created by arjun on 24/04/16.
 */
public interface GoodieModel {

    interface Listener {
        void dataChanged();
    }

    void loadGoodies();

    Goodie getGoodie(int position);

    int getGoodiesCount();
}
