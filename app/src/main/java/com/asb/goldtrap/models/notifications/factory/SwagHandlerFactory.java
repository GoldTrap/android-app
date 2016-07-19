package com.asb.goldtrap.models.notifications.factory;

import com.asb.goldtrap.models.notifications.SwagHandler;
import com.asb.goldtrap.models.notifications.impl.BarSwagHandler;
import com.asb.goldtrap.models.notifications.impl.CoinSwagHandler;
import com.asb.goldtrap.models.notifications.impl.DiamondSwagHandler;
import com.asb.goldtrap.models.notifications.impl.NoSwagHandler;
import com.asb.goldtrap.models.notifications.impl.NuggetSwagHandler;

/**
 * Swag Handler Factory.
 * Created by arjun on 18/07/16.
 */
public class SwagHandlerFactory {
    public static SwagHandler getSwagHandler(String type) {
        SwagHandler handler;
        switch (type) {
            case "GOLD_COINS":
                handler = new CoinSwagHandler();
                break;
            case "GOLD_NUGGETS":
                handler = new NuggetSwagHandler();
                break;
            case "GOLD_BARS":
                handler = new BarSwagHandler();
                break;
            case "DIAMONDS":
                handler = new DiamondSwagHandler();
                break;
            default:
                handler = new NoSwagHandler();
                break;
        }
        return handler;
    }
}
