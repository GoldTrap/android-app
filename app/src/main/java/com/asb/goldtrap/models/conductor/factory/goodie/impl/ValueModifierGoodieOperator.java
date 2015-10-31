package com.asb.goldtrap.models.conductor.factory.goodie.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.complications.goodies.impl.DynamicGoodieValueModifier;
import com.asb.goldtrap.models.complications.goodies.impl.NullGoodieOperator;
import com.asb.goldtrap.models.complications.series.impl.AP;
import com.asb.goldtrap.models.complications.series.impl.GP;
import com.asb.goldtrap.models.conductor.factory.goodie.GoodieOperatorFactory;
import com.asb.goldtrap.models.eo.Complication;
import com.asb.goldtrap.models.eo.StrategyData;

/**
 * Created by arjun on 31/10/15.
 */
public class ValueModifierGoodieOperator implements GoodieOperatorFactory {

    public static final String AP = "AP";
    public static final String GP = "GP";

    @Override
    public GoodieOperator getGoodieOperator(Complication complication) {
        GoodieOperator goodieOperator;
        StrategyData strategy = complication.getStrategy();
        switch (strategy.getType()) {
            case AP:
                goodieOperator = new DynamicGoodieValueModifier(new AP(strategy.getValue()));
                break;
            case GP:
                goodieOperator = new DynamicGoodieValueModifier(new GP(strategy.getValue()));
                break;
            default:
                goodieOperator = new NullGoodieOperator();
        }
        return goodieOperator;
    }
}
