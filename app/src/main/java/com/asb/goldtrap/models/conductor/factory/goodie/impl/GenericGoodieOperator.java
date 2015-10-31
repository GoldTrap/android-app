package com.asb.goldtrap.models.conductor.factory.goodie.impl;

import com.asb.goldtrap.models.complications.goodies.GoodieOperator;
import com.asb.goldtrap.models.complications.goodies.impl.NullGoodieOperator;
import com.asb.goldtrap.models.conductor.factory.goodie.GoodieOperatorFactory;
import com.asb.goldtrap.models.eo.Complication;

/**
 * Created by arjun on 31/10/15.
 */
public class GenericGoodieOperator implements GoodieOperatorFactory {
    public static final String DYNAMIC_GOODIE_VALUE_MODIFIER = "DYNAMIC_GOODIE_VALUE_MODIFIER";
    public static final String GOODIE_POSITION_MODIFIER = "GOODIE_POSITION_MODIFIER";
    private GoodieOperatorFactory positionModifierGoodieOperator =
            new PositionModifierGoodieOperator();
    private GoodieOperatorFactory valueModifierGoodieOperator = new ValueModifierGoodieOperator();

    @Override
    public GoodieOperator getGoodieOperator(Complication complication) {
        GoodieOperator operator;
        switch (complication.getOperator()) {
            case DYNAMIC_GOODIE_VALUE_MODIFIER:
                operator = valueModifierGoodieOperator.getGoodieOperator(complication);
                break;
            case GOODIE_POSITION_MODIFIER:
                operator = positionModifierGoodieOperator.getGoodieOperator(complication);
                break;
            default:
                operator = new NullGoodieOperator();
        }
        return operator;
    }
}
