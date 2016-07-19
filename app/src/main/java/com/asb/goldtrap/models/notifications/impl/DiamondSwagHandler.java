package com.asb.goldtrap.models.notifications.impl;

import android.content.Context;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.states.enums.GoodiesState;
import com.asb.goldtrap.models.swag.SwagModel;
import com.asb.goldtrap.models.swag.impl.SwagModelImpl;

/**
 * Coins Swag Handler.
 * Created by arjun on 18/07/16.
 */
public class DiamondSwagHandler extends AbstractSwagHandler {
    @Override
    public void handleSwag(Context context, String value) {
        SwagModel swagModel = new SwagModelImpl(context);
        swagModel.addGoodieCount(GoodiesState.DIAMOND, Integer.valueOf(value));
        String qtyString = context.getResources()
                .getQuantityString(R.plurals.take_diamonds,
                        Integer.parseInt(value),
                        Integer.parseInt(value));
        notifyUser(context, qtyString);
    }
}
